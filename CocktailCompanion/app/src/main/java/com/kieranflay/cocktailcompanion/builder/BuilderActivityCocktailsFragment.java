package com.kieranflay.cocktailcompanion.builder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;
import com.kieranflay.cocktailcompanion.detail.DetailActivity;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BuilderActivityCocktailsFragment extends Fragment {

    public BuilderActivityCocktailsFragment() {
    }

    View mRootView;
    ArrayList<String> cocktailIds;
    ArrayList<String> cocktailNames;
    ArrayList<String> cocktailIngredients;
    ArrayList<String> listIngredientsNeeded;
    ArrayList<Integer> listNumberIngredientsNeeded;

    ListView mCocktailList;
    TextView mNoCocktails;
    CocktailsAdapter mAdapter;

    private String LOG_TAG = BuilderActivityCocktailsFragment.class.getSimpleName();

    boolean initalLoadDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_builder_activity_cocktails, container, false);

        initalLoadDone = false;
        mCocktailList = (ListView) mRootView.findViewById(R.id.lvBuilderCocktails);
        mNoCocktails = (TextView) mRootView.findViewById(R.id.tvBuilderNoDataView);
        cocktailIngredients = Utilities.loadSavedIngredientList(getActivity());

        return mRootView;
    }

    private static boolean isFragmentVisible;

    // Reference: http://stackoverflow.com/questions/9323279/how-to-test-if-a-fragment-view-is-visible-to-the-user
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;

        if (isFragmentVisible) {
            Log.d(LOG_TAG, "Total ingredients: " + Integer.toString(Utilities.loadSavedIngredientList(getActivity()).size()));
            if (Utilities.loadSavedIngredientList(getActivity()).size() == 0) {
                mCocktailList.setVisibility(View.GONE);
                mNoCocktails.setVisibility(View.VISIBLE);
            } else {
                // If it has not changed size
                if (Utilities.loadSavedIngredientList(getActivity()).size() == cocktailIngredients.size()) {

                    // Unless the inital load has been completed
                    if (initalLoadDone == false) {
                        if (Utilities.isNetworkAvailable(getActivity())) {
                            new LoadCocktails().execute();
                        } else {
                            Log.e(LOG_TAG, "No internet connection");
                            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                        }

                        initalLoadDone = true;
                    }
                } else {
                    // List has been changed so update
                    if (Utilities.isNetworkAvailable(getActivity())) {
                        new LoadCocktails().execute();
                    } else {
                        Log.e(LOG_TAG, "No internet connection");
                        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                    cocktailIngredients = Utilities.loadSavedIngredientList(getActivity());
                }
                mCocktailList.setVisibility(View.VISIBLE);
                mNoCocktails.setVisibility(View.GONE);

            }
        }
    }

    public class LoadCocktails extends AsyncTask<Void, Integer, ArrayList<Drink>> {

        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected ArrayList<Drink> doInBackground(Void... params) {

            ArrayList<Drink> tempCocktailList = new ArrayList<Drink>();
            ArrayList<Drink> cocktailList = new ArrayList<Drink>();

            String url = "http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic";

            // http://stackoverflow.com/questions/1395551/convert-a-json-string-to-object-in-java
            tempCocktailList = JsonHandler.getJSONFromUrl(url);

            for (Drink tempDrink : tempCocktailList) {
                String id = "http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + tempDrink.getIdDrink();
                ArrayList<Drink> drinks = JsonHandler.getJSONFromUrl(id);
                cocktailList.add(drinks.get(0));
            }

            return cocktailList;
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> cocktailList) {

            // IDs
            cocktailIds = new ArrayList<String>();

            // Names
            cocktailNames = new ArrayList<String>();

            // Ingredients still needed
            listIngredientsNeeded = new ArrayList<String>();

            // Number of ingredients still needed
            listNumberIngredientsNeeded = new ArrayList<Integer>();

            // For every possible drink
            for (Drink drink : cocktailList) {

                int numberOfBlanks = 0;
                int numberOfMatches = 0;
                String ingredientsNeeded = "";

                // For every selected ingredient by the user
                for (String ingredient : cocktailIngredients) {

                    // This code loops through each ingredient on the cocktail list and checks
                    // if the user has any matching ingredients, the results are displayed as
                    // user ingredient (number of needed ingredients)

                    if (drink.getStrIngredient1().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient1();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient1();
                        }
                    } else if (drink.getStrIngredient2().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient2();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient2();
                        }
                    } else if (drink.getStrIngredient3().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient3();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient3();
                        }
                    } else if (drink.getStrIngredient4().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient4();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient4();
                        }
                    } else if (drink.getStrIngredient5().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient5();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient5();
                        }
                    } else if (drink.getStrIngredient6().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient6();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient6();
                        }
                    } else if (drink.getStrIngredient7().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient7();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient7();
                        }
                    } else if (drink.getStrIngredient8().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient8();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient8();
                        }
                    } else if (drink.getStrIngredient9().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient9();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient9();
                        }
                    } else if (drink.getStrIngredient10().equals(ingredient)) {
                        numberOfMatches++;
                        if (ingredientsNeeded.equals("")) {
                            ingredientsNeeded = drink.getStrIngredient10();
                        } else {
                            ingredientsNeeded = ingredientsNeeded + ", " + drink.getStrIngredient10();
                        }
                    }
                }

                // As we need the number of non-matches, we need to take off the ingredients
                // that are actually blank, so no comparison is possible

                if (drink.getStrIngredient1() == null || drink.getStrIngredient1() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient2() == null || drink.getStrIngredient2() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient3() == null || drink.getStrIngredient3() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient4() == null || drink.getStrIngredient4() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient5() == null || drink.getStrIngredient5() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient6() == null || drink.getStrIngredient6() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient7() == null || drink.getStrIngredient7() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient8() == null || drink.getStrIngredient8() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient9() == null || drink.getStrIngredient9() == "") {
                    numberOfBlanks++;
                }
                if (drink.getStrIngredient10() == null || drink.getStrIngredient10() == "") {
                    numberOfBlanks++;
                }

                // 10 possible ingredients - blanks - matches = ingredients needed
                int neededIngredients = 10 - numberOfBlanks - numberOfMatches;

                if (neededIngredients < 3 && numberOfMatches > 1) {
                    cocktailIds.add(drink.getIdDrink());
                    cocktailNames.add(drink.getStrDrink());
                    listNumberIngredientsNeeded.add(neededIngredients);
                    listIngredientsNeeded.add(ingredientsNeeded);
                } else {
                    // Dont add the drink as it contains no ingredients that match
                }
            }

            mAdapter = new CocktailsAdapter(getActivity(), cocktailNames, listIngredientsNeeded, listNumberIngredientsNeeded);
            mCocktailList.setAdapter(mAdapter);
            mCocktailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.v(LOG_TAG, "Cocktails adapter pos clicked on:" + position);
                    if (Utilities.isNetworkAvailable(getActivity())) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("cocktail_id", cocktailIds.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Log.e(LOG_TAG, "No internet connection");
                        Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.hide();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Building cocktails...");
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
