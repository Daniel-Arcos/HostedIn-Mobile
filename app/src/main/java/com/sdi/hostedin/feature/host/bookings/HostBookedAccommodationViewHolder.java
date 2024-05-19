package com.sdi.hostedin.feature.host.bookings;

import androidx.annotation.NonNull;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.ItemHostBookedAccommodationBinding;
import com.sdi.hostedin.feature.guest.explore.ImageAdapter;

public class HostBookedAccommodationViewHolder extends ImageAdapter.ViewHolder {
    private ItemHostBookedAccommodationBinding binding;


    public HostBookedAccommodationViewHolder(@NonNull ItemHostBookedAccommodationBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindBookedAccommodation(Accommodation accommodation){
        //binding.imvAccommodation = accommodation.image;
        //
    }

}
