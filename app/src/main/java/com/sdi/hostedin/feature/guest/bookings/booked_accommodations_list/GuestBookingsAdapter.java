package com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.databinding.ItemGuestBookedAccommodationBinding;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.TranslatorToSpanish;

import java.util.Arrays;
import java.util.Objects;

public class GuestBookingsAdapter extends ListAdapter<GuestBooking, GuestBookingsAdapter.GuestBookingsViewHolder> {

    Context context;
    private MutableLiveData<Boolean> showButton = new MutableLiveData<>();

    public void setShowButton(Boolean showButton) {
        this.showButton.setValue(showButton);
    }

    public static final DiffUtil.ItemCallback<GuestBooking> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GuestBooking>() {
                @Override
                public boolean areItemsTheSame(@NonNull GuestBooking oldItem, @NonNull GuestBooking newItem) {
                    return Objects.equals(oldItem.get_id(), newItem.get_id());
                }

                @Override
                public boolean areContentsTheSame(@NonNull GuestBooking oldItem, @NonNull GuestBooking newItem) {
                    return oldItem.equals(newItem) && Arrays.equals(oldItem.getAccommodation().getMainImage(), newItem.getAccommodation().getMainImage());
                }
            };

    protected GuestBookingsAdapter(@NonNull Context context, Boolean showButton) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.showButton.setValue(showButton);
    }

    @NonNull
    @Override
    public GuestBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGuestBookedAccommodationBinding binding = ItemGuestBookedAccommodationBinding.inflate(LayoutInflater.from(parent.getContext()));
        return  new GuestBookingsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestBookingsViewHolder holder, int position) {
        GuestBooking accommodation = getItem(position);
        holder.bindAccommodation(accommodation);
    }

    public class GuestBookingsViewHolder extends RecyclerView.ViewHolder{
        private ItemGuestBookedAccommodationBinding binding;

        public GuestBookingsViewHolder(@NonNull ItemGuestBookedAccommodationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindAccommodation(GuestBooking booking){
            if(booking.getAccommodation().getMainImage() != null){
                binding.imvAccommodation.setImageBitmap(ImageUtils.bytesToBitmap(booking.getAccommodation().getMainImage()));
            }
            binding.txvAddress.setText(booking.getAccommodation().getTitle());
            String dates = DateFormatterUtils.parseMongoDateToLocal(booking.getBeginningDate())
                    + " - "
                    + DateFormatterUtils.parseMongoDateToLocal(booking.getEndingDate());
            binding.txvDates.setText(dates);
            binding.txvPrice.setText("$ "+String.valueOf(booking.getTotalCost()));

            binding.txvStatus.setText(context.getString(R.string.hint_status) + " " +TranslatorToSpanish.getBookingSpanishStatus(context.getApplicationContext(), booking.getBookingStatus()));

            binding.bttReviewAccommodation.setOnClickListener(v->{
                onRateClick.onRateClick(booking);
            });
            binding.getRoot().setOnClickListener(v->{
                onItemClicListener.onItemClick(booking);
            });

            if(Boolean.FALSE.equals(showButton.getValue())){
                binding.bttReviewAccommodation.setVisibility(View.INVISIBLE);
            }
            else{
                binding.bttReviewAccommodation.setVisibility(View.VISIBLE);
            }

        }
    }
    private GuestBookingsAdapter.OnItemClickListener onItemClicListener;
    private GuestBookingsAdapter.OnRateClick onRateClick;
    interface OnItemClickListener{
        void onItemClick(GuestBooking accommodation);
    }
    interface OnRateClick{
        void onRateClick(GuestBooking accommodation);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClicListener){
        this.onItemClicListener = onItemClicListener;
    }
    public void setOnRateButtonClick(OnRateClick onRateClick){
        this.onRateClick = onRateClick;
    }
}
