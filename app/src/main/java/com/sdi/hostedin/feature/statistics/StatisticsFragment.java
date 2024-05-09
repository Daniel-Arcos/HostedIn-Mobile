package com.sdi.hostedin.feature.statistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;

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
        // Inflate the layout for this fragment
        binding = FragmentStatisticsBinding.inflate(getLayoutInflater());

        BarChart barChart = binding.statisticsBarOne;
        BarDataSet barDataSet = new BarDataSet(getData(), "Accomodations");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        final String[] labels = getAxis().toArray(new String[0]);
        XAxis axis = barChart.getXAxis();
        axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Most booked accomodations");
        barChart.animateY(2000);

        BarChart barChartTwo = binding.statisticsBarTwo;
        BarDataSet barDataSetTwo = new BarDataSet(getData(), "Accomodations");
        barDataSetTwo.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetTwo.setValueTextColor(Color.BLACK);
        barDataSetTwo.setValueTextSize(16f);
        BarData barDataTwo = new BarData(barDataSetTwo);
        final String[] labelsTwo = getAxis().toArray(new String[0]);
        XAxis axisTwo = barChart.getXAxis();
        axisTwo.setValueFormatter(new IndexAxisValueFormatter(labelsTwo));
        barChartTwo.setFitBars(true);
        barChartTwo.setData(barDataTwo);
        barChartTwo.getDescription().setText("Most booked accomodations");
        barChartTwo.animateY(2000);

        return binding.getRoot();
    }

    private ArrayList<BarEntry> getData() {
        ArrayList<BarEntry> accomodations = new ArrayList<>();
        accomodations.add(new BarEntry(1, 100));
        accomodations.add(new BarEntry(2, 91));
        accomodations.add(new BarEntry(3, 120));
        accomodations.add(new BarEntry(4, 290));
        accomodations.add(new BarEntry(5, 83));
        accomodations.add(new BarEntry(6, 130));
        accomodations.add(new BarEntry(7, 260));
        accomodations.add(new BarEntry(8, 120));
        return accomodations;
    }

    private ArrayList<String> getAxis() {
        ArrayList<String> xAxis = new ArrayList();
        xAxis.add("");
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");

        return xAxis;
    }
}