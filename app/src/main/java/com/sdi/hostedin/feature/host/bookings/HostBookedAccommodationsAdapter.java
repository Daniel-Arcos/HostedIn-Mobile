package com.sdi.hostedin.feature.host.bookings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.databinding.ItemHostBookedAccommodationBinding;

public class HostBookedAccommodationsAdapter extends ListAdapter<BookedAccommodation, HostBookedAccommodationsAdapter.HostBookedAccViewHolder> {

    Context context;
    public static final DiffUtil.ItemCallback<BookedAccommodation> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BookedAccommodation>() {
                @Override
                public boolean areItemsTheSame(@NonNull BookedAccommodation oldItem, @NonNull BookedAccommodation newItem) {
                    return oldItem.get_id().equals(newItem.get_id());
                }


                @Override
                public boolean areContentsTheSame(@NonNull BookedAccommodation oldItem, @NonNull BookedAccommodation newItem) {
                    return oldItem.equals(newItem);
                }
            };

    protected HostBookedAccommodationsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }


    @NonNull
    @Override
    public HostBookedAccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHostBookedAccommodationBinding binding = ItemHostBookedAccommodationBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new HostBookedAccViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HostBookedAccViewHolder holder, int position) {
        BookedAccommodation accommodation = getItem(position);
        holder.bindBookedAccommodation(accommodation);
    }

    public class HostBookedAccViewHolder extends RecyclerView.ViewHolder{

        private final ItemHostBookedAccommodationBinding binding;
        public HostBookedAccViewHolder(@NonNull ItemHostBookedAccommodationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindBookedAccommodation(BookedAccommodation accommodation){
            binding.txvAddress.setText(String.valueOf(accommodation.getTitle()));
            binding.txvPrice.setText("$ " + String.valueOf(accommodation.getNightPrice()));
            binding.bttSeeBookingDetails.setOnClickListener(v->{
                onItemClicListener.onItemClick(accommodation);
            });
        }
    }

    private HostBookedAccommodationsAdapter.OnItemClickListener onItemClicListener;
    interface OnItemClickListener{
        void onItemClick(BookedAccommodation accommodation);
    }
    public void setOnItemClickListener(HostBookedAccommodationsAdapter.OnItemClickListener onItemClicListener){
        this.onItemClicListener = onItemClicListener;
    }
}
