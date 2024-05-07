package com.sdi.hostedin.feature.guest.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentExploreBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.user.ProfileActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentExploreBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        binding.searchView.setupWithSearchBar(binding.searchBar);
        binding.searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            binding.searchBar.setText(binding.searchView.getText());
                             binding.searchView.hide();
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

}