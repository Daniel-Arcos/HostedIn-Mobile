package com.sdi.hostedin.feature.host.accommodations;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.util.Log;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityAccommodationFormBinding;

public class AccommodationFormActivity extends AppCompatActivity {

    private ActivityAccommodationFormBinding binding;
    private int fragmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccommodationFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentNumber = 1;
        getSupportFragmentManager().popBackStack();    // REVIEW

        showAccommodationPublishingMessage();
        showAccommodationTypeFragment();
        binding.nextBtn.setOnClickListener( v -> controlClickNext() );

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                    fragmentNumber--;
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showAccommodationPublishingMessage() {
        String styledText = getString(R.string.accommodation_publishing_message);
        CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);
        binding.accommodationPublishingMessageTxv.setText(styledTextSpanned);
    }

    private void controlClickNext() {
        switch (fragmentNumber) {
            case 1:
                showAccommodationLocationFragment();
                break;
            case 2:
                showAccommodationBasicsFragment();
                break;
            case 3:
                showAccommodationServicesFragment();
                break;
            case 4:
                showAccommodationMultimediaFragment();
                break;
            case 5:
                showAccommodationInformationFragment();
                break;
            case 6:
                finishPublication();
                break;
            default:
                break;
        }
    }

    private void showAccommodationTypeFragment() {
        fragmentNumber = 1;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationTypeFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationLocationFragment() {
        fragmentNumber = 2;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationLocationFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationBasicsFragment(){
        fragmentNumber = 3;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationBasicsFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationServicesFragment() {
        fragmentNumber = 4;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationServicesFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationMultimediaFragment() {
        fragmentNumber = 5;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationMultimediaFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void showAccommodationInformationFragment() {
        fragmentNumber = 6;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragmentContainer.getId(), AccommodationInformationFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    private void finishPublication() {
        // TODO
    }

}