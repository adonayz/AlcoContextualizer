package edu.wpi.alcoholcontextualizer.fragments.people;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;
import edu.wpi.alcoholcontextualizer.treemap.MapLayoutView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupPeopleFragment extends Fragment {


    public GroupPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new MapLayoutView(this.getActivity(), (new DatabaseHandler(this.getActivity())).getGroupTreeModel());
    }

}
