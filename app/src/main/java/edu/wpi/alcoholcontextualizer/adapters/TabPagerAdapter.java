package edu.wpi.alcoholcontextualizer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by jules on 1/26/2017.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    //region Variables
    /**
     * Holds the number of tabs
     */
    private ArrayList<Fragment> tabs = new ArrayList<Fragment>();
    //endregion

    //region Constructors

    /**
     * Initializes a new instance of this class
     *
     * @param fm   The fragment manager
     * @param tabs The List of tab fragments to be used
     */
    public TabPagerAdapter(FragmentManager fm, ArrayList<Fragment> tabs) {
        super(fm);
        this.tabs = tabs;
    }
    //endregion

    // Overridden Methods
    @Override
    public Fragment getItem(int position) {
        if (!tabs.isEmpty()) {
            return tabs.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
    //endregion
}
