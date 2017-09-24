package edu.wpi.alcoholcontextualizer.fragments.time;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;
import edu.wpi.alcoholcontextualizer.utilities.Colors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyTimeFragment extends Fragment {

    DatabaseHandler dbHandler;

    public MonthlyTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_monthly, container, false);

        dbHandler = new DatabaseHandler(this.getActivity());

        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("Amount drank every month");
        chart.getAxisRight().setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        chart.animateXY(2000, 2000);
        chart.invalidate();

        return view;

    }

    private ArrayList<BarDataSet> getDataSet() {


        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(45.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(39.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(42.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(40.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(43.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(51.000f, 5); // Jun
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(55.000f, 6); // Jul
        valueSet1.add(v1e7);
        BarEntry v1e8 = new BarEntry(48.000f, 7); // Aug
        valueSet1.add(v1e8);
        BarEntry v1e9 = new BarEntry(43.000f, 8); // Sep
        valueSet1.add(v1e9);
        BarEntry v1e10 = new BarEntry(42.000f, 9); // Oct
        valueSet1.add(v1e10);
        BarEntry v1e11 = new BarEntry(44.000f, 10); // Nov
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(47.000f, 11); // Dec
        valueSet1.add(v1e12);

//        for(int i=0; i < 12; i++) {
//            int monthTotal = dbHandler.getMonthTimeTotals(convertMonthIntToString(i+1));
//            BarEntry entry = new BarEntry(monthTotal, 0);
//            valueSet1.add(entry);
//        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColors(Colors.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }

    private String convertMonthIntToString(int monthNumb) {

        switch (monthNumb) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "January";
        }
    }

}
