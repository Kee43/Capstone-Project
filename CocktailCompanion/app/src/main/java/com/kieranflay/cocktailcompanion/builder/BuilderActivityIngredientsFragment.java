package com.kieranflay.cocktailcompanion.builder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.TinyDB;
import com.kieranflay.cocktailcompanion.Utilities;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BuilderActivityIngredientsFragment extends Fragment {

    public BuilderActivityIngredientsFragment() {
    }

    View mRootView;
    ArrayList<String> cocktailIngredients;
    ArrayList<String> savedCocktailIngredients;
    ListView mIngredientsList;
    ListView mUserIngredientsList;
    ArrayAdapter<String> mAdapter;
    TextView tvNoData;
    SearchView mSearchIngredients;
    private String LOG_TAG = BuilderActivityIngredientsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_builder_activity_ingredients, container, false);

        savedCocktailIngredients = Utilities.loadSavedIngredientList(getContext());

        mSearchIngredients = (SearchView) mRootView.findViewById(R.id.svSearch);
        mUserIngredientsList = (ListView) mRootView.findViewById(R.id.lvUserIngredients);
        mIngredientsList = (ListView) mRootView.findViewById(R.id.lvIngredients);
        tvNoData = (TextView) mRootView.findViewById(R.id.tvIngredientsNoDataView);

        final IngredientsAdapter ingredientsAdapter = new
                IngredientsAdapter(getActivity(), savedCocktailIngredients);
        mUserIngredientsList.setAdapter(ingredientsAdapter);

        if (savedCocktailIngredients.size() == 0 || savedCocktailIngredients.equals(null)) {
            mUserIngredientsList.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "Ingredients listview is empty.");
        } else {
            mUserIngredientsList.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }

        mIngredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ingredient = (String) parent.getItemAtPosition(position);
                Log.d(LOG_TAG, "Ingredient clicked on: " + ingredient);
                savedCocktailIngredients.add(ingredient);
                ingredientsAdapter.notifyDataSetChanged();
                Utilities.saveIngredient(getActivity(), ingredient);
                mUserIngredientsList.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            }
        });

        mSearchIngredients.setQueryHint("Add an ingredient...");
        mSearchIngredients.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Disabled text pop up - reference for line below:
                // http://stackoverflow.com/questions/20278385/how-to-remove-android-searchview-popup-text-while-searching
                android.widget.Filter filter = mAdapter.getFilter();

                if (newText.length() == 0) {
                    filter.filter(null);
                } else {
                    filter.filter(newText);
                }
                return true;
            }
        });

        if (Utilities.isNetworkAvailable(getActivity())) {
            new LoadIngredients().execute();
        } else {
            Log.e(LOG_TAG, "No internet connection");
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
        }

        return mRootView;
    }

    private class LoadIngredients extends AsyncTask<Void, Integer, ArrayList<Drink>> {

        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected ArrayList<Drink> doInBackground(Void... params) {

            String url = "http://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";

            // Reference http://stackoverflow.com/questions/1395551/convert-a-json-string-to-object-in-java
            return JsonHandler.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> drinksList) {

            cocktailIngredients = new ArrayList<String>();
            if (cocktailIngredients.size() != 0){
                Log.d(LOG_TAG, "Cocktail ingredients size: " + cocktailIngredients.size());
            } else {
                Log.e(LOG_TAG, "Cocktail ingredients list is empty");
            }

            if (drinksList != null) {
                for (int i = 0; i < drinksList.size(); i++) {
                    Drink drink = drinksList.get(i);
                    cocktailIngredients.add(drink.getStrIngredient1());
                }
            }

            mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cocktailIngredients);
            mIngredientsList.setAdapter(mAdapter);
            mIngredientsList.setTextFilterEnabled(false);
            dialog.hide();
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