package com.sdi.hostedin.feature.guest.bookings.review;

import android.app.Dialog;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentReviewAccommodationBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

public class ReviewAccommodationFragment extends DialogFragment {


    private  FragmentReviewAccommodationBinding binding;
    private static String accommodationId;
    private static String userId;
    private ReviewAccommodationViewModel reviewAccommodationViewModel;

    public ReviewAccommodationFragment() {
    }

    public static ReviewAccommodationFragment newInstance(String accommodationId, String userId) {
        ReviewAccommodationFragment fragment = new ReviewAccommodationFragment();
        ReviewAccommodationFragment.accommodationId = accommodationId;
        ReviewAccommodationFragment.userId = userId;
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
        binding = FragmentReviewAccommodationBinding.inflate(inflater, container, false);
        reviewAccommodationViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication())).get(ReviewAccommodationViewModel.class);
        binding.bttRate.setOnClickListener(v -> {
            if(checkFieldsContent()){
                savedNewReview();
            }
        });
        binding.imbCloseRateView.setOnClickListener(this::CloseDialogFragment);
        manageLoading();
        return binding.getRoot();
    }

    private void CloseDialogFragment(View view) {
        reviewAccommodationViewModel.getShowFirstTime().setValue(false);
        this.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = getResources().getDisplayMetrics().heightPixels / 2;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private boolean checkFieldsContent(){
        if(binding.etxGuestReview.getEditText().getText().length() <= 0){
            ToastUtils.showShortInformationMessage(getActivity(), getString(R.string.must_enter_review));
            return false;
        }
        float rate = binding.rtbScore.getRating();
        if (rate < 0){
            ToastUtils.showShortInformationMessage(getActivity(), getString(R.string.must_rate_accommodation));
            return false;
        }
        return  true;
    }

    private void savedNewReview(){
        reviewAccommodationViewModel.getShowFirstTime().setValue(true);
        Review review = new Review();
        User user = new User();
        review.setAccommodation(accommodationId);
        review.setReviewDescription(binding.etxGuestReview.getEditText().getText().toString().trim());
        review.setRating(binding.rtbScore.getRating());
        user.setId(userId);
        review.setGuestUser(user);
        reviewAccommodationViewModel.saveNewReview(review);
    }

    private void manageLoading() {
        reviewAccommodationViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    if(Boolean.TRUE.equals(reviewAccommodationViewModel.getShowFirstTime().getValue())) {
                        ToastUtils.showShortInformationMessage(this.getContext(),status.getMessage());
                        CloseDialogFragment(getView());
                    }
                    break;
                case ERROR:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    if(Boolean.TRUE.equals(reviewAccommodationViewModel.getShowFirstTime().getValue())) {
                        ToastUtils.showShortInformationMessage(this.getContext(), status.getMessage());
                    }
            }
        });


    }

}