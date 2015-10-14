package com.kieranflay.cocktailcompanion.saved;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.TinyDB;
import com.kieranflay.cocktailcompanion.Utilities;
import com.kieranflay.cocktailcompanion.browse.GridViewAdapter;
import com.kieranflay.cocktailcompanion.detail.DetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class SaveActivityFragment extends Fragment {

    public SaveActivityFragment() {
    }

    View rootView;
    ListView listViewCocktails;
    TextView tvNoSavedData;
    private String LOG_TAG = SaveActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_save, container, false);

        listViewCocktails = (ListView) rootView.findViewById(R.id.lvSavedCocktails);
        tvNoSavedData = (TextView) rootView.findViewById(R.id.tvNoDataView);

        new LoadSavedCocktails().execute();

        return rootView;
    }

    private class LoadSavedCocktails extends AsyncTask<Boolean, Integer, ArrayList<Drink>> {

        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected ArrayList<Drink> doInBackground(Boolean... params) {
            return Utilities.loadSavedCocktailList(getContext());
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> drinksList) {

            if (drinksList.size() == 0 || drinksList.equals(null)) {
                Log.v(LOG_TAG, "No saved drinks.");
                listViewCocktails.setVisibility(View.GONE);
                tvNoSavedData.setVisibility(View.VISIBLE);
            } else {
                Log.v(LOG_TAG, drinksList.size() + " saved drinks loaded.");
                listViewCocktails.setVisibility(View.VISIBLE);
                tvNoSavedData.setVisibility(View.GONE);

                final ArrayList<String> cocktailIds = new ArrayList<String>();
                final ArrayList<String> cocktailNames = new ArrayList<String>();
                final ArrayList<String> cocktailImages = new ArrayList<String>();

                if (drinksList != null) {

                    for (int i = 0; i < drinksList.size(); i++) {
                        Drink drink = drinksList.get(i);
                        cocktailIds.add(drink.getIdDrink());
                        cocktailNames.add(drink.getStrDrink());
                        cocktailImages.add(drink.getStrDrinkThumb());
                    }
                }

                final SaveAdapter saveAdapter = new
                        SaveAdapter(getActivity(), cocktailNames, cocktailImages);
                listViewCocktails.setAdapter(saveAdapter);

                listViewCocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
            dialog.hide();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}


