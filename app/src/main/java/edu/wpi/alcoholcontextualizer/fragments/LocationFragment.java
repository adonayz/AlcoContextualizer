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
import edu.wpi.alcoholcontextualizer.fragments.location.FrequentLocationFragment;
import edu.wpi.alcoholcontextualizer.fragments.location.RecentLocationFragment;

/**
 * @author Jules Voltaire on 1/25/2017.
 *         This class is the controller fragment class for fragment_location view file
 */
public class LocationFragment extends Fragment {
    private static final String ARGUMENT_TAB_ID = "tabId";

    private int tab;

    //region Constructors

    /**
     * Initializes a new instance of this class
     */
    public LocationFragment() {
        // Required empty public constructor
        this.tab = 0;
    }
    //endregion

    public static LocationFragment newInstance(int tabId) {
        final Bundle args = new Bundle();
        args.putInt(ARGUMENT_TAB_ID, tabId);
        final LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //region Overridden Callback Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        final Bundle args = getArguments();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_location);
        tabLayout.addTab(tabLayout.newTab().setText("Frequent"));
        tabLayout.addTab(tabLayout.newTab().setText("Recent"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_location);

        ArrayList<Fragment> tabs = new ArrayList<>();
        tabs.add(new FrequentLocationFragment());
        tabs.add(new RecentLocationFragment());

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
