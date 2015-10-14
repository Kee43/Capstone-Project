package com.kieranflay.cocktailcompanion.builder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kieranflay.cocktailcompanion.R;

/**
 * A placeholder fragment containing a simple view.
 */

public class BuilderActivityFragment extends Fragment {

    public BuilderActivityFragment() {
    }

    CharSequence tabNames[] = {"Ingredients", "Cocktails"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_builder, container, false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), tabNames, 2);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.light_blue);
            }
        });

        slidingTabLayout.setViewPager(viewPager);
        return rootView;
    }
}
