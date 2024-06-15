package com.sdi.hostedin.feature.guest.explore.accommodations;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchByTextRequest;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentExploreBinding;
import com.sdi.hostedin.feature.guest.explore.accommodationdetails.AccommodationDetailsActivity;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.user.ProfileActivity;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ExploreFragment extends Fragment {

    public static final String USER_KEY = "user_key";
    private FragmentExploreBinding binding;
    private PlacesClient placesClient;

    ExploreViewModel exploreViewModel;
    private List<Place> places;
    ResultSearchingPlaceAdapter placeAdapter;
    AnimationDrawable animationDrawable;

    public ExploreFragment() {

    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        exploreViewModel =
                new ViewModelProvider(requireActivity(), new ViewModelFactory(requireActivity().getApplication())).get(ExploreViewModel.class);
        if (exploreViewModel.getIsNew().getValue()) {
            exploreViewModel.getAllAccommodationsExceptUserAccommodations();
            exploreViewModel.setIsNew(false);
        }

        User user = (User) getArguments().getParcelable(USER_KEY);
        exploreViewModel.setUserMutableLiveData(user);
        if (!exploreViewModel.getUserMutableLiveData().getValue().getRoles().contains("Host")) {
            binding.changeToHostBtn.setVisibility(View.GONE);
        }
        Places.initialize(this.getContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this.getContext());
        animationDrawable = (AnimationDrawable) binding.imgLoading.getDrawable();

        binding.rcyvPlacesResults.setLayoutManager(new LinearLayoutManager(this.getContext()));

        binding.searchBar.setText(exploreViewModel.getPlaceToSearch().getValue());

        placeAdapter = new ResultSearchingPlaceAdapter(this.getContext());
        placeAdapter.setOnItemClickListener(placeResult -> {
            exploreViewModel.setPlaceToSearch(placeResult.getName());
            binding.searchView.hide();
            exploreViewModel.searchAccommodations(placeResult.getLat(), placeResult.getLng());
        });

        exploreViewModel.getPlaceToSearch().observe(getViewLifecycleOwner(), s -> {
            binding.searchBar.setText(s);
        });

        binding.rcyvPlacesResults.setAdapter(placeAdapter);
        binding.searchView.setupWithSearchBar(binding.searchBar);
        binding.searchView
                .getEditText()
                .setOnEditorActionListener((v, actionId, event) -> {
                            PerformSearchPlaces(binding.searchView.getText().toString());
                            return false;
                        });

        binding.changeToHostBtn.setOnClickListener(v -> changeToHostMenu());
        binding.profileBtn.setOnClickListener(v -> changeToUserProfile());
        configureDeleteResultsButton();
        binding.rcyvAccommodationsExplore.setLayoutManager(new LinearLayoutManager(this.getContext()));
        AccommodationAdapter adapter = new AccommodationAdapter(this.getContext());
        adapter.setOnItemClickListener(this::goToAccommodationDetails);
        binding.rcyvAccommodationsExplore.setAdapter(adapter);
        adapter.submitList(exploreViewModel.accommodationsLiveData.getValue());
        exploreViewModel.getAccommodationsLiveData().observe(getViewLifecycleOwner(), accommodations -> {
            adapter.submitList(accommodations);
            if (accommodations.size() == 0) {
                binding.imgNoResults.setVisibility(View.VISIBLE);
                binding.txvNoResults.setVisibility(View.VISIBLE);
                binding.rcyvAccommodationsExplore.setVisibility(View.GONE);
            } else {
                binding.imgNoResults.setVisibility(View.GONE);
                binding.txvNoResults.setVisibility(View.GONE);
                binding.rcyvAccommodationsExplore.setVisibility(View.VISIBLE);
            }
        });

        binding.swiperefresh.setOnRefreshListener(this:: refreshExploreView);

        manageLoading();

        return binding.getRoot();
    }

    private void refreshExploreView() {
        binding.searchBar.setText("");
        exploreViewModel.setPlaceToSearch("");
        exploreViewModel.getAllAccommodationsExceptUserAccommodations();
        binding.swiperefresh.setRefreshing(false);
    }

    private void configureDeleteResultsButton() {
        binding.btnDeleteResults.setOnClickListener(v -> {
            binding.searchBar.setText("");
            exploreViewModel.setPlaceToSearch("");
            exploreViewModel.getAllAccommodationsExceptUserAccommodations();
        });
    }
    private void changeToHostMenu() {
        Intent intent = new Intent(this.getActivity(), HostMainActivity.class);
        if (exploreViewModel.getRequestStatusMutableLiveData().getValue() != null) {
            intent.putExtra(HostMainActivity.USER_KEY, exploreViewModel.getUserMutableLiveData().getValue());
            startActivity(intent);
            this.getActivity().finish();
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.messg_generic_error));
        }
    }

    private void changeToUserProfile() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }

    private void PerformSearchPlaces(String placeToSearch) {
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        final SearchByTextRequest searchByTextRequest = SearchByTextRequest.builder(placeToSearch, placeFields)
                .setMaxResultCount(10)
                .build();

        placesClient.searchByText(searchByTextRequest)
                .addOnSuccessListener(response -> {
                    places = response.getPlaces();
                    ArrayList<PlaceResult>  placeResults = new ArrayList<>();
                    for (Place place:places) {
                        PlaceResult placeResult = new PlaceResult(place.getName(), place.getAddress(), place.getId(), place.getLatLng().latitude, place.getLatLng().longitude);
                        placeResults.add(placeResult);
                    }
                    placeAdapter.submitList(placeResults);
                });
    }

    private void goToAccommodationDetails(Accommodation accommodation) {
        Intent intent = new Intent(this.getContext(), AccommodationDetailsActivity.class);
        intent.putExtra(AccommodationDetailsActivity.ACCOMMODATION_KEY, accommodation);
        intent.putExtra(AccommodationDetailsActivity.IS_HOST_KEY, false);
        startActivity(intent);
    }

    private void manageLoading() {
        exploreViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.imgNoResults.setVisibility(View.GONE);
                    binding.txvNoResults.setVisibility(View.GONE);
                    binding.rcyvAccommodationsExplore.setVisibility(View.GONE);
                    animationDrawable.start();
                    binding.imgLoading.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    animationDrawable.stop();
                    binding.imgLoading.setVisibility(View.GONE);
                    break;
                case ERROR:
                    animationDrawable.stop();
                    binding.imgLoading.setVisibility(View.GONE);
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}