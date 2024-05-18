package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.net.Uri;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentAccommodationMultimediaBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationMultimediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationMultimediaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int LOCAL_FRAGMENT_NUMBER = 5;
    private FragmentAccommodationMultimediaBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private Uri[] temporaryPhotos;
    private Uri temporaryVideo;

    private boolean isPhotoClickedTEMPORARY;

    public AccommodationMultimediaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationMultimediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationMultimediaFragment newInstance(String param1, String param2) {
        AccommodationMultimediaFragment fragment = new AccommodationMultimediaFragment();
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
        binding = FragmentAccommodationMultimediaBinding.inflate(getLayoutInflater());

        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                    .get(AccommodationFormViewModel.class);

        binding.btnTakePhoto.setOnClickListener( v -> manageBtnTakePhoto() );

        return binding.getRoot();
    }

    private void manageBtnTakePhoto() {
        //TODO
        this.isPhotoClickedTEMPORARY = true;
        ToastUtils.showShortInformationMessage(this.getContext(), "Ahora da clic en Siguiente");
    }

    @Override
    public void onResume() {
        super.onResume();
        customActivityParent();
        temporaryPhotos = null;
        temporaryVideo = null;
        isPhotoClickedTEMPORARY = false;

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                ValidateAccommodationMultimediaSelected();
            }
        });
    }

    public void customActivityParent() {
        if (getActivity() != null) {
            Button btnNext = getActivity().findViewById(R.id.btn_next);
            String messageButton = "Siguiente";

            if (!btnNext.getText().toString().equals(messageButton)){
                String styledText = getString(R.string.accommodation_publishing_message);
                CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);

                TextView txvAccommodationPublishingMessage = getActivity().findViewById(R.id.txv_accommodation_publishing_message);

                txvAccommodationPublishingMessage.setText(styledTextSpanned);
                btnNext.setText(messageButton);
            }

        }
    }

    private void ValidateAccommodationMultimediaSelected() {
        if (isPhotoClickedTEMPORARY) {
            //TODO:
            accommodationFormViewModel.selectPhoto();
            accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "**Da Clic en TOMAR FOTO**" );
        }
    }
}