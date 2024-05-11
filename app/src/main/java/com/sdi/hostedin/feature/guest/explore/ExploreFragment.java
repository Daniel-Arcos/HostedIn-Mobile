package com.sdi.hostedin.feature.guest.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchByTextRequest;
import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentExploreBinding;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.user.ProfileActivity;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ExploreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentExploreBinding binding;
    private PlacesClient placesClient;

    ExploreViewModel exploreViewModel;
    private List<Place> places;
    ResultSearchingPlaceAdapter placeAdapter;
    public ExploreFragment() {

    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
        exploreViewModel =
                new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication())).get(ExploreViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        Places.initialize(this.getContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this.getContext());

        binding.rcyvPlacesResults.setLayoutManager(new LinearLayoutManager(this.getContext()));
        String lugar = exploreViewModel.getPlaceToSearch().getValue();
        binding.searchBar.setText(exploreViewModel.getPlaceToSearch().getValue());
        placeAdapter = new ResultSearchingPlaceAdapter(this.getContext());
        placeAdapter.setOnItemClickListener(placeResult -> {
            exploreViewModel.setPlaceToSearch(placeResult.getName());
            binding.searchView.hide();
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

//        ArrayList<String> imageUrls = new ArrayList<>();
//        imageUrls.add("https://rickandmortyapi.com/api/character/avatar/353.jpeg");
//        imageUrls.add("https://rickandmortyapi.com/api/character/avatar/353.jpeg");
//        imageUrls.add("https://rickandmortyapi.com/api/character/avatar/353.jpeg");
//
//        ImageAdapter adapter = new ImageAdapter(getContext(), imageUrls);
//        binding.recExample.setAdapter(adapter);
        return binding.getRoot();
    }

    private void changeToHostMenu() {
        Intent intent = new Intent(getContext(), HostMainActivity.class);
        startActivity(intent);
        this.requireActivity().finish();
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
                        PlaceResult placeResult = new PlaceResult(place.getName(), place.getAddress(), place.getId());
                        placeResults.add(placeResult);
                    }
                    placeAdapter.submitList(placeResults);
                })
                .addOnFailureListener(command -> {

                });
    }

}