package com.kieranflay.cocktailcompanion.builder;

// Class reference: http://www.android4devs.com/2015/01/how-to-make-material-design-sliding-tabs.html

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            BuilderActivityIngredientsFragment builderActivityIngredientsFragment = new BuilderActivityIngredientsFragment();
            return builderActivityIngredientsFragment;
        } else {
            BuilderActivityCocktailsFragment builderActivityCocktailsFragment = new BuilderActivityCocktailsFragment();
            return builderActivityCocktailsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
