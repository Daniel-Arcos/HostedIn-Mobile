package com.sdi.hostedin.feature.guest.explore.accommodations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.AccomodationExploreResultItemBinding;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.Arrays;
import java.util.Objects;

public class AccommodationAdapter extends ListAdapter<Accommodation, AccommodationAdapter.AcViewHolder> {

    public static final DiffUtil.ItemCallback<Accommodation> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Accommodation>() {
                @Override
                public boolean areItemsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
                    return oldItem.equals(newItem) && Arrays.equals(oldItem.getMainImage(), newItem.getMainImage());
                }
            };

    Context context;
    protected AccommodationAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public AccommodationAdapter.AcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AccomodationExploreResultItemBinding binding = AccomodationExploreResultItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new AcViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationAdapter.AcViewHolder holder, int position) {
        Accommodation accommodation = getItem(position);
        holder.bind(accommodation);
    }

    private OnItemClickListener onItemClickListener;
    interface OnItemClickListener {
        void onItemClick(Accommodation accommodation);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public class AcViewHolder extends RecyclerView.ViewHolder {

        private final AccomodationExploreResultItemBinding binding;
        public AcViewHolder(@NonNull AccomodationExploreResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Accommodation accommodation) {
            if (accommodation.getMainImage() != null) {
                binding.imvAccommodation.setImageBitmap(ImageUtils.bytesToBitmap(accommodation.getMainImage()));
            }
            binding.txvTitle.setText(String.valueOf(accommodation.getTitle()));
            binding.price.setText("$ " + String.valueOf(accommodation.getNightPrice()) + " MXN");
            binding.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemClick(accommodation);
            });
        }
    }
}
