package com.kieranflay.cocktailcompanion.detail;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    View rootView;
    String cocktailId;

    TextView tvName, tvByline, tvMethod;
    ImageView ivCocktailIcon;
    ImageView ivPhotoBackground;

    private String LOG_TAG = DetailFragment.class.getSimpleName();

    TextView tvIngredient1, tvIngredient2, tvIngredient3, tvIngredient4, tvIngredient5, tvIngredient6, tvIngredient7,
            tvIngredient8, tvIngredient9, tvIngredient10;

    TextView tvMeasure1, tvMeasure2, tvMeasure3, tvMeasure4, tvMeasure5, tvMeasure6, tvMeasure7, tvMeasure8, tvMeasure9,
            tvMeasure10;

    TextView tvDash1, tvDash2, tvDash3, tvDash4, tvDash5, tvDash6, tvDash7, tvDash8, tvDash9, tvDash10;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);

        tvName = (TextView) rootView.findViewById(R.id.cocktail_name);
        tvByline = (TextView) rootView.findViewById(R.id.cocktail_byline);
        ivCocktailIcon = (ImageView) rootView.findViewById(R.id.cocktail_glass_icon);
        tvMethod = (TextView) rootView.findViewById(R.id.cocktail_method);
        ivPhotoBackground = (ImageView) rootView.findViewById(R.id.photo);

        tvIngredient1 = (TextView) rootView.findViewById(R.id.ingredient_1);
        tvIngredient2 = (TextView) rootView.findViewById(R.id.ingredient_2);
        tvIngredient3 = (TextView) rootView.findViewById(R.id.ingredient_3);
        tvIngredient4 = (TextView) rootView.findViewById(R.id.ingredient_4);
        tvIngredient5 = (TextView) rootView.findViewById(R.id.ingredient_5);
        tvIngredient6 = (TextView) rootView.findViewById(R.id.ingredient_6);
        tvIngredient7 = (TextView) rootView.findViewById(R.id.ingredient_7);
        tvIngredient8 = (TextView) rootView.findViewById(R.id.ingredient_8);
        tvIngredient9 = (TextView) rootView.findViewById(R.id.ingredient_9);
        tvIngredient10 = (TextView) rootView.findViewById(R.id.ingredient_10);

        tvMeasure1 = (TextView) rootView.findViewById(R.id.measure_1);
        tvMeasure2 = (TextView) rootView.findViewById(R.id.measure_2);
        tvMeasure3 = (TextView) rootView.findViewById(R.id.measure_3);
        tvMeasure4 = (TextView) rootView.findViewById(R.id.measure_4);
        tvMeasure5 = (TextView) rootView.findViewById(R.id.measure_5);
        tvMeasure6 = (TextView) rootView.findViewById(R.id.measure_6);
        tvMeasure7 = (TextView) rootView.findViewById(R.id.measure_7);
        tvMeasure8 = (TextView) rootView.findViewById(R.id.measure_8);
        tvMeasure9 = (TextView) rootView.findViewById(R.id.measure_9);
        tvMeasure10 = (TextView) rootView.findViewById(R.id.measure_10);

        tvDash1 = (TextView) rootView.findViewById(R.id.dash_1);
        tvDash2 = (TextView) rootView.findViewById(R.id.dash_2);
        tvDash3 = (TextView) rootView.findViewById(R.id.dash_3);
        tvDash4 = (TextView) rootView.findViewById(R.id.dash_4);
        tvDash5 = (TextView) rootView.findViewById(R.id.dash_5);
        tvDash6 = (TextView) rootView.findViewById(R.id.dash_6);
        tvDash7 = (TextView) rootView.findViewById(R.id.dash_7);
        tvDash8 = (TextView) rootView.findViewById(R.id.dash_8);
        tvDash9 = (TextView) rootView.findViewById(R.id.dash_9);
        tvDash10 = (TextView) rootView.findViewById(R.id.dash_10);

        if (getArguments() == null) {
            Log.e(LOG_TAG, "Error - Arguments are null.");
            Toast.makeText(getActivity(), "Error getting cocktail details.", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            cocktailId = getArguments().getString("cocktail_id");
            new LoadCocktailDetails().execute(cocktailId);
        }

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ImageView ivShareFab = (ImageView) rootView.findViewById(R.id.share_fab);
        ivShareFab.setImageResource(R.drawable.ic_add_white);

        ivShareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                Utilities.saveCocktailId(getContext(), cocktailId);
                Log.d(LOG_TAG, "Cocktail added to saved list.");
                Toast.makeText(getActivity(), "Cocktail added to saved list.", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    private class LoadCocktailDetails extends AsyncTask<String, Integer, ArrayList<Drink>> {

        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected ArrayList<Drink> doInBackground(String... params) {

            String url = "http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + params[0];

            // http://stackoverflow.com/questions/1395551/convert-a-json-string-to-object-in-java
            ArrayList<Drink> drinksList = JsonHandler.getJSONFromUrl(url);
            return drinksList;
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> drinksList) {

            if (drinksList == null) {
                // No drink
                Log.e(LOG_TAG, "Unable to get cocktail details.");
            } else {
                Drink cocktail = drinksList.get(0);
                tvName.setText(cocktail.getStrDrink());
                tvByline.setText(cocktail.getStrAlcoholic() + " - " + cocktail.getStrCategory());

                if (cocktail.getStrDrinkThumb() == "" || cocktail.getStrDrinkThumb() == null){
                    Picasso.with(getContext()).load(getRandomImage()).into(ivPhotoBackground);
                } else {
                    Picasso.with(getContext()).load(cocktail.getStrDrinkThumb()).into(ivPhotoBackground);
                }

                tvMethod.setText(cocktail.getStrInstructions());
                ivCocktailIcon.setImageResource(R.drawable.ic_cocktail_martini_grey);

                tvIngredient1.setText(cocktail.getStrIngredient1());
                if (cocktail.getStrIngredient1() == null || cocktail.getStrIngredient1() == "") {
                    setHidden(1);
                }

                tvIngredient2.setText(cocktail.getStrIngredient2());
                if (cocktail.getStrIngredient2() == null || cocktail.getStrIngredient2() == "") {
                    setHidden(2);
                }

                tvIngredient3.setText(cocktail.getStrIngredient3());
                if (cocktail.getStrIngredient3() == null || cocktail.getStrIngredient3() == "") {
                    setHidden(3);
                }

                tvIngredient4.setText(cocktail.getStrIngredient4());
                if (cocktail.getStrIngredient4() == null || cocktail.getStrIngredient4() == "") {
                    setHidden(4);
                }

                tvIngredient5.setText(cocktail.getStrIngredient5());
                if (cocktail.getStrIngredient5() == null || cocktail.getStrIngredient5() == "") {
                    setHidden(5);
                }

                tvIngredient6.setText(cocktail.getStrIngredient6());
                if (cocktail.getStrIngredient6() == null || cocktail.getStrIngredient6() == "") {
                    setHidden(6);
                }

                tvIngredient7.setText(cocktail.getStrIngredient7());
                if (cocktail.getStrIngredient7() == null || cocktail.getStrIngredient7() == "") {
                    setHidden(7);
                }

                tvIngredient8.setText(cocktail.getStrIngredient8());
                if (cocktail.getStrIngredient8() == null || cocktail.getStrIngredient8() == "") {
                    setHidden(8);
                }

                tvIngredient9.setText(cocktail.getStrIngredient9());
                if (cocktail.getStrIngredient9() == null || cocktail.getStrIngredient9() == "") {
                    setHidden(9);
                }

                tvIngredient10.setText(cocktail.getStrIngredient10());
                if (cocktail.getStrIngredient10() == null || cocktail.getStrIngredient10() == "") {
                    setHidden(10);
                }

                tvMeasure1.setText(cocktail.getStrMeasure1());
                if (cocktail.getStrMeasure1() == null || cocktail.getStrMeasure1() == "") {
                    setHidden(1);
                }

                tvMeasure2.setText(cocktail.getStrMeasure2());
                if (cocktail.getStrMeasure2() == null || cocktail.getStrMeasure2() == "") {
                    setHidden(2);
                }

                tvMeasure3.setText(cocktail.getStrMeasure3());
                if (cocktail.getStrMeasure3() == null || cocktail.getStrMeasure3() == "") {
                    setHidden(3);
                }

                tvMeasure4.setText(cocktail.getStrMeasure4());
                if (cocktail.getStrMeasure4() == null || cocktail.getStrMeasure4() == "") {
                    setHidden(4);
                }

                tvMeasure5.setText(cocktail.getStrMeasure5());
                if (cocktail.getStrMeasure5() == null || cocktail.getStrMeasure5() == "") {
                    setHidden(5);
                }

                tvMeasure6.setText(cocktail.getStrMeasure6());
                if (cocktail.getStrMeasure6() == null || cocktail.getStrMeasure6() == "") {
                    setHidden(6);
                }

                tvMeasure7.setText(cocktail.getStrMeasure7());
                if (cocktail.getStrMeasure7() == null || cocktail.getStrMeasure7() == "") {
                    setHidden(7);
                }

                tvMeasure8.setText(cocktail.getStrMeasure8());
                if (cocktail.getStrMeasure8() == null || cocktail.getStrMeasure8() == "") {
                    setHidden(8);
                }

                tvMeasure9.setText(cocktail.getStrMeasure9());
                if (cocktail.getStrMeasure9() == null || cocktail.getStrMeasure9() == "") {
                    setHidden(9);
                }

                tvMeasure10.setText(cocktail.getStrMeasure10());
                if (cocktail.getStrMeasure10() == null || cocktail.getStrMeasure10() == "") {
                    setHidden(10);
                }
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

        public void setHidden(int aHidden) {
            Log.d(LOG_TAG, "Setting hidden views: " + aHidden);
            switch (aHidden) {
                case 1:
                    tvIngredient1.setVisibility(View.GONE);
                    tvDash1.setVisibility(View.GONE);
                    tvMeasure1.setVisibility(View.GONE);
                    break;
                case 2:
                    tvIngredient2.setVisibility(View.GONE);
                    tvDash2.setVisibility(View.GONE);
                    tvMeasure2.setVisibility(View.GONE);
                    break;
                case 3:
                    tvIngredient3.setVisibility(View.GONE);
                    tvDash3.setVisibility(View.GONE);
                    tvMeasure3.setVisibility(View.GONE);
                    break;
                case 4:
                    tvIngredient4.setVisibility(View.GONE);
                    tvDash4.setVisibility(View.GONE);
                    tvMeasure4.setVisibility(View.GONE);
                    break;
                case 5:
                    tvIngredient5.setVisibility(View.GONE);
                    tvDash5.setVisibility(View.GONE);
                    tvMeasure5.setVisibility(View.GONE);
                    break;
                case 6:
                    tvIngredient6.setVisibility(View.GONE);
                    tvDash6.setVisibility(View.GONE);
                    tvMeasure6.setVisibility(View.GONE);
                    break;
                case 7:
                    tvIngredient7.setVisibility(View.GONE);
                    tvDash7.setVisibility(View.GONE);
                    tvMeasure7.setVisibility(View.GONE);
                    break;
                case 8:
                    tvIngredient8.setVisibility(View.GONE);
                    tvDash8.setVisibility(View.GONE);
                    tvMeasure8.setVisibility(View.GONE);
                    break;
                case 9:
                    tvIngredient9.setVisibility(View.GONE);
                    tvDash9.setVisibility(View.GONE);
                    tvMeasure9.setVisibility(View.GONE);
                    break;
                case 10:
                    tvIngredient10.setVisibility(View.GONE);
                    tvDash10.setVisibility(View.GONE);
                    tvMeasure10.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        public int getRandomImage(){

            Random random = new Random();
            int randomInt = random.nextInt(10);
            Log.d(LOG_TAG, "Random image set: " + randomInt);

            switch (randomInt) {
                case 1:
                    return R.drawable.cocktail_1;
                case 2:
                    return R.drawable.cocktail_2;
                case 3:
                    return R.drawable.cocktail_3;
                case 4:
                    return R.drawable.cocktail_4;
                case 5:
                    return R.drawable.cocktail_5;
                case 6:
                    return R.drawable.cocktail_6;
                case 7:
                    return R.drawable.cocktail_7;
                case 8:
                    return R.drawable.cocktail_8;
                case 9:
                    return R.drawable.cocktail_9;
                case 10:
                    return R.drawable.cocktail_10;
            }

            return R.drawable.cocktail_1;
        }
    }
}

