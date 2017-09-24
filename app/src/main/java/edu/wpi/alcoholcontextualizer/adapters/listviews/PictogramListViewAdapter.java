package edu.wpi.alcoholcontextualizer.adapters.listviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.model.Person;

/**
 * Created by jules on 2/14/2017.
 */

public class PictogramListViewAdapter extends ArrayAdapter<Person> {

    private int maxAmount = 0;

    public PictogramListViewAdapter(Context context, ArrayList<Person> people, int amount) {
        super(context, R.layout.item_pictogram_row, people);
        if (amount > this.maxAmount) {
            maxAmount = amount;
        }

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pictogram_row, parent, false);
        }

        Person person = getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.textView_pictogram_title);
        name.setText(person.getPersonName());


        LinearLayout imagesContainer = (LinearLayout) convertView.findViewById(R.id.linearLayout_pictogram_images_container);

        int personDrinkAmount = person.getDrinkAmount();
        for (int i = 0; i < personDrinkAmount; i++) {
            View bottleView = LayoutInflater.from(getContext()).inflate(R.layout.item_pictogram_row_item, parent, false);
            bottleView.setLayoutParams(new ViewGroup.LayoutParams(getScaledWidth(), 100));
            imagesContainer.addView(bottleView);
        }

        TextView amount = (TextView) convertView.findViewById(R.id.textView_pictogram_amount);
        amount.setText("x " + String.valueOf(person.getDrinkAmount()));

        return convertView;
    }

    private int getScaledWidth() {
        return 800 / maxAmount;
    }
}
