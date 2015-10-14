package com.kieranflay.cocktailcompanion.surprise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.browse.GridViewAdapter;
import com.kieranflay.cocktailcompanion.detail.DetailActivity;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurpriseActivityFragment extends Fragment {

    public SurpriseActivityFragment() {
    }

    View rootView;
    private String LOG_TAG = SurpriseActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_surprise, container, false);
        Button btnSurprise = (Button) rootView.findViewById(R.id.btnSurpriseMe);
        btnSurprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadRandomCocktail().execute();
            }
        });

        return rootView;
    }

    private class LoadRandomCocktail extends AsyncTask<Void, Integer, ArrayList<Drink>> {

        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected ArrayList<Drink> doInBackground(Void... params) {

            String url = "http://www.thecocktaildb.com/api/json/v1/1/random.php";
            return JsonHandler.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> drinksList) {

            if (drinksList != null || drinksList.size() != 0){

                Drink randomDrink = drinksList.get(0);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("cocktail_id", randomDrink.getIdDrink());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Log.e(LOG_TAG, "No internet connection");
                Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
            dialog.hide();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {}
    }
}

