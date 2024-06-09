package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentAccommodationMultimediaBinding;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.io.IOException;
import java.util.List;

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
    public static final int LOCAL_FRAGMENT_NUMBER = 5;
    private static final String DEFAULT_TYPE_PHOTO = "Buffer";
    private static final int NUMBER_OF_IMAGES = 3;
    private static final int MILLISECONDS_TIME_VIDEO = 60000;
    private static final int MAX_MB_SIZE_VIDEO = 130;
    private FragmentAccommodationMultimediaBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri[] selectedImagesUri = new Uri[NUMBER_OF_IMAGES];
    private int currentImageNumber = 0;
    private Uri selectedVideo;
    boolean isNextClicked = false;

    private static Accommodation accommodationToEdit ;
    private static boolean isEdition;

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
    public static AccommodationMultimediaFragment newInstance(Accommodation param1, boolean param2) {
        AccommodationMultimediaFragment fragment = new AccommodationMultimediaFragment();
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

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                if (currentImageNumber < 3) {
                    selectedImagesUri[currentImageNumber] = uri;
                    accommodationFormViewModel.getImagesUri().getValue().add(uri);   /////////
                    ImageView imvAccommodationPhoto = getImageViewByNumber(currentImageNumber);
                    if (imvAccommodationPhoto != null) {
                        imvAccommodationPhoto.setImageURI(uri);
                    }

                } else {
                    if (isVideoValid(this.getContext(), uri)) {
                        selectedVideo = uri;
                        accommodationFormViewModel.getVideoUri().setValue(uri);
                        binding.imvFourthVideo.setImageResource(0);
                        binding.vdvFourthVideo.setVideoURI(uri);
                        binding.vdvFourthVideo.start();
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccommodationMultimediaBinding.inflate(getLayoutInflater());

        selectedImagesUri = new Uri[NUMBER_OF_IMAGES];
        selectedVideo = null;
        isPhotoClickedTEMPORARY = false;

        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                    .get(AccommodationFormViewModel.class);

        configureListeners();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        customActivityParent();

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                validateAccommodationMultimediaSelected();
            }
        });

        List<Uri> imageUri = accommodationFormViewModel.getImagesUri().getValue();
        for (int i = 0; i < imageUri.size() ; i++) {
            if (imageUri != null) {
                selectedImagesUri[i] = imageUri.get(i);
                ImageView imvAccommodationPhoto = getImageViewByNumber(i);
                if (imvAccommodationPhoto != null) {
                    imvAccommodationPhoto.setImageURI(imageUri.get(i));
                }
            }
        }

        Uri videoUri = accommodationFormViewModel.getVideoUri().getValue();
        if (videoUri != null) {
            selectedVideo = videoUri;
            binding.imvFourthVideo.setImageResource(0);
            binding.vdvFourthVideo.setVideoURI(videoUri);
            binding.vdvFourthVideo.start();
        }
    }

    private void configureListeners() {
        binding.rtlyMainImage.setOnClickListener( v -> {
            currentImageNumber = 0;
            openImageChooser();
        });

        binding.rtlySecondImage.setOnClickListener( v -> {
            currentImageNumber = 1;
            openImageChooser();
        });

        binding.rtlyThirdImage.setOnClickListener( v -> {
            currentImageNumber = 2;
            openImageChooser();
        });

        binding.rtlyFourthVideo.setOnClickListener( v -> {
            currentImageNumber = 4;
            openVideoChooser();
        });
    }

    private void openImageChooser() {
        this.pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void openVideoChooser() {
        this.pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                .build());
    }

    private ImageView getImageViewByNumber(int imageNumber) {
        switch (imageNumber) {
            case 0:
                binding.imvMainImage.setPadding(0,0,0,0);
                return binding.imvMainImage;
            case 1:
                binding.imvSecondImage.setPadding(0,0,0,0);
                return binding.imvSecondImage;
            case 2:
                binding.imvThirdImage.setPadding(0,0,0,0);
                return binding.imvThirdImage;
            default:
                return null;
        }
    }

    private boolean isVideoValid(Context context, Uri videoUri) {
        boolean isVideoValid = true;

        if (!isVideoDurationValid(context, videoUri)) {
            isVideoValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "El video debe durar menos de 1 minuto");
        } else if (!isVideoSizeValid(context, videoUri)) {
            isVideoValid = false;
            ToastUtils.showShortInformationMessage(this.getContext(), "El video debe pesar menos de 10 MB");
        }

        return isVideoValid;
    }

    private boolean isVideoDurationValid(Context context, Uri videoUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, videoUri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long duration = 0;
        if (time != null) {
            duration = Long.parseLong(time);
        }
        try {
            retriever.release();
            retriever.close();
        } catch (IOException e) {
            Log.e("DEBUG", "isVideoDurationValid: " + e.getMessage());;
        }

        return duration <= MILLISECONDS_TIME_VIDEO;
    }

    private boolean isVideoSizeValid(Context context, Uri videoUri) {

        Cursor cursor = context.getContentResolver().query(videoUri, null, null, null, null);
        if (cursor != null) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            long size = cursor.getLong(sizeIndex);
            cursor.close();
            return size <= MAX_MB_SIZE_VIDEO * 1024 * 1024;
        }
        return false;
    }

    public void customActivityParent() {
        if (getActivity() != null) {
            Button btnNext = getActivity().findViewById(R.id.btn_next);
            String messageButton = "Siguiente";

            if (!isEdition && !btnNext.getText().toString().equals(messageButton)){
                String styledText = getString(R.string.accommodation_publishing_header);
                CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);

                TextView txvAccommodationPublishingMessage = getActivity().findViewById(R.id.txv_accommodation_publishing_message);

                txvAccommodationPublishingMessage.setText(styledTextSpanned);
                btnNext.setText(messageButton);
            }

        }
    }

    private void validateAccommodationMultimediaSelected() {
        if (areSelectedPhotosValid() && isSelectedVideoValid()) {
            if (isNextClicked) {
                accommodationFormViewModel.selectMultimedia(uriPhotoToBytes(), uriVideoToBytes());
                accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
            } else {
                isNextClicked = true;
            }
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "Selecciona 3 fotos y 1 video de tu alojamiento");
        }
    }

    private byte[][] uriPhotoToBytes() {
        byte[][] photoBytes = new byte[selectedImagesUri.length][];

        for (int i = 0; i < selectedImagesUri.length; i++) {
            if (selectedImagesUri[i] != null) {
                byte[] bytes = ImageUtils.uriToBytes(this.getContext(), selectedImagesUri[i]);
                if (bytes != null) {
                    photoBytes[i] = bytes;
                } else {
                    ToastUtils.showShortInformationMessage(this.getContext(), "Error procesando la imagen");
                }
            }
        }

        return photoBytes;
    }

    private byte[] uriVideoToBytes() {
        byte[] videoBytes = null;

        if (selectedVideo != null) {
            byte[] bytes = ImageUtils.uriToBytes(this.getContext(), selectedVideo);
            if (bytes != null) {
                videoBytes = bytes;
            } else {
                ToastUtils.showShortInformationMessage(this.getContext(), "Error procesando la imagen");
            }
        }

        return videoBytes;
    }

    private boolean areSelectedPhotosValid() {
        boolean arePhotosSelectedValid = false;

        if (selectedImagesUri != null
                && selectedImagesUri[0] != null
                && selectedImagesUri[1] != null
                && selectedImagesUri[2] != null) {
            arePhotosSelectedValid = true;
        }

        return arePhotosSelectedValid;
    }

    private boolean isSelectedVideoValid() {
        boolean isSelectedVideoValid = false;

        if (selectedVideo != null && isVideoValid(this.getContext(), selectedVideo)) {
            isSelectedVideoValid = true;
        }

        return isSelectedVideoValid;
    }
}