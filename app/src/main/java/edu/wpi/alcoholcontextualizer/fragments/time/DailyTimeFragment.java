package edu.wpi.alcoholcontextualizer.fragments.time;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;
import edu.wpi.alcoholcontextualizer.model.Time;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyTimeFragment extends Fragment {

    Calendar calendar = Calendar.getInstance();
    TextView dateDisplay;
    DatabaseHandler dbHandler;
    LineChart lineChart;

    public DailyTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dbHandler = new DatabaseHandler(this.getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_daily, container, false);

        // Set text of date from date picker
        dateDisplay = (TextView) view.findViewById(R.id.date_text);
        updateLabel();

        // Set listener for button that opens date picker
        Button datePickerButton = (Button) view.findViewById(R.id.date_Picker_Button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });


        // Set Line chart
        lineChart = (LineChart) view.findViewById(R.id.lineChart);


        lineChart.animateY(5000);
//        lineChart.getAxisRight().setDrawLabels(false);
//        lineChart.getLegend().setEnabled(false);


        lineChart.setDescription("Amount drank every hour");
        updateDataEntries(lineChart, convertMonthIntToString(calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));

        return view;
    }


    private List<Entry> getDataEntries(String month, int day) {
        //creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        // pull data from db
        List<Time> allTime = dbHandler.getTimeListOnDate(month, day);
        System.out.println("Here is test month " + calendar.get(Calendar.MONTH));
        for (Time time : allTime) {
            entries.add(new Entry(time.getDrinkAmount(), time.getHour()));
        }

        //no entries case
        if (entries.size() == 0) {
            entries.add(new Entry(0, 0));
        }

        return entries;
    }

    private void updateDataEntries(LineChart lineChart, String month, int day) {

//        System.out.println("Here is a test month " + month);
//        System.out.println("Here is a test day " + day);

//         creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2f, 0));
        entries.add(new Entry(3f, 1));
        entries.add(new Entry(2f, 2));
        entries.add(new Entry(1f, 3));
        entries.add(new Entry(0f, 4));
        entries.add(new Entry(0f, 5));
        entries.add(new Entry(0f, 6));
        entries.add(new Entry(0f, 7));
        entries.add(new Entry(0f, 8));
        entries.add(new Entry(0f, 9));
        entries.add(new Entry(0f, 10));
        entries.add(new Entry(0f, 11));
        entries.add(new Entry(0f, 12));
        entries.add(new Entry(0f, 13));
        entries.add(new Entry(0f, 14));
        entries.add(new Entry(0f, 15));
        entries.add(new Entry(00f, 16));
        entries.add(new Entry(0f, 17));
        entries.add(new Entry(1f, 18));
        entries.add(new Entry(2f, 19));
        entries.add(new Entry(2f, 20));
        entries.add(new Entry(3f, 21));
        entries.add(new Entry(4f, 22));
        entries.add(new Entry(4f, 23));

        //set chart data entries
//        LineDataSet dataSet = new LineDataSet(getDataEntries(month, day), "Amount drank");
        LineDataSet dataSet = new LineDataSet(entries, "Amount drank");
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
//        dataSet.setColors(Colors.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("12am");
        labels.add("1am");
        labels.add("2am");
        labels.add("3am");
        labels.add("4am");
        labels.add("5am");
        labels.add("6am");
        labels.add("7am");
        labels.add("8am");
        labels.add("9am");
        labels.add("10m");
        labels.add("11am");
        labels.add("12pm");
        labels.add("1pm");
        labels.add("2pm");
        labels.add("3pm");
        labels.add("4pm");
        labels.add("5pm");
        labels.add("6pm");
        labels.add("7pm");
        labels.add("8pm");
        labels.add("9pm");
        labels.add("10pm");
        labels.add("11pm");

        LineData data = new LineData(labels, dataSet);
        lineChart.animateY(5000);
        lineChart.setData(data);

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

    private void updateLabel() {

        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        dateDisplay.setText(mm + "/" + dd + "/" + yy);

    }

    /**
     * Displays date picker and sets global calendar date to selected date when closed
     *
     * @param view
     */
    public void showDatePicker(View view) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDataEntries(lineChart, convertMonthIntToString(monthOfYear + 1), dayOfMonth); //edit data
            updateLabel(); //edit text label
        }

    };

}
