package edu.wpi.alcoholcontextualizer.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.adapters.TabPagerAdapter;
import edu.wpi.alcoholcontextualizer.fragments.people.GroupPeopleFragment;
import edu.wpi.alcoholcontextualizer.fragments.people.IndividualPeopleFragment;

/**
 * @author Jules Voltaire on 1/25/2017.
 *         This class is the controller fragment class for fragment_people view file
 */
public class PeopleFragment extends Fragment {
    private static final String ARGUMENT_TAB_ID = "tabId";

    private int tab;
    //region Constructors

    /**
     * Initializes a new instance of this class
     */
    public PeopleFragment() {
        // Required empty public constructor
    }
    //endregion

    public static PeopleFragment newInstance(int tabId) {
        final Bundle args = new Bundle();
        args.putInt(ARGUMENT_TAB_ID, tabId);
        final PeopleFragment fragment = new PeopleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //region Overridden Callback Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        final Bundle args = getArguments();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_people);
        tabLayout.addTab(tabLayout.newTab().setText("Individual"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_people);

        ArrayList<Fragment> tabs = new ArrayList<>();
        tabs.add(new IndividualPeopleFragment());
        tabs.add(new GroupPeopleFragment());

        final TabPagerAdapter adapter = new TabPagerAdapter(getFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tab = args == null ? tab : args.getInt(ARGUMENT_TAB_ID);
        viewPager.setCurrentItem(tab);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
    //endregion

}
