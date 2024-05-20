package com.sdi.hostedin.feature.host.bookings.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.databinding.ItemBookingMainInfoBinding;
import com.sdi.hostedin.utils.DateFormatterUtils;

public class HostBookingsListAdapter extends ListAdapter<Booking, HostBookingsListAdapter.BookingListViewHolder> {

    Context context;
    protected HostBookingsListAdapter(Context context) {super(DIFFER_CALLBACK); this.context = context;}

    public static final DiffUtil.ItemCallback<Booking> DIFFER_CALLBACK = new DiffUtil.ItemCallback<Booking>() {
        @Override
        public boolean areItemsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.get_id().equals(newItem.get_id());
        }


        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.equals(newItem);
        }
    };



    @NonNull
    @Override
    public BookingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookingMainInfoBinding binding = ItemBookingMainInfoBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new BookingListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListViewHolder holder, int position) {
        Booking booking = getItem(position);
        holder.bindBooking(booking);
    }


    public class BookingListViewHolder extends RecyclerView.ViewHolder{

        private ItemBookingMainInfoBinding binding;

        public BookingListViewHolder(@NonNull ItemBookingMainInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindBooking(Booking booking){
            binding.txvGuestName.setText(booking.getGuestName());
            String starDate = DateFormatterUtils.parseMongoDateToLocal(booking.getBeginningDate());
            String endDate = DateFormatterUtils.parseMongoDateToLocal(booking.getEndingDate());
            binding.txvStayDates.setText(starDate + " - " + endDate);
            binding.bttCancel.setOnClickListener(v ->{
                onItemClicListener.onItemClic(booking);
            });
            binding.executePendingBindings();
        }
    }

    private OnItemClickListener onItemClicListener;
    interface OnItemClickListener{
        void onItemClic(Booking booking);
    }
    public void setOnItemClicListener(OnItemClickListener onItemClicListener){
        this.onItemClicListener = onItemClicListener;
    }

}
