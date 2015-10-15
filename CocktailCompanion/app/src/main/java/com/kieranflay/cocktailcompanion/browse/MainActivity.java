package com.kieranflay.cocktailcompanion.browse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Utilities;
import com.kieranflay.cocktailcompanion.about.AboutActivityFragment;
import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.NavigationDrawerFragment;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.builder.BuilderActivityFragment;
import com.kieranflay.cocktailcompanion.detail.DetailActivity;
import com.kieranflay.cocktailcompanion.saved.SaveActivityFragment;
import com.kieranflay.cocktailcompanion.surprise.SurpriseActivityFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Color the action bar
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue)));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.theme_primary_dark));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(LOG_TAG, "Navigation drawer item selected: " + position);

        switch (position) {
            case 0:
                mTitle = getString(R.string.title_section1);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CocktailBrowseFragment.newInstance(position + 1))
                        .addToBackStack(null)
                        .commit();
                restoreActionBar();
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                fragment = new SaveActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                restoreActionBar();
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                fragment = new SurpriseActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                restoreActionBar();
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                fragment = new BuilderActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                restoreActionBar();
                break;
            case 4:
                mTitle = getString(R.string.title_section5);
                fragment = new AboutActivityFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                restoreActionBar();
                break;
        }
    }

    // Based on http://stackoverflow.com/questions/14275627/how-to-go-back-to-previous-fragment-on-pressing-manually-back-button-of-individu
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CocktailBrowseFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private String LOG_TAG = CocktailBrowseFragment.class.getSimpleName();
        private static final String ARG_SECTION_NUMBER = "section_number";
        View rootView;
        GridView gridViewCocktails;
        TextView tvTotal;

        public int spinnerValue;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CocktailBrowseFragment newInstance(int sectionNumber) {
            CocktailBrowseFragment fragment = new CocktailBrowseFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public CocktailBrowseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            tvTotal = (TextView) rootView.findViewById(R.id.tvTotalCocktails);

            setUpSpinner();
            setUpGridView();

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void setUpSpinner() {
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            List<String> list = new ArrayList<String>();
            list.add("None");
            list.add("Alcoholic");
            list.add("Non-Alcoholic");


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(LOG_TAG, "Spinner value selected: " + position);
                    if (Utilities.isNetworkAvailable(getActivity())) {
                        if (position == 0) {
                            spinnerValue = 0;
                            new LoadCocktails().execute(spinnerValue);
                        } else if (position == 1) {
                            spinnerValue = 1;
                            new LoadCocktails().execute(spinnerValue);
                        } else if (position == 2) {
                            spinnerValue = 2;
                            new LoadCocktails().execute(spinnerValue);
                        }
                    } else {
                        Log.e(LOG_TAG, "No internet connection");
                        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        public void setUpGridView() {
            gridViewCocktails = (GridView) rootView.findViewById(R.id.gvBrowseCocktails);
        }


        private class LoadCocktails extends AsyncTask<Integer, Integer, ArrayList<Drink>> {

            ProgressDialog dialog =
                    new ProgressDialog(getActivity());

            @Override
            protected ArrayList<Drink> doInBackground(Integer... params) {

                if (params[0] == 0) {
                    return Utilities.loadAllCocktails(getActivity());
                } else if (params[0] == 1){
                    return Utilities.loadAlcoholicCocktails(getActivity());
                } else {
                    return Utilities.loadNonAlcoholicCocktails(getActivity());
                }
            }

            @Override
            protected void onPostExecute(ArrayList<Drink> drinksList) {

                dialog.dismiss();
                dialog.hide();

                final ArrayList<String> cocktailIds = new ArrayList<String>();
                final ArrayList<String> cocktailNames = new ArrayList<String>();
                final ArrayList<String> cocktailImages = new ArrayList<String>();

                if (drinksList != null) {

                    for (int i = 0; i < drinksList.size(); i++) {
                        Drink drink = drinksList.get(i);
                        if (drink.getStrDrinkThumb() == null) {
                            // We only want cocktails with images
                        } else {
                            cocktailIds.add(drink.getIdDrink());
                            cocktailNames.add(drink.getStrDrink());
                            cocktailImages.add(drink.getStrDrinkThumb());

                        }
                    }
                    tvTotal.setText("Total cocktails: " + cocktailIds.size());
                }

                final GridViewAdapter gridViewAdapter = new
                        GridViewAdapter(getActivity(), cocktailNames, cocktailImages);
                gridViewCocktails = (GridView) rootView.findViewById(R.id.gvBrowseCocktails);
                gridViewCocktails.setAdapter(gridViewAdapter);


                gridViewCocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (Utilities.isNetworkAvailable(getContext())){
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("cocktail_id", cocktailIds.get(position));
                            Log.d(LOG_TAG, "Cocktail selected: " + cocktailNames.get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                            Log.e(LOG_TAG, "No internet connection");
                        }
                    }
                });
            }

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Loading...");
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog.show();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
            }
        }
    }
}
