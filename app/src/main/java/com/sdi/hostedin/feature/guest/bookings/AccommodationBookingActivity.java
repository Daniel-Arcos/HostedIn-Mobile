package com.sdi.hostedin.feature.guest.bookings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityAccommodationBookingBinding;

public class AccommodationBookingActivity extends AppCompatActivity {

    ActivityAccommodationBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccommodationBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showAccommodationBookinhMessage();
    }

    private void showAccommodationBookinhMessage() {
        String styledText = getString(R.string.accommodation_booking_message);
        CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);
        binding.accommodationBookingMessageTxv.setText(styledTextSpanned);
    }
}