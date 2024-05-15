package com.sdi.hostedin.feature.user;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.ProfilePhoto;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityEditProfileBinding;
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

    private static final String DEFAULT_TYPE_PHOTO = "Buffer";
    private static final String ON_SUCCESS_EDIT_MESSAGE = "edited account";
    private ActivityEditProfileBinding binding;
    private EditProfileViewModel editProfileViewModel;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri selectedImageUri;
    private byte[] profilePhotoLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editProfileViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(EditProfileViewModel.class);

        editProfileViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.loadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.loadingWheel.setVisibility(View.GONE);
                    if (status.getMessage().equals(ON_SUCCESS_EDIT_MESSAGE)) {
                        manageSuccessUpdate();
                    }
                    break;
                case ERROR:
                    binding.loadingWheel.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });

        configureBottomSheet();
        loadUserData();
        DatePickerConfigurator.configureDatePicker(binding.etxBirthdate);
        configureButtons();
    }

    private void configureBottomSheet() {
        BottomSheetBehavior<RelativeLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.btmshEditUserData);
        bottomSheetBehavior.setDraggable(false);
        binding.btnCancelChangeUserData.setOnClickListener( v -> finish() );
    }

    private void loadUserData() {
        // TODO: Get the ID from Singleton
        String userId = "6643c7274ce07d500e558960";
        editProfileViewModel.getUserById(userId);

        editProfileViewModel.getUserMutableLiveData().observe(this, user -> {
            if (user != null) {
                String birthdate = DateFormatterUtils.parseMongoDateToLocal(user.getBirthDate());
                binding.txvFullName.setText(user.getFullName());
                binding.etxEmail.setText(user.getEmail());
                binding.etxFullName.setText(user.getFullName());
                if (birthdate != null) {
                    binding.etxBirthdate.setText(birthdate);
                }
                binding.etxPhoneNumber.setText(user.getPhoneNumber());
                binding.etxResidence.setText(user.getResidence());
                binding.etxOccupation.setText(user.getOccupation());

                loadProfilePhoto(user);
            }
        });
    }

    private void loadProfilePhoto(User user) {
        if (user.getProfilePhoto() != null) {
            byte[] imageData = user.getProfilePhoto().getData();
            Bitmap profilePhoto = ImageUtils.bytesToBitmap(imageData);

            if (profilePhoto != null) {
                binding.imvPhotoProfile.setImageBitmap(profilePhoto);
            } else {
                binding.imvPhotoProfile.setImageResource(R.drawable.profile_icon);
            }

            this.profilePhotoLoaded = imageData;
        } else {
            binding.imvPhotoProfile.setImageResource(R.drawable.profile_icon);
        }
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
            binding.imvPhotoProfile.setImageURI(uriPhoto);
        }
    }

    private void saveUserChanges() {
        if (isUserDataValid()) {
            User user = createUser();
            editProfileViewModel.editProfile(user);
        }
    }

    private User createUser() {
        User editedUser = new User();
        // TODO: Get ID from SINGLETON
        editedUser.setId("6643c7274ce07d500e558960");

        String birthdateMongoDb = DateFormatterUtils.parseDateForMongoDB(String.valueOf(binding.etxBirthdate.getText()).trim());
        editedUser.setEmail(String.valueOf(binding.etxEmail.getText()).trim());
        editedUser.setFullName(String.valueOf(binding.etxFullName.getText()).trim());
        if (birthdateMongoDb != null) {
            editedUser.setBirthDate(birthdateMongoDb);
        }
        editedUser.setPhoneNumber(String.valueOf(binding.etxPhoneNumber.getText()).trim());
        editedUser.setResidence(String.valueOf(binding.etxResidence.getText()).trim());
        editedUser.setOccupation(String.valueOf(binding.etxOccupation.getText()).trim());
        editedUser.setProfilePhoto(assignProfilePhotoOnCreate());

        return editedUser;
    }

    private ProfilePhoto assignProfilePhotoOnCreate() {
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setType(DEFAULT_TYPE_PHOTO);

        if (selectedImageUri != null) {
            byte[] profilePhotoData = ImageUtils.uriToBytes(this, selectedImageUri);
            if (profilePhotoData != null) {
                profilePhoto.setData(profilePhotoData);
            } else {
                ToastUtils.showShortInformationMessage(this, "Error procesando la imagen");
            }
        } else {
            profilePhoto.setData(this.profilePhotoLoaded);
        }

        return profilePhoto;
    }

    private void manageSuccessUpdate() {
        ToastUtils.showShortInformationMessage(this, "Información actualizada con éxito");
        finish();
    }

    private boolean isUserDataValid() {
        boolean isUserDataValid = true;

        if (!isPhotoValid()) {
            isUserDataValid = false;
            ToastUtils.showShortInformationMessage(this, "Foto no válida");
        } else if (!isFullNameValid()) {
            isUserDataValid = false;
        } else if (!isBirthdateValid()) {
            isUserDataValid = false;
            ToastUtils.showShortInformationMessage(this, "Debes ser mayor de edad (18)");
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
        // TODO
        boolean isValid = true;

        return isValid;
    }

    private boolean isFullNameValid() {
        boolean isFullNameValid = true;
        String fullName = String.valueOf(binding.etxFullName.getText()).trim();

        if (fullName.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, "Debes ingresar tu nombre completo");
            isFullNameValid = false;
        } else if (!DataValidator.isFullNameValid(fullName)) {
            ToastUtils.showShortInformationMessage(this, "Nombre completo no válido");
            isFullNameValid = false;
        }

        return isFullNameValid;
    }

    private boolean isBirthdateValid() {
        boolean isBirthdateValid = true;
        String birthdateStr = String.valueOf(binding.etxBirthdate.getText()).trim();

        if (birthdateStr.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, "Debes ingresar tu fecha de nacimiento");
            isBirthdateValid = false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
                LocalDate currentDate = LocalDate.now();
                LocalDate minAgeDate = currentDate.minusYears(18);
                isBirthdateValid = !birthdate.isAfter(minAgeDate);
            } catch (DateTimeParseException e) {
                ToastUtils.showShortInformationMessage(this, "El formato de la fecha no es válido");
                isBirthdateValid = false;
            }
        }

        return isBirthdateValid;
    }


    private boolean isPhoneNumberValid() {
        boolean isPhoneNumberValid = true;
        String phoneNumber = String.valueOf(binding.etxPhoneNumber.getText()).trim();

        if (phoneNumber.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, "Debes ingresar tu número de teléfono");
            isPhoneNumberValid = false;
        } else if (!DataValidator.isPhoneNumberValid(phoneNumber)) {
            ToastUtils.showShortInformationMessage(this, "Número de teléfono no válido");
            isPhoneNumberValid = false;
        }

        return  isPhoneNumberValid;
    }

    private boolean isOccupationValid() {
        boolean isOccupationValid = true;
        String occupation = String.valueOf(binding.etxOccupation.getText()).trim();

        if (!occupation.isEmpty() && !DataValidator.isOccupationValid(occupation)) {
            ToastUtils.showShortInformationMessage(this, "Ocupación no válida");
            isOccupationValid = false;
        }

        return isOccupationValid;
    }

    private boolean isResidenceValid() {
        boolean isResidenceValid = true;
        String residence = String.valueOf(binding.etxResidence.getText()).trim();

        if (!residence.isEmpty() && !DataValidator.isResidenceValid(residence)) {
            ToastUtils.showShortInformationMessage(this, "Estancia no válida");
            isResidenceValid = false;
        }

        return isResidenceValid;
    }
}