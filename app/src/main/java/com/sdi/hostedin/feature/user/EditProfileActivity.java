package com.sdi.hostedin.feature.user;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityEditProfileBinding;
import com.sdi.hostedin.grpc.GrpcProfilePhoto;
import com.sdi.hostedin.utils.DataValidator;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.DatePickerConfigurator;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditProfileActivity extends AppCompatActivity {

    public static final String USER_KEY = "user_key";
    private static final String DEFAULT_TYPE_PHOTO = "Buffer";
    private static final int MAX_MB_SIZE_VIDEO = 1;
    private ActivityEditProfileBinding binding;
    private EditProfileViewModel editProfileViewModel;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri selectedImageUri;
    private byte[] profilePhotoLoaded;
    private GrpcProfilePhoto grpcClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setUserData(extras.getParcelable(USER_KEY));

        editProfileViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(EditProfileViewModel.class);

        manageProgressBarCircle();
        configureBottomSheet();
        loadUserData();
        DatePickerConfigurator.configureDatePicker(binding.etxBirthdate);
        configureButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (grpcClient != null) {
                grpcClient.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void manageProgressBarCircle() {
        editProfileViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbEditProfile.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbEditProfile.setVisibility(View.GONE);
                    if (status.getMessage().equals(EditProfileViewModel.ON_SUCCESS_EDIT_MESSAGE)) {
                        manageSuccessUpdate();
                    }
                    break;
                case ERROR:
                    binding.pgbEditProfile.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });
    }

    private void configureBottomSheet() {
        //BottomSheetBehavior<RelativeLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.btmshEditUserData);
        //bottomSheetBehavior.setDraggable(false);
        binding.btnCancelChangeUserData.setOnClickListener( v -> finish() );
    }

    private void loadUserData() {
        String birthdate = DateFormatterUtils.parseMongoDateToLocal(binding.getUserData().getBirthDate());
        if (birthdate != null) {
            binding.etxBirthdate.setText(birthdate);
        }

        this.profilePhotoLoaded = ImageUtils.loadProfilePhoto(binding.getUserData(), binding.imvProfilePhoto);
    }

    private void configureButtons() {
        configureActivityResultLauncher();
        binding.btnEditPhotoProfile.setOnClickListener( v -> openImageChooser() );
        binding.btnSaveUserChanges.setOnClickListener( v -> saveUserChanges() );
    }

    private void configureActivityResultLauncher() {
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> setPhotoProfile(uri) );

        this.pickMedia = pickMedia;
    }

    private void openImageChooser() {
        this.pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void setPhotoProfile(Uri uriPhoto) {
        if (uriPhoto != null) {
            selectedImageUri = uriPhoto;
            binding.imvProfilePhoto.setImageURI(uriPhoto);
        }
    }

    private void saveUserChanges() {
        if (isUserDataValid()) {
            User user = createUser();
            editProfileViewModel.editProfile(user);
            assignProfilePhoto();
        }
    }

    private User createUser() {
        User editedUser = new User();
        String userId = binding.getUserData().getId();
        String birthdateMongoDb = DateFormatterUtils.parseDateForMongoDB(String.valueOf(binding.etxBirthdate.getText()).trim());

        editedUser.setId(userId);
        editedUser.setEmail(String.valueOf(binding.etxEmail.getText()).trim());
        editedUser.setFullName(String.valueOf(binding.etxFullName.getText()).trim());
        if (birthdateMongoDb != null) {
            editedUser.setBirthDate(birthdateMongoDb);
        }
        editedUser.setPhoneNumber(String.valueOf(binding.etxPhoneNumber.getText()).trim());
        editedUser.setResidence(String.valueOf(binding.etxResidence.getText()).trim());
        editedUser.setOccupation(String.valueOf(binding.etxOccupation.getText()).trim());

        return editedUser;
    }

    private void assignProfilePhoto() {
        if (selectedImageUri != null) {
            byte[] profilePhotoData = ImageUtils.uriToBytes(this, selectedImageUri);
            if (profilePhotoData != null) {
                grpcClient = new GrpcProfilePhoto();
                grpcClient.uploadProfilePhoto(binding.getUserData().getId(), profilePhotoData);
            } else {
                ToastUtils.showShortInformationMessage(this, getString(R.string.error_processing_image));
            }
        }
    }

    private void manageSuccessUpdate() {
        binding.txvFullName.setText(binding.etxFullName.getText());
        ToastUtils.showShortInformationMessage(this, getString(R.string.iInformation_updated_successfully));
        finish();
    }

    private boolean isUserDataValid() {
        boolean isUserDataValid = true;

        if (!isPhotoValid()) {
            isUserDataValid = false;
        } else if (!isFullNameValid()) {
            isUserDataValid = false;
        } else if (!isBirthdateValid()) {
            isUserDataValid = false;
            ToastUtils.showShortInformationMessage(this, getString(R.string.age_restriction));
        } else if (!isPhoneNumberValid()) {
            isUserDataValid = false;
        } else if (!isOccupationValid()) {
            isUserDataValid = false;
        } else if (!isResidenceValid()) {
            isUserDataValid = false;
        }

        return isUserDataValid;
    }

    private boolean isPhotoValid() {
        boolean isValid = true;
        if (selectedImageUri != null && !isPhotoSizeValid(selectedImageUri)) {
            isValid = false;
            ToastUtils.showShortInformationMessage(this, String.format(getString(R.string.profile_photo_size_restriction), MAX_MB_SIZE_VIDEO));
        }

        return isValid;
    }

    private boolean isFullNameValid() {
        boolean isFullNameValid = true;
        String fullName = String.valueOf(binding.etxFullName.getText()).trim();

        if (fullName.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.must_enter_full_name));
            isFullNameValid = false;
        } else if (!DataValidator.isFullNameValid(fullName)) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.invalid_full_name));
            isFullNameValid = false;
        }

        return isFullNameValid;
    }

    private boolean isBirthdateValid() {
        boolean isBirthdateValid = true;
        String birthdateStr = String.valueOf(binding.etxBirthdate.getText()).trim();

        if (birthdateStr.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.must_enter_birthdate));
            isBirthdateValid = false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
                LocalDate currentDate = LocalDate.now();
                LocalDate minAgeDate = currentDate.minusYears(18);
                isBirthdateValid = !birthdate.isAfter(minAgeDate);
            } catch (DateTimeParseException e) {
                ToastUtils.showShortInformationMessage(this, getString(R.string.invalid_date_format));
                isBirthdateValid = false;
            }
        }

        return isBirthdateValid;
    }


    private boolean isPhoneNumberValid() {
        boolean isPhoneNumberValid = true;
        String phoneNumber = String.valueOf(binding.etxPhoneNumber.getText()).trim();

        if (phoneNumber.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.must_enter_phone_number));
            isPhoneNumberValid = false;
        } else if (!DataValidator.isPhoneNumberValid(phoneNumber)) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.invalid_phone_number));
            isPhoneNumberValid = false;
        }

        return  isPhoneNumberValid;
    }

    private boolean isOccupationValid() {
        boolean isOccupationValid = true;
        String occupation = String.valueOf(binding.etxOccupation.getText()).trim();

        if (!occupation.isEmpty() && !DataValidator.isOccupationValid(occupation)) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.invalid_occupation));
            isOccupationValid = false;
        }

        return isOccupationValid;
    }

    private boolean isResidenceValid() {
        boolean isResidenceValid = true;
        String residence = String.valueOf(binding.etxResidence.getText()).trim();

        if (!residence.isEmpty() && !DataValidator.isResidenceValid(residence)) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.invalid_residence));
            isResidenceValid = false;
        }

        return isResidenceValid;
    }

    private boolean isPhotoSizeValid(Uri imageUri) {

        Cursor cursor = this.getContentResolver().query(imageUri, null, null, null, null);
        if (cursor != null) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            long size = cursor.getLong(sizeIndex);
            cursor.close();
            return size <= MAX_MB_SIZE_VIDEO * 1024 * 1024;
        }
        return false;
    }
}