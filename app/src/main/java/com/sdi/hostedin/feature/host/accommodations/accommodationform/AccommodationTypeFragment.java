package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentAccommodationTypeBinding;
import com.sdi.hostedin.enums.AccommodationTypes;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationTypeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int LOCAL_FRAGMENT_NUMBER = 1;
    private FragmentAccommodationTypeBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private AccommodationTypes selectedAccommodationType;
    private Button[] typesButtons;

    public AccommodationTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationTypeFragment newInstance(String param1, String param2) {
        AccommodationTypeFragment fragment = new AccommodationTypeFragment();
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
        binding = FragmentAccommodationTypeBinding.inflate(getLayoutInflater());

        typesButtons = new Button[] {
                binding.inclHouse.btnAccommodationType,
                binding.inclApartment.btnAccommodationType,
                binding.inclCabin.btnAccommodationType,
                binding.inclCampament.btnAccommodationType,
                binding.inclCamper.btnAccommodationType,
                binding.inclShip.btnAccommodationType
        };
        configureTypesButtons();

        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                .get(AccommodationFormViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedAccommodationType = null;

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                ValidateAccommodationTypeSelected();
            }
        });
    }

    private void configureTypesButtons() {
        String[] types = {"Casa", "Departamento", "Cabaña", "Campamento", "Casa rodante", "Barco"};
        int[] typeIcons = {R.drawable.ic_house, R.drawable.ic_apartment, R.drawable.ic_cabin,
                R.drawable.ic_camp, R.drawable.ic_camper, R.drawable.ic_ship};

        for (int i = 0; i < typesButtons.length; i++) {
            typesButtons[i].setText(types[i]);
            typesButtons[i].setCompoundDrawablesWithIntrinsicBounds(0, typeIcons[i], 0, 0);
            typesButtons[i].setTag(i);
            typesButtons[i].setOnClickListener( v -> selectType((int) v.getTag(), (Button) v));
        }
    }

    private void selectType(int type, Button btnSelected) {
        switch (type) {
            case 0:
                selectedAccommodationType = AccommodationTypes.HOUSE;
                break;
            case 1:
                selectedAccommodationType = AccommodationTypes.APARTMENT;
                break;
            case 2:
                selectedAccommodationType = AccommodationTypes.CABIN;
                break;
            case 3:
                selectedAccommodationType = AccommodationTypes.CAMP;
                break;
            case 4:
                selectedAccommodationType = AccommodationTypes.CAMPER;
                break;
            case 5:
                selectedAccommodationType = AccommodationTypes.SHIP;
                break;
        }

        unselectTypes();
        showSelectedType(btnSelected);
    }

    private void unselectTypes() {
        for (Button typeButton: typesButtons) {
            int color = Color.parseColor("#FCFCFC");
            typeButton.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    private void showSelectedType(Button btnSelected) {
        int color = Color.parseColor("#5280DB");
        btnSelected.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void ValidateAccommodationTypeSelected() {
        if (selectedAccommodationType != null) {
            accommodationFormViewModel.selectAccommodationType(selectedAccommodationType.getDescription());
            accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "Selecciona un tipo de alojamiento" );
        }
    }
}