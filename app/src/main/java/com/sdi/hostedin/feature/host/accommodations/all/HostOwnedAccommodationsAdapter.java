package com.sdi.hostedin.feature.host.accommodations.all;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.ItemHostBookedAccommodationBinding;

public class HostOwnedAccommodationsAdapter extends ListAdapter<Accommodation, HostOwnedAccommodationsAdapter.HostOwnedAccViewHolder> {

    private Context context;

    public static final DiffUtil.ItemCallback<Accommodation> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Accommodation>() {
                @Override
                public boolean areItemsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
                    return oldItem.equals(newItem);
                }
            };

    protected HostOwnedAccommodationsAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public HostOwnedAccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHostBookedAccommodationBinding binding = ItemHostBookedAccommodationBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new HostOwnedAccViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HostOwnedAccViewHolder holder, int position) {
        Accommodation accommodation = getItem(position);
        holder.bindAccommodation(accommodation);
    }


    public class HostOwnedAccViewHolder extends RecyclerView.ViewHolder{

        private ItemHostBookedAccommodationBinding binding;
        public HostOwnedAccViewHolder(@NonNull ItemHostBookedAccommodationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindAccommodation(Accommodation accommodation){
            binding.txvAddress.setText(accommodation.getTitle());
            binding.imvAccommodation.setBackgroundColor(Color.LTGRAY);
            binding.txvPrice.setText("$ "+String.valueOf(accommodation.getNightPrice() +" MXN"));
            binding.bttSeeBookingDetails.setBackgroundResource(R.drawable.edit_acc_icon);
            binding.txvBttHint.setText("Edit");
            binding.getRoot().setOnClickListener(v->{
                onItemClickListener.onItemClick(accommodation);
            });
            binding.bttSeeBookingDetails.setOnClickListener(v->{
                onEditClick.onEditClick(accommodation);
            });
        }

    }


    private HostOwnedAccommodationsAdapter.OnItemClickListener onItemClickListener;
    private HostOwnedAccommodationsAdapter.OnEditClick onEditClick;
    interface OnItemClickListener{
        void onItemClick(Accommodation accommodation);
    }
    interface OnEditClick{
        void onEditClick(Accommodation accommodation);
    }
    public void setOnItemClickListener(HostOwnedAccommodationsAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnEditButtonClick(HostOwnedAccommodationsAdapter.OnEditClick onEditClick){
        this.onEditClick = onEditClick;
    }
}
