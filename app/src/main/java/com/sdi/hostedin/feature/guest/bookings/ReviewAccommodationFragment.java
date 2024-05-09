package com.sdi.hostedin.feature.guest.bookings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentReviewAccommodationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewAccommodationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewAccommodationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentReviewAccommodationBinding binding;

    public ReviewAccommodationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewAccommodationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewAccommodationFragment newInstance(String param1, String param2) {
        ReviewAccommodationFragment fragment = new ReviewAccommodationFragment();
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
        binding = FragmentReviewAccommodationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}