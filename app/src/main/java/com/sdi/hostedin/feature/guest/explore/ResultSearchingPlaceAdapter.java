package com.sdi.hostedin.feature.guest.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.databinding.PlaceResultItemBinding;

public class ResultSearchingPlaceAdapter extends ListAdapter<PlaceResult, ResultSearchingPlaceAdapter.PlaceResultViewHolder> {

    public static final DiffUtil.ItemCallback<PlaceResult> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PlaceResult>() {
                @Override
                public boolean areItemsTheSame(@NonNull PlaceResult oldItem, @NonNull PlaceResult newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull PlaceResult oldItem, @NonNull PlaceResult newItem) {
                    return oldItem.equals(newItem);
                }
            };

    Context context;

    protected ResultSearchingPlaceAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ResultSearchingPlaceAdapter.PlaceResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceResultItemBinding binding = PlaceResultItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PlaceResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultSearchingPlaceAdapter.PlaceResultViewHolder holder, int position) {
        PlaceResult placeResult = getItem(position);
        holder.bind(placeResult);
    }

    private OnItemClickListener onItemClickListener;
    interface OnItemClickListener {
        void onItemClick(PlaceResult placeResult);
    }

    public void setOnItemClickListener(OnItemClickListener
                                               onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class PlaceResultViewHolder extends RecyclerView.ViewHolder {

        private final PlaceResultItemBinding binding;
        public PlaceResultViewHolder(@NonNull PlaceResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlaceResult placeResult) {
            binding.txvPlaceName.setText(placeResult.getName());
            binding.txvPlaceAddress.setText(placeResult.getAddress());
            binding.getRoot().setOnClickListener( v -> {
                onItemClickListener.onItemClick(placeResult);
            });
        }
    }
}
