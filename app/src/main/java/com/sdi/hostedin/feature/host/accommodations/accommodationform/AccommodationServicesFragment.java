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
import com.sdi.hostedin.databinding.FragmentAccommodationServicesBinding;
import com.sdi.hostedin.enums.AccommodationServices;
import com.sdi.hostedin.enums.AccommodationTypes;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationServicesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int LOCAL_FRAGMENT_NUMBER = 4;
    private FragmentAccommodationServicesBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private List<AccommodationServices> accommodationServices = new ArrayList<>();
    private Button[] servicesButtons;

    public AccommodationServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationServicesFragment newInstance(String param1, String param2) {
        AccommodationServicesFragment fragment = new AccommodationServicesFragment();
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
        binding = FragmentAccommodationServicesBinding.inflate(getLayoutInflater());

        servicesButtons = new Button[] {
                binding.inclInternet.btnAccommodationService,
                binding.inclTv.btnAccommodationService,
                binding.inclKitchen.btnAccommodationService,
                binding.inclWashingMachine.btnAccommodationService,
                binding.inclParking.btnAccommodationService,
                binding.inclAirConditioning.btnAccommodationService,
                binding.inclPool.btnAccommodationService,
                binding.inclGarden.btnAccommodationService,
                binding.inclLight.btnAccommodationService,
                binding.inclWater.btnAccommodationService
        };
        configureServiceButtons();

        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                        .get(AccommodationFormViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        accommodationServices.clear();

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                ValidateAccommodationServicesSelected();
            }
        });
    }

    private void configureServiceButtons() {
        String[] services = {
                "Internet", "TV", "Cocina", "Lavadora", "Estacionamiento",
                "Aire acondicionado", "Alberca", "Patio", "Luz", "Agua"
        };

        for (int i = 0; i < servicesButtons.length; i++) {
            Button currentButton = servicesButtons[i];
            currentButton.setText(services[i]);
            currentButton.setTag(i);
            currentButton.setOnClickListener( v -> selectService((int) v.getTag(), currentButton) );
        }
    }

    private void selectService(int serviceNumber, Button btnServiceSelected) {
        switch (serviceNumber) {
            case 0:
                manageSelectionService(AccommodationServices.INTERNET, btnServiceSelected);
                break;
            case 1:
                manageSelectionService(AccommodationServices.TV, btnServiceSelected);
                break;
            case 2:
                manageSelectionService(AccommodationServices.KITCHEN, btnServiceSelected);
                break;
            case 3:
                manageSelectionService(AccommodationServices.WASHING_MACHINE, btnServiceSelected);
                break;
            case 4:
                manageSelectionService(AccommodationServices.PARKING, btnServiceSelected);
                break;
            case 5:
                manageSelectionService(AccommodationServices.AIR_CONDITIONING, btnServiceSelected);
                break;
            case 6:
                manageSelectionService(AccommodationServices.POOL, btnServiceSelected);
                break;
            case 7:
                manageSelectionService(AccommodationServices.GARDEN, btnServiceSelected);
                break;
            case 8:
                manageSelectionService(AccommodationServices.LIGHT, btnServiceSelected);
                break;
            case 9:
                manageSelectionService(AccommodationServices.WATER, btnServiceSelected);
                break;
            default:
                break;
        }
    }

    private void manageSelectionService(AccommodationServices accommodationService, Button btnServiceSelected) {
        if (!accommodationServices.contains(accommodationService)) {
            selectAccommodationService(accommodationService, btnServiceSelected);
        } else {
            unselectAccommodationService(accommodationService, btnServiceSelected);
        }
    }

    private void selectAccommodationService(AccommodationServices accommodationService, Button btnServiceSelected) {
        accommodationServices.add(accommodationService);
        showSelectedService(btnServiceSelected);
    }

    private void showSelectedService(Button btnSelected) {
        int color = Color.parseColor("#5280DB");
        btnSelected.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void unselectAccommodationService(AccommodationServices accommodationService, Button btnServiceUnselected) {
        accommodationServices.remove(accommodationService);
        showUnselectedService(btnServiceUnselected);
    }

    private void showUnselectedService(Button btnUnselected) {
        int color = Color.parseColor("#FCFCFC");
        btnUnselected.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void ValidateAccommodationServicesSelected() {
        int numberOfServices = accommodationServices.size();
        if (numberOfServices > 0) {

            String[] selectedServices = new String[numberOfServices];
            int i = 0;
            for (AccommodationServices service : accommodationServices) {
                selectedServices[i] = service.getDescription();
                i++;
            }

            accommodationFormViewModel.selectAccommodationServices(selectedServices);
            accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "Selecciona al menos un servicio" );
        }
    }
}