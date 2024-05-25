package com.sdi.hostedin.feature.host.accommodations.edition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentEditAccommodationBinding;
import com.sdi.hostedin.feature.guest.explore.accommodationdetails.AccommodationDetailsViewModel;
import com.sdi.hostedin.grpc.GrpcAccommodationMultimedia;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EditAccommodationFragment extends Fragment {

    private FragmentEditAccommodationBinding binding;
    private static Accommodation accommodation;
    private AccommodationDetailsViewModel accommodationDetailsViewModel;
    private EditAccommodationViewModel editAccommodationViewModel;
    private GrpcAccommodationMultimedia grpcClient;

    public EditAccommodationFragment() {

    }

    public static EditAccommodationFragment newInstance(Accommodation accommodation) {
        EditAccommodationFragment fragment = new EditAccommodationFragment();
        Bundle args = new Bundle();
        EditAccommodationFragment.accommodation = accommodation;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accommodationDetailsViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication())).get(AccommodationDetailsViewModel.class);
        editAccommodationViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication())).get(EditAccommodationViewModel.class);
        accommodationDetailsViewModel.getMultimediasListMutableLiveData().observe(this, multimedia -> {
            loadMultimedia(multimedia);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = com.sdi.hostedin.databinding.FragmentEditAccommodationBinding.inflate(inflater, container, false);
        setAccommodatioInfo();
        configureSections();
        manageProgressBarCircle();
        accommodationDetailsViewModel.loadAccommodationMultimedia(accommodation.getId());

        binding.btnGoAhead.setOnClickListener( v -> binding.vflpAccommodationMultimedia.showNext() );
        binding.btnGoBack.setOnClickListener( v -> binding.vflpAccommodationMultimedia.showPrevious() );

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        accommodationDetailsViewModel.loadAccommodationMultimedia(accommodation.getId());
    }


    private void setAccommodatioInfo(){
        binding.txvTitle.setText(accommodation.getTitle());
        binding.txvPrice.setText("$ " + String.valueOf(accommodation.getNightPrice()) + " MXN");
        binding.txvDescription.setText(accommodation.getDescription());
        String services = "Services: ";
        for (String service: accommodation.getAccommodationServices()) {
            services.concat(service+"\n");
        }
        binding.txvServices.setText(services);
        binding.vflpAccommodationMultimedia.setBackgroundColor(Color.LTGRAY);
    }

    private void configureSections(){
        binding.inclAccommodationType.imageView.setBackgroundResource(R.drawable.accommodation_icon);
        binding.inclAccommodationType.txvGenericText.setText("Accommodation type");
        binding.inclAccommodationType.bttGenericButton.setOnClickListener(this::editAccommodationType);

        binding.inclUbication.imageView.setBackgroundResource(R.drawable.map_icon);
        binding.inclUbication.txvGenericText.setText("Ubication");
        binding.inclUbication.bttGenericButton.setOnClickListener(this::editAccommodationUbication);

        binding.inclNumberGuests.imageView.setBackgroundResource(R.drawable.bed_icon);
        binding.inclNumberGuests.txvGenericText.setText("Rooms and guests...");
        binding.inclNumberGuests.bttGenericButton.setOnClickListener(this::editAccommodationNumberGuest);

        binding.inclServices.imageView.setBackgroundResource(R.drawable.service_icon);
        binding.inclServices.txvGenericText.setText("Services");
        binding.inclServices.bttGenericButton.setOnClickListener(this::editAccommodationServices);

        binding.inclMedia.imageView.setBackgroundResource(R.drawable.camera_icon);
        binding.inclMedia.txvGenericText.setText("Pictures and video");
        binding.inclMedia.bttGenericButton.setOnClickListener(this::editAccommodationMedia);

        binding.inclPublicationInfo.imageView.setBackgroundResource(R.drawable.edit_acc_icon);
        binding.inclPublicationInfo.txvGenericText.setText("Title, description and price");
        binding.inclPublicationInfo.bttGenericButton.setOnClickListener(this::editAccommodationInfo);

        binding.bttDeleteAccommodation.setOnClickListener(this::confirmationMessage);
    }

    private void confirmationMessage(View view){
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getContext());
        confirmationDialog.setTitle("Watch Out!!");
        confirmationDialog.setMessage("Are you sure you want to delete this accommodation?, this action cannot be undo");
        confirmationDialog.setPositiveButton("Yes, delete", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 deleteAccommodation();
                 dialog.dismiss();
            }
        });
        confirmationDialog.setNegativeButton("No, cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = confirmationDialog.create();
        dialog.show();
    }
    private void deleteAccommodation() {
        editAccommodationViewModel.deleteAccommodation(accommodation.getId());
    }

    private void editAccommodationInfo(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_DESCRIPTIONS);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationMedia(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_MULTIMEDIA);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationServices(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_SERVICES);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationNumberGuest(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_NUMBERS_OF);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationUbication(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_MAP_UBICATION);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationType(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_TYPE);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void loadFragmentEdition(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fgcv_main_edit_accomm_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void loadMultimedia(List<byte[]> multimedia) {
        for (int i = 0; i < 4; i++) {
            insertMultimediaIntoViewFlipper(i, multimedia.get(i));
        }
    }

    private void insertMultimediaIntoViewFlipper(int index, byte[] multimediaBytes) {
        switch (index) {
            case 0:
                ImageView imageView1 = binding.imvFirstImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView1);
                binding.vflpAccommodationMultimedia.setDisplayedChild(0);
                break;
            case 1:
                ImageView imageView2 = binding.imvSecondImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView2);
                break;
            case 2:
                ImageView imageView3 = binding.imvThirdImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView3);
                break;
            case 3:
                loadVideo(multimediaBytes);
                break;
        }
    }
    private void loadVideo(byte[] videoBytes) {
        try {
            File tempFile = File.createTempFile("video", ".mp4", getActivity().getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(videoBytes);
            fos.close();

            VideoView videoView = binding.vdvFourthVideo;
            videoView.setVideoURI(Uri.parse(tempFile.getAbsolutePath()));
            videoView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void manageProgressBarCircle() {
        accommodationDetailsViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbEditedAccommodation.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbEditedAccommodation.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.pgbEditedAccommodation.setVisibility(View.GONE);
                    Log.d("PRUEBA", "error: " + status.getMessage());
                    ToastUtils.showShortInformationMessage(getContext(), status.getMessage());
            }
        });
        editAccommodationViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbEditedAccommodation.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbEditedAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(getContext(), status.getMessage());
                    getActivity().finish();
                    break;
                case ERROR:
                    binding.pgbEditedAccommodation.setVisibility(View.GONE);
                    Log.d("PRUEBA", "error: " + status.getMessage());
                    ToastUtils.showShortInformationMessage(getContext(), status.getMessage());
            }
        });
    }



}