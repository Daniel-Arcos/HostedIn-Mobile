package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sdi.hostedin.databinding.FragmentAccommodationBasicsBinding;
import com.sdi.hostedin.databinding.ItemAccommodationBasicBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationBasicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationBasicsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int LOCAL_FRAGMENT_NUMBER = 3;
    private FragmentAccommodationBasicsBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private ItemAccommodationBasicBinding[] includeBasics;
    private int guestsNumber;
    private int roomsNumber;
    private int bedsNumber;
    private int bathroomsNumber;

    public AccommodationBasicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationBasicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationBasicsFragment newInstance(String param1, String param2) {
        AccommodationBasicsFragment fragment = new AccommodationBasicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccommodationBasicsBinding.inflate(getLayoutInflater());

        includeBasics = new ItemAccommodationBasicBinding[]{
                binding.inclGuests,
                binding.inclRooms,
                binding.inclBeds,
                binding.inclBathrooms
        };

        configureBasics();

        accommodationFormViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                .get(AccommodationFormViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        guestsNumber = 0;
        roomsNumber = 0;
        bedsNumber = 0;
        bathroomsNumber = 0;

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber ->{
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                if (areQuantitiesValid()) {
                    accommodationFormViewModel.selectBasics(guestsNumber, roomsNumber, bedsNumber, bathroomsNumber);
                    accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
                }
            }
        });
    }

    private void configureBasics() {
        String[] basics = {"Huéspedes", "Habitaciones", "Camas", "Baños"};

        for(int i = 0 ; i < includeBasics.length ; i++ ) {
            ItemAccommodationBasicBinding item = includeBasics[i];
            item.txvNameOfBasic.setText(basics[i]);
            item.btnIncrement.setOnClickListener( v -> incrementQuantity( item.etxQuantity ) );
            item.btnDecrement.setOnClickListener( v -> decrementQuantity( item.etxQuantity) );
        }
    }

    private void incrementQuantity(EditText etxQuantity) {
        // TODO
        int currentQuantity = Integer.parseInt(etxQuantity.getText().toString());
        currentQuantity++;
        etxQuantity.setText(String.valueOf(currentQuantity));
    }

    private void decrementQuantity(EditText etxQuantity) {
        //TODO
        int currentQuantity = Integer.parseInt(etxQuantity.getText().toString());
        if (currentQuantity > 0) {
            currentQuantity--;
            etxQuantity.setText(String.valueOf(currentQuantity));
        }
    }

    private boolean areQuantitiesValid() {
        boolean areQuantitiesValid = true;

        if (!isGuestsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "Debes ajustar la cantidad de Huespedes");
        } else if (!isRoomsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "Debes ajustar la cantidad de Habitaciones");
        }else if (!isBedsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "Debes ajustar la cantidad Camas");
        } else if (!isBathroomsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "Debes ajustar la cantidad de Baños");
        }

        return areQuantitiesValid;
    }

    private boolean isGuestsNumberValid() {
        // TODO
        boolean isGuestNumberValid = false;
        String guests = String.valueOf(includeBasics[0].etxQuantity.getText());

        if (guests != null && !guests.isEmpty()) {
            int guestNumber = Integer.parseInt(guests);

            if (guestNumber > 0) {
                isGuestNumberValid = true;
                this.guestsNumber = guestNumber;
            }
        }

        return isGuestNumberValid;
    }

    private boolean isRoomsNumberValid() {
        // TODO
        boolean isRoomsNumberValid = false;
        String rooms = String.valueOf(includeBasics[1].etxQuantity.getText());

        if (rooms != null && !rooms.isEmpty()) {
            int roomNumber = Integer.parseInt(rooms);

            if (roomNumber > 0) {
                isRoomsNumberValid = true;
                this.roomsNumber = roomNumber;
            }
        }

        return isRoomsNumberValid;
    }

    private boolean isBedsNumberValid() {
        boolean isBedsNumberValid = false;
        String beds = String.valueOf(includeBasics[2].etxQuantity.getText());

        if (beds != null && !beds.isEmpty()) {
            int bedNumber = Integer.parseInt(beds);

            if (bedNumber >= 0) {
                isBedsNumberValid = true;
                this.bedsNumber = bedNumber;
            }
        }

        return isBedsNumberValid;
    }

    private boolean isBathroomsNumberValid() {
        boolean isBathroomsNumberValid = false;
        String bathrooms = String.valueOf(includeBasics[3].etxQuantity.getText());

        if (bathrooms != null && !bathrooms.isEmpty()) {
            int bathroomNumber = Integer.parseInt(bathrooms);

            if (bathroomNumber >= 0) {
                isBathroomsNumberValid = true;
                this.bathroomsNumber = bathroomNumber;
            }
        }

        return isBathroomsNumberValid;
    }
}