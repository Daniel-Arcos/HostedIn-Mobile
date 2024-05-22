package com.sdi.hostedin.feature.guest.bookings.accommodationbooking;

import static com.sdi.hostedin.feature.guest.explore.accommodationdetails.AccommodationDetailsActivity.ACCOMMODATION_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityAccommodationBookingBinding;
import com.sdi.hostedin.databinding.ItemHostDetailsBinding;
import com.sdi.hostedin.enums.BookingSatuses;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationFormActivity;
import com.sdi.hostedin.feature.user.EditProfileViewModel;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AccommodationBookingActivity extends AppCompatActivity {

    private static final String MONEY_SYMBOL = "$";
    private ActivityAccommodationBookingBinding binding;
    private RxDataStore<Preferences> dataStoreRX;
    private MaterialDatePicker<Pair<Long, Long>> dateRangePicker;
    private AccommodationBookingViewModel accommodationBookingViewModel;
    private ItemHostDetailsBinding inclHostData;
    private int limitGuestsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccommodationBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setAccommodationData(extras.getParcelable(ACCOMMODATION_KEY));

        limitGuestsNumber = binding.getAccommodationData().getGuestsNumber();
        this.inclHostData = binding.inclHostData;

        accommodationBookingViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(AccommodationBookingViewModel.class);

        loadData();
        setViewModelObservers();
        showAccommodationBookingMessage();
        manageProgressBarCircle();
        configureDatePicker();
        configureButtons();
    }

    private void loadData() {
        loadHostData();
        loadLimitGuestsNumber();
        loadNightPrice();
        loadAccommodationType();
        loadAccommodationImage();
    }

    private void loadLimitGuestsNumber() {
        String limitGuests = String.format("Solo se permiten\n %d huéspedes", this.limitGuestsNumber);
        binding.txvGuestsNumberLimit.setText(limitGuests);
    }

    private void loadNightPrice() {
        String price = String.valueOf(binding.getAccommodationData().getNightPrice());
        String detailPrice = " MXN por noche";
        String nightPrice = MONEY_SYMBOL + price + detailPrice;

        binding.txvNightPrice.setText(nightPrice);
    }

    private void loadAccommodationType() {
        // TODO:
    }

    private void loadAccommodationImage() {
        // TODO:
    }

    private void setViewModelObservers() {
        accommodationBookingViewModel.getBeginningDate().observe(this, date -> updateDate(binding.btnBeginningDate, date));

        accommodationBookingViewModel.getEndingDate().observe(this, date -> updateDate(binding.btnEndingDate, date));

        accommodationBookingViewModel.getRealGuestsNumber().observe(this, guestsNumber -> updateGuestsNumber(guestsNumber) );

        accommodationBookingViewModel.getStayDays().observe(this, stayDays -> updateStayDays() );

        accommodationBookingViewModel.getTotalCost().observe(this, cost -> updateCost() );
    }

    private void manageProgressBarCircle() {
        accommodationBookingViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbCreateBooking.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbCreateBooking.setVisibility(View.GONE);
                    manageSuccessCreation();
                    break;
                case ERROR:
                    binding.pgbCreateBooking.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });
    }

    private void manageSuccessCreation() {
        ToastUtils.showShortInformationMessage(this, "Reservación registrada con éxito");
        finish();
    }

    private void updateDate(Button btnDate, String date) {
        btnDate.setText(date);

        if (date != null && accommodationBookingViewModel.getBeginningDate().getValue() != null) {
            Date lastDay = DateFormatterUtils.parseStringToDate(accommodationBookingViewModel.getBeginningDate().getValue());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastDay);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date dayBefore = calendar.getTime();

            String dayBeforeString = DateFormatterUtils.formatNormalDate(dayBefore);

            String cancellationPolicy = String.format("La cancelación de la reservación puede hacerse hasta máximo un día antes de la fecha de inicio. (Tienes hasta el %s antes de las 23:59 hrs)",
                    dayBeforeString);

            binding.txvCancellationPolicy.setText(cancellationPolicy);
        }
    }

    private void updateGuestsNumber(int guestsNumber){
        binding.etxGuestsNumber.setText(String.valueOf(guestsNumber));
    }

    private void configureButtons() {
        binding.btnIncrementGuests.setOnClickListener( v -> {
            accommodationBookingViewModel.incrementRealGuestsNumber(limitGuestsNumber);
        });

        binding.btnDecrementGuests.setOnClickListener( v -> {
            accommodationBookingViewModel.decrementRealGuestsNumber();
        });

        binding.btnBeginningDate.setOnClickListener( v ->
        {
            dateRangePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
            v.setEnabled(false);
            binding.btnEndingDate.setEnabled(false);
        });

        binding.btnEndingDate.setOnClickListener( v ->
        {
            dateRangePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
            v.setEnabled(false);
            binding.btnBeginningDate.setEnabled(false);
        });

        binding.btnSaveBooking.setOnClickListener( v -> saveBooking() );
    }

    private void configureDatePicker() {
        String titleCalendar = "Selecciona un rango de fechas";
        long currentTimeMillis = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(currentTimeMillis);

        // TODO: Recover the bookings
        List<Booking> reservedDates = new ArrayList<>();
        ///

        constraintsBuilder.setValidator(new DateValidatorBookedDates(reservedDates));

        dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText(titleCalendar)
                        .setSelection(
                                Pair.create(
                                        currentTimeMillis,
                                        MaterialDatePicker.todayInUtcMilliseconds()
                                )
                        )
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

        dateRangePicker.addOnDismissListener( v -> getSelectedDates() );
    }

    public void getSelectedDates() {
        Pair<Long, Long> selectedDates = dateRangePicker.getSelection();

        if (selectedDates != null) {
            long firstDate = selectedDates.first;
            long secondDate = selectedDates.second;

            //TODO: Recover the bookings
            List<Booking> reservedDates = new ArrayList<>();
            ///

            DateValidatorBookedDates dateValidator = new DateValidatorBookedDates(
                    DateFormatterUtils.parseLongToDate(firstDate),
                    DateFormatterUtils.parseLongToDate(secondDate),
                    reservedDates
            );

            boolean isValidDateBooking = dateValidator.areBookingDatesValid(DateFormatterUtils.parseLongToDate(firstDate), DateFormatterUtils.parseLongToDate(secondDate));

            if (isValidDateBooking) {
                accommodationBookingViewModel.updateBeginningDate(firstDate);
                accommodationBookingViewModel.updateEndingDate(secondDate);
                accommodationBookingViewModel.calculateStayDays();
                accommodationBookingViewModel.calculateCost(binding.getAccommodationData().getNightPrice());
            } else {
                ToastUtils.showShortInformationMessage(this, "No puedes reservar entre fechas ocupadas");
            }

            binding.btnBeginningDate.setEnabled(true);
            binding.btnEndingDate.setEnabled(true);
        }
    }

    private void showAccommodationBookingMessage() {
        String styledText = getString(R.string.accommodation_booking_message);
        CharSequence styledTextSpanned = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY);
        binding.txvAccommodationBookingMessage.setText(styledTextSpanned);
    }

    private void loadHostData() {
        inclHostData.txvHostName.setText(binding.getAccommodationData().getUser().getFullName());
        inclHostData.txvHostPhoneNumber.setText(binding.getAccommodationData().getUser().getPhoneNumber());
    }

    private void updateStayDays() {
        int days = accommodationBookingViewModel.getStayDays().getValue();
        String stayDays = String.valueOf(days);
        String pricePerNight = MONEY_SYMBOL + binding.getAccommodationData().getNightPrice()+ " x " + stayDays + " noches";

        binding.txvPricePerNight.setText(pricePerNight);
    }

    private void updateCost() {
        double subtotal = accommodationBookingViewModel.getTotalCost().getValue();
        double ivaPercentage = .16;
        double iva = subtotal * ivaPercentage;
        double total = subtotal + iva;

        binding.txvSubtotal.setText(MONEY_SYMBOL + String.valueOf(subtotal));
        binding.txvIva.setText(MONEY_SYMBOL + String.valueOf(iva));
        binding.txvTotal.setText(MONEY_SYMBOL + String.valueOf(total));
    }

    private void saveBooking() {
        if (isBookingValid()) {
            Booking newBooking = createBooking();
            accommodationBookingViewModel.createBooking(newBooking);
        }
    }

    private boolean isBookingValid() {
        boolean isBookingValid = true;

        if (!isGuestsNumberValid()) {
            isBookingValid = false;
        } else if (!areDatesValid()) {
            isBookingValid = false;
        }

        return isBookingValid;
    }

    private boolean isGuestsNumberValid() {
        boolean isGuestsNumberValid = false;
        int guestsNumber = accommodationBookingViewModel.getRealGuestsNumber().getValue();

        if (guestsNumber > 0 && guestsNumber <= binding.getAccommodationData().getGuestsNumber()) {
            isGuestsNumberValid = true;
        } else {
            ToastUtils.showShortInformationMessage(this, "Cantidad no válida de huéspedes");
        }

        return isGuestsNumberValid;
    }

    private boolean areDatesValid() {
        boolean areDatesValid = false;
        String beginDate = accommodationBookingViewModel.getBeginningDate().getValue();
        String endDate = accommodationBookingViewModel.getEndingDate().getValue();

        if (beginDate != null && endDate != null) {
            Date beginningDate = DateFormatterUtils.parseStringToDate(beginDate);
            Date endingDate = DateFormatterUtils.parseStringToDate(endDate);

            if (endingDate.compareTo(beginningDate) >= 0) {
                areDatesValid = true;
            } else {
                ToastUtils.showShortInformationMessage(this, "Fechas de reservación no válidas");
            }
        } else {
            ToastUtils.showShortInformationMessage(this, "Selecciona las fechas de reservación");
        }

        return areDatesValid;
    }

    private Booking createBooking() {
        Booking newBooking = new Booking();

        String beginningDateMongo = DateFormatterUtils.parseDateForMongoDB(String.valueOf(binding.btnBeginningDate.getText()).trim());
        String endingDateMongo = DateFormatterUtils.parseDateForMongoDB(String.valueOf(binding.btnEndingDate.getText()).trim());

        newBooking.setAccommodationId(binding.getAccommodationData().getId());
        if (beginningDateMongo != null) {
            newBooking.setBeginningDate(beginningDateMongo);
        }
        if (endingDateMongo != null) {
            newBooking.setEndingDate(endingDateMongo);
        }

        newBooking.setNumberOfGuests(accommodationBookingViewModel.getRealGuestsNumber().getValue());
        newBooking.setTotalCost(accommodationBookingViewModel.getTotalCost().getValue());
        newBooking.setBookingStatus(BookingSatuses.CURRENT.getDescription());
        newBooking.setGuestUser(assignGuestUserId());
        newBooking.setHostUser(assignHostUserId());

        return newBooking;
    }

    private User assignGuestUserId() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getApplication(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(new AccommodationFormActivity(), dataStoreRX);
        String userId = dataStoreHelper.getStringValue("USER_ID");
        User user = new User();
        user.setId(userId);

        return user;
    }

    private User assignHostUserId() {
        User user = new User();
        user.setId(binding.getAccommodationData().getUser().getId());

        return user;
    }
}