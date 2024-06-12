package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.ActivityAccommodationFormBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

public class AccommodationFormActivity extends AppCompatActivity {

    private ActivityAccommodationFormBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private int fragmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccommodationFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accommodationFormViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(AccommodationFormViewModel.class);

        accommodationFormViewModel.getAccommodationMutableLiveData().observe(this, accommodation -> {
            controlNextFragment(accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue());
        });

        accommodationFormViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbCreateAccommodation.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbCreateAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, getString(R.string.accommodation_created_successfully));
                    finish();
                    break;
                case ERROR:
                    binding.pgbCreateAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });

        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        getSupportFragmentManager().popBackStack();

        showAccommodationPublishingMessage();
        binding.btnNext.setOnClickListener( v -> btnNextClick() );

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();

                    accommodationFormViewModel.backFragment(fragmentNumber);
                    fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void showAccommodationPublishingMessage() {
        String styledText = getString(R.string.accommodation_publishing_header);
        CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);
        binding.txvAccommodationPublishingMessage.setText(styledTextSpanned);

        binding.btnNext.setText(R.string.next);
    }

    private void btnNextClick() {
        accommodationFormViewModel.nextFragment(fragmentNumber);
    }

    private void controlNextFragment(int fragmentNumber) {
        switch (fragmentNumber) {
            case 1:
                showAccommodationTypeFragment();
                break;
            case 2:
                showAccommodationLocationFragment();
                break;
            case 3:
                showAccommodationBasicsFragment();
                break;
            case 4:
                showAccommodationServicesFragment();
                break;
            case 5:
                showAccommodationMultimediaFragment();
                break;
            case 6:
                showAccommodationInformationFragment();
                break;
            case 7:
                finishPublication();
                break;
            default:
                break;
        }
    }

    private void showAccommodationTypeFragment() {
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationTypeFragment accommodationTypeFragment = AccommodationTypeFragment.newInstance(null,  false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationTypeFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationLocationFragment() {
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationLocationFragment accommodationLocationFragment = AccommodationLocationFragment.newInstance(null, false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationLocationFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationBasicsFragment(){
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationBasicsFragment accommodationBasicsFragment = AccommodationBasicsFragment.newInstance(null, false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationBasicsFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationServicesFragment() {
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationServicesFragment accommodationServicesFragment = AccommodationServicesFragment.newInstance(null, false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationServicesFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationMultimediaFragment() {
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationMultimediaFragment accommodationMultimediaFragment = AccommodationMultimediaFragment.newInstance(null, false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationMultimediaFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationInformationFragment() {
        fragmentNumber = accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue();
        AccommodationInformationFragment accommodationInformationFragment = AccommodationInformationFragment.newInstance(null, false, null);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), accommodationInformationFragment, null)
                .addToBackStack(null)
                .commit();
    }

    private void finishPublication() {
        Accommodation newAccommodation = accommodationFormViewModel.getAccommodationMutableLiveData().getValue();

        if (newAccommodation.getTitle() != null && !newAccommodation.getTitle().isEmpty() && (newAccommodation.getId() == null || newAccommodation.getId().isEmpty()) ) {
            accommodationFormViewModel.createAccommodation(newAccommodation);
        }
    }

}