package com.sdi.hostedin.feature.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.AccommodationGrpc;
import com.sdi.hostedin.data.model.EarningGrpc;
import com.sdi.hostedin.databinding.FragmentStatisticsBinding;
import com.sdi.hostedin.utils.MonthValueFormatter;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private StaticticsViewModel statisticsViewModel;
    RxDataStore<Preferences> dataStoreRX;
    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(getLayoutInflater());

        statisticsViewModel =
                new ViewModelProvider(requireActivity(), new ViewModelFactory(requireActivity().getApplication())).get(StaticticsViewModel.class);
        manageProgressBarOne();
        manageProgressBarTwo();
        statisticsViewModel.getMostBookedAccommodationsLiveData().observe(getViewLifecycleOwner(), this::configureMostBookedAccommodationsBar);
        statisticsViewModel.getBestRatedAccommodationsLiveData().observe(getViewLifecycleOwner(), this::configureBestRatedAccommodationsBar);
        statisticsViewModel.getMostBookedAccommodationsHostLiveData().observe(getViewLifecycleOwner(), this::configureMostBookedAccommodationsBar);
        statisticsViewModel.getEarningsHostLiveData().observe(getViewLifecycleOwner(), this::configureEarningsBar);
        showStatistics();
        return binding.getRoot();
    }

    private void showStatistics() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
        boolean isHostEstablished = dataStoreHelper.getBoolValue("START_HOST");
        if (isHostEstablished) {
            statisticsViewModel.loadMostBookedHostAccommodations();
            statisticsViewModel.loadHostEarnings();
        } else {
            statisticsViewModel.loadMostBookedAccommodations();
            statisticsViewModel.loadBestRatedAccommodations();
        }
    }

    private void configureMostBookedAccommodationsBar(List<AccommodationGrpc> mostBookedAccommodations) {
        binding.txvTitleBarOne.setText(R.string.header_most_booked_acco);
        BarChart barChart = binding.statisticsBarOne;
        BarDataSet barDataSet = new BarDataSet(transformMostBookedAccommodationsIntoBarEntries(mostBookedAccommodations), "Accomodations");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barData.setBarWidth(1f);

        barChart.setData(barData);
        barChart.getDescription().setText(getString(R.string.header_most_booked_acco));
        barChart.animateY(2000);

        final String[] labels = getAxisLabels(mostBookedAccommodations).toArray(new String[0]);
        XAxis axis = barChart.getXAxis();
        axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        axis.setGranularity(1f);
        axis.setGranularityEnabled(true);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
        binding.pgbBarOne.setVisibility(View.GONE);
    }

    private void configureBestRatedAccommodationsBar(List<AccommodationGrpc> bestRatedAccommodations) {
        binding.txvTitleBarTwo.setText(R.string.header_best_acco);
        BarChart barChart = binding.statisticsBarTwo;
        BarDataSet barDataSet = new BarDataSet(transformBestRatedAccommodationsIntoBarEntries(bestRatedAccommodations), "Accomodations");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barData.setBarWidth(1f);

        barChart.setData(barData);
        barChart.getDescription().setText(getString(R.string.header_best_acco));
        barChart.animateY(2000);

        final String[] labels = getAxisLabels(bestRatedAccommodations).toArray(new String[0]);
        XAxis axis = barChart.getXAxis();
        axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        axis.setGranularity(1f);
        axis.setGranularityEnabled(true);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.invalidate();
        binding.pgbBarTwo.setVisibility(View.GONE);
    }

    private void configureEarningsBar(List<EarningGrpc> earnings) {
        binding.txvTitleBarTwo.setText(R.string.header_monthly_earns);
        BarChart barChart = binding.statisticsBarTwo;

        List<BarEntry> barEntries = transformEarningsIntoBarEntries(earnings);
        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.tag_earnings));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.getDescription().setText(getString(R.string.tag_earnings));
        barChart.animateY(2000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MonthValueFormatter());
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
        binding.pgbBarTwo.setVisibility(View.GONE);
    }

    private ArrayList<BarEntry> transformEarningsIntoBarEntries(List<EarningGrpc> earnings) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (EarningGrpc earning: earnings) {
            int month = Integer.parseInt(earning.getMonth());
            barEntries.add(new BarEntry(month, (float) earning.getEarning()));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> transformMostBookedAccommodationsIntoBarEntries(List<AccommodationGrpc> accommodations) {
        ArrayList<BarEntry> barEntriesAccommodations = new ArrayList<>();
        int i = 0;
        for (AccommodationGrpc acc: accommodations) {
            barEntriesAccommodations.add(new BarEntry(i, acc.getBookingsNumber()));
            i++;
        }
        return barEntriesAccommodations;
    }

    private ArrayList<BarEntry> transformBestRatedAccommodationsIntoBarEntries(List<AccommodationGrpc> accommodations) {
        ArrayList<BarEntry> barEntriesAccommodations = new ArrayList<>();
        int i = 0;
        for (AccommodationGrpc acc: accommodations) {
            barEntriesAccommodations.add(new BarEntry(i, (float) acc.getRate()));
            i++;
        }
        return barEntriesAccommodations;
    }

    private ArrayList<String> getAxisLabels(List<AccommodationGrpc> accommodations) {
        ArrayList<String> xAxis = new ArrayList();
        for (AccommodationGrpc accommodation: accommodations) {
            xAxis.add(accommodation.getTitle());
        }
        return  xAxis;
    }

    private void manageProgressBarOne() {
        statisticsViewModel.getRequestBarOneStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbBarOne.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbBarOne.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbBarOne.setVisibility(View.GONE);
            }
        });
    }

    private void manageProgressBarTwo() {
        statisticsViewModel.getRequestBarTwoStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbBarTwo.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbBarTwo.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbBarTwo.setVisibility(View.GONE);
            }
        });
    }
}