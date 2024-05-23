package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.databinding.ItemUserAccommodationReviewBinding;
import com.sdi.hostedin.utils.ImageUtils;

public class ReviewAdapter extends ListAdapter<Review, ReviewViewHolder> {

    public static final DiffUtil.ItemCallback<Review> DIFF_CALLBACK = new DiffUtil.ItemCallback<Review>() {
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected ReviewAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserAccommodationReviewBinding binding = ItemUserAccommodationReviewBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        holder.bind(review);
    }
}

class ReviewViewHolder extends RecyclerView.ViewHolder {
    private ItemUserAccommodationReviewBinding binding;


    public ReviewViewHolder(@NonNull ItemUserAccommodationReviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Review review) {
        ImageUtils.loadProfilePhoto(review.getGuestUser(), binding.imvProfilePhoto);
        binding.txvGuestName.setText(String.valueOf(review.getGuestUser().getFullName()));
        binding.txvReviewDescription.setText(String.valueOf(review.getReviewDescription()));
        binding.rtbUserRating.setRating(review.getRating());

        binding.executePendingBindings();

    }
}
