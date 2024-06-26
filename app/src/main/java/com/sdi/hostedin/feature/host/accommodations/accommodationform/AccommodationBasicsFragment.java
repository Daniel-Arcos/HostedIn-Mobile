package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
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
    private static final int MAX_BASICS_NUMBER = 50;
    private FragmentAccommodationBasicsBinding binding;
    private static AccommodationFormViewModel accommodationFormViewModel;
    private ItemAccommodationBasicBinding[] includeBasics;
    private int guestsNumber;
    private int roomsNumber;
    private int bedsNumber;
    private int bathroomsNumber;

    private static Accommodation accommodationToEdit;
    private static boolean isEdition = false;

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
    public static AccommodationBasicsFragment newInstance(Accommodation accommodation, boolean isEdition, AccommodationFormViewModel accFormVm) {
        AccommodationBasicsFragment fragment = new AccommodationBasicsFragment();
        Bundle args = new Bundle();
        accommodationToEdit = accommodation;
        AccommodationBasicsFragment.isEdition = isEdition;
        accommodationFormViewModel = accFormVm;
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

        if(!isEdition){
            accommodationFormViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                .get(AccommodationFormViewModel.class);
        }else {
            loadQuantitiesFromViewModel();
        }


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetValues();

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber ->{
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                if (areQuantitiesValid()) {
                    accommodationFormViewModel.selectBasics(guestsNumber, roomsNumber, bedsNumber, bathroomsNumber);
                    accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
                }
            }
        });

        loadQuantitiesFromViewModel();

    }

    public void SetBasicsForEdition(){
        if(areQuantitiesValid()){
            accommodationFormViewModel.selectBasics(guestsNumber, roomsNumber, bedsNumber, bathroomsNumber);
        }
    }

    private void resetValues() {
        guestsNumber = 0;
        roomsNumber = 0;
        bedsNumber = 0;
        bathroomsNumber = 0;

        binding.inclGuests.etxQuantity.setText(String.valueOf(guestsNumber));
        binding.inclRooms.etxQuantity.setText(String.valueOf(roomsNumber));
        binding.inclBeds.etxQuantity.setText(String.valueOf(bedsNumber));
        binding.inclBathrooms.etxQuantity.setText(String.valueOf(bathroomsNumber));
    }

    private void loadQuantitiesFromViewModel() {
        int guestsSelected = accommodationFormViewModel.getAccommodationMutableLiveData().getValue().getGuestsNumber();
        int roomsSelected = accommodationFormViewModel.getAccommodationMutableLiveData().getValue().getRoomsNumber();
        int bedsSelected = accommodationFormViewModel.getAccommodationMutableLiveData().getValue().getBedsNumber();
        int bathroomsSelected = accommodationFormViewModel.getAccommodationMutableLiveData().getValue().getBathroomsNumber();

        if (guestsSelected > 0 && roomsSelected > 0 && bedsSelected >= 0 && bathroomsSelected >= 0) {
            binding.inclGuests.etxQuantity.setText(String.valueOf(guestsSelected));
            binding.inclRooms.etxQuantity.setText(String.valueOf(roomsSelected));
            binding.inclBeds.etxQuantity.setText(String.valueOf(bedsSelected));
            binding.inclBathrooms.etxQuantity.setText(String.valueOf(bathroomsSelected));
        }
    }

    private void configureBasics() {
        String[] basics =
        {
            getString(R.string.guests),
            getString(R.string.rooms),
            getString(R.string.beds),
            getString(R.string.bathrooms)
        };

        for(int i = 0 ; i < includeBasics.length ; i++ ) {
            ItemAccommodationBasicBinding item = includeBasics[i];
            item.txvNameOfBasic.setText(basics[i]);
            item.btnIncrement.setOnClickListener( v -> incrementQuantity( item.etxQuantity ) );
            item.btnDecrement.setOnClickListener( v -> decrementQuantity( item.etxQuantity) );
        }
    }

    private void incrementQuantity(EditText etxQuantity) {
        int currentQuantity = Integer.parseInt(etxQuantity.getText().toString());
        if (currentQuantity < MAX_BASICS_NUMBER) {
            currentQuantity++;
            etxQuantity.setText(String.valueOf(currentQuantity));
        } else {
            String message = String.format(getString(R.string.max_basics_quantity), MAX_BASICS_NUMBER);
            ToastUtils.showShortInformationMessage(this.getContext(), message);
        }
    }

    private void decrementQuantity(EditText etxQuantity) {
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
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.must_adjust_guests_number));
        } else if (!isRoomsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.must_adjust_rooms_number));
        }else if (!isBedsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.must_adjust_beds_number));
        } else if (!isBathroomsNumberValid()) {
            areQuantitiesValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.must_adjust_bathrooms_number));
        }

        return areQuantitiesValid;
    }

    private boolean isGuestsNumberValid() {
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