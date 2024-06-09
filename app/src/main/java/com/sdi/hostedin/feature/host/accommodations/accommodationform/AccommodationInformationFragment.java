package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentAccommodationInformationBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationInformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int LOCAL_FRAGMENT_NUMBER = 6;
    private FragmentAccommodationInformationBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private String title;
    private String description;
    private String rules;
    private double nightPrice;
    private static Accommodation accommodationToEdit ;
    private static boolean isEdition;



    public AccommodationInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationInformationFragment newInstance(String param1, String param2) {
        AccommodationInformationFragment fragment = new AccommodationInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AccommodationInformationFragment newInstance(Accommodation param1, boolean param2) {
        AccommodationInformationFragment fragment = new AccommodationInformationFragment();
        Bundle args = new Bundle();
        accommodationToEdit = param1;
        isEdition = param2;
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
        binding = FragmentAccommodationInformationBinding.inflate(getLayoutInflater());

        if(!isEdition) {
            customActivityParent();
        }else{
            loadAccommodationInfo();
        }
        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                        .get(AccommodationFormViewModel.class);

        return binding.getRoot();
    }

    private void loadAccommodationInfo() {
        binding.etxTitle.setText(accommodationToEdit.getTitle());
        binding.etxDescription.setText(accommodationToEdit.getDescription());
        binding.etxRules.setText(accommodationToEdit.getRules());
        binding.etxNightPrice.setText(String.valueOf(accommodationToEdit.getNightPrice()));
    }

    @Override
    public void onResume() {
        super.onResume();
        title = null;
        description = null;
        rules = null;
        nightPrice = -1;

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                if (isAccommodationInformationValid()) {
                    collectInformation();
                    accommodationFormViewModel.selectAccommodationInformation(title, description, rules, nightPrice);
                    accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
                }
            }
        });

        accommodationFormViewModel.getTitle().observe(getViewLifecycleOwner(), title -> {
            if (title != null) {
                binding.etxTitle.setText(title);
            }
        });

        accommodationFormViewModel.getDescription().observe(getViewLifecycleOwner(), description -> {
            if (description != null) {
                binding.etxDescription.setText(description);
            }
        });

        accommodationFormViewModel.getRules().observe(getViewLifecycleOwner(), rules -> {
            if (rules != null) {
                binding.etxTitle.setText(rules);
            }
        });

        accommodationFormViewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            if (price != null) {
                binding.etxDescription.setText(String.valueOf(price));
            }
        });
    }

    public void customActivityParent() {
        if (getActivity() != null) {
            String styledText = getString(R.string.last_step_publishing_header);
            String messageButton = getString(R.string.ready);
            CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);

            TextView txvAccommodationPublishingMessage = getActivity().findViewById(R.id.txv_accommodation_publishing_message);
            Button btnReady = getActivity().findViewById(R.id.btn_next);

            txvAccommodationPublishingMessage.setText(styledTextSpanned);
            btnReady.setText(messageButton);
        }
    }

    private void collectInformation() {
        this.title = String.valueOf(binding.etxTitle.getText());
        this.description = String.valueOf(binding.etxDescription.getText());
        this.rules = String.valueOf(binding.etxRules.getText());
        String price = String.valueOf(binding.etxNightPrice.getText());
        if (!price.isEmpty()) {
            this.nightPrice = Double.parseDouble(price);
            accommodationFormViewModel.getPrice().setValue(nightPrice);
        }

        accommodationFormViewModel.getTitle().setValue(title);
        accommodationFormViewModel.getDescription().setValue(description);
        accommodationFormViewModel.getRules().setValue(rules);
    }

    private boolean isAccommodationInformationValid() {
        boolean isInformationValid = true;

        if (!isTitleValid()) {
            isInformationValid = false;
        } else if (!isDescriptionValid()) {
            isInformationValid = false;
        } else if (!isRuleValid()) {
            isInformationValid = false;
        } else if (!isPriceValid()) {
            isInformationValid = false;
        }

        return isInformationValid;
    }

    private boolean isTitleValid() {
        boolean isTitleValid = true;
        String title = String.valueOf(binding.etxTitle.getText());

        //TODO: Regex
        if (title == null || title.isEmpty()) {
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_accommodation_title_message));
            isTitleValid = false;
        } else {
            //TODO:
        }

        return isTitleValid;
    }

    private boolean isDescriptionValid() {
        boolean isDescriptionValid = true;
        String description = String.valueOf(binding.etxDescription.getText());

        //TODO:
        if (description == null || description.isEmpty()) {
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_accommodation_description_message));
            isDescriptionValid = false;
        } else {
            //TODO:
        }

        return isDescriptionValid;
    }

    private boolean isRuleValid() {
        boolean isRuleValid = true;
        String rules = String.valueOf(binding.etxRules.getText());

        //TODO:
        if (rules == null || rules.isEmpty()) {
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_accommodation_rules_description));
            isRuleValid = false;
        } else {
            //TODO:
        }

        return isRuleValid;
    }

    private boolean isPriceValid() {
        //TODO:
        boolean isPriceValid = true;
        String price = String.valueOf(binding.etxNightPrice.getText());
        double priceNight = -1;

        if (!price.isEmpty()) {
            try {
                priceNight = Double.parseDouble(price);
                if (priceNight <= 0) {
                    ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_night_price_instruction));
                    isPriceValid = false;
                }
            } catch (NumberFormatException e) {
                ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_valid_night_price));
                isPriceValid = false;
            }
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.enter_night_price_instruction));
            isPriceValid = false;
        }

        return isPriceValid;
    }
}