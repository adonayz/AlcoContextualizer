package edu.wpi.alcoholcontextualizer.fragments.people;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.adapters.listviews.PictogramListViewAdapter;
import edu.wpi.alcoholcontextualizer.model.Person;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndividualPeopleFragment extends Fragment {


    public IndividualPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people_individual, container, false);

        ListView pictogramListView = (ListView) view.findViewById(R.id.listView_pictogram);

        pictogramListView.setAdapter(new PictogramListViewAdapter(getContext(), getSortedTestPeople(), getSortedTestPeople().get(0).getDrinkAmount()));
        return view;
    }

    // TODO: ============= Replace this with realistic database data===========
    //region Testing
    private ArrayList<Person> getSortedTestPeople() {
        ArrayList<Person> people = new ArrayList<>();

        people.add(new Person(3, "Peter", 10, "Group 1"));
        people.add(new Person(4, "Hailey", 8, "Group 1"));
        people.add(new Person(4, "Brown", 6, "Group 1"));
        people.add(new Person(2, "Tom", 3, "Group 1"));
        people.add(new Person(1, "Bob", 2, "Group 1"));

        //TODO: sort the array
//        Arrays.sort(people.toArray());

        return people;
    }
    //endregion

}
