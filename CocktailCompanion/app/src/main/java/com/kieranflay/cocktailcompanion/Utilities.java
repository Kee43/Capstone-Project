package com.kieranflay.cocktailcompanion;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kieran on 03/10/2015.
 */
public class Utilities {

    public static ArrayList<String> alCocktailIds = new ArrayList<String>();
    public static ArrayList<String> alIngredients = new ArrayList<String>();
    public static String LOG_TAG = Utilities.class.getSimpleName();

    public static ArrayList<Drink> loadDatabase(Context aContext) {

        ArrayList<String> drinkListId = new ArrayList<>();
        ArrayList<Drink> drinkList = new ArrayList<>();

        TinyDB tinydb = new TinyDB(aContext);
        drinkListId = tinydb.getListString("local_db");

        String url = "http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";

        for (String cocktailId : drinkListId) {
            if (cocktailId == "" || cocktailId == null){
            } else {
                drinkList.add(JsonHandler.getJSONFromUrl(url + cocktailId).get(0));
            }
        }
        Log.d(LOG_TAG, "Loading in " + drinkList.size() + " cocktails.");
        return drinkList;
    }

    public static boolean checkDatabase(Context aContext) {
        ArrayList<String> drinkList = new ArrayList<>();
        TinyDB tinydb = new TinyDB(aContext);
        drinkList = tinydb.getListString("local_db");

        if (drinkList.size() == 0) {
            Log.v(LOG_TAG, "Database is empty, performing initial setup.");
            return false;
        } else {
            Log.v(LOG_TAG, "Database has records, skipping initial setup.");
            return true;
        }
    }

    public static boolean saveDatabase(Context aContext) {

        ArrayList<Drink> tempList = new ArrayList<Drink>();
        ArrayList<Drink> dbList = new ArrayList<Drink>();
        ArrayList<String> tempDrinkIdList = new ArrayList<>();
        ArrayList<String> urlList = getUrlArray();
        Set<String> filteredDrinkList = new HashSet<String>();

        TinyDB tinydb = new TinyDB(aContext);

        for (String url : urlList) {
            tempList.clear();
            tempList = JsonHandler.getJSONFromUrl(url);
            for (Drink tempDrink : tempList) {
                if (tempDrink.getStrDrinkThumb() == null || tempDrink.getStrDrinkThumb() == "") {
                    // No image
                } else {
                    dbList.add(tempDrink);
                }
            }
        }

        // We need to remove duplicates
        filteredDrinkList.clear();
        for (Drink dbDrink : dbList) {
            // Removing duplicates from the list
            filteredDrinkList.add(dbDrink.getIdDrink());
        }
        tempDrinkIdList.addAll(filteredDrinkList);

        if (tempDrinkIdList.size() == 0) {
            Log.v(LOG_TAG, "Drink list size is 0.");
            return false;
        } else {
            tinydb.putListString("local_db", tempDrinkIdList);
            Log.d(LOG_TAG, "Successfully added: " + Integer.toString(tempDrinkIdList.size()) + " entries to the local db.");
            return true;
        }
    }

    public static ArrayList<Drink> loadAlcoholicCocktails(Context aContext) {

        long start = System.currentTimeMillis();
        ArrayList<Drink> drinkList = new ArrayList<>();
        ArrayList<Drink> dbDrinks = loadDatabase(aContext);

        for (Drink drink : dbDrinks) {
            if (drink.getStrAlcoholic().equals("Alcoholic")) {
                // Ensure that no duplicates are added
                drinkList.add(drink);
            }
        }
        long end = System.currentTimeMillis();
        long total = (end-start);
        String timeTaken = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(total),
                TimeUnit.MILLISECONDS.toSeconds(total) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total))
        );
        Log.v(LOG_TAG, "Time to load alcoholic cocktails: " + timeTaken);
        return drinkList;
    }

    public static ArrayList<Drink> loadNonAlcoholicCocktails(Context aContext) {

         long start = System.currentTimeMillis();
        ArrayList<Drink> drinkList = new ArrayList<>();
        ArrayList<Drink> dbDrinks = loadDatabase(aContext);

        for (Drink drink : dbDrinks) {
            if (drink.getStrAlcoholic().equals("Non alcoholic")) {
                // Ensure that no duplicates are added
                drinkList.add(drink);
            }
        }
        long end = System.currentTimeMillis();
        long total = (end-start);
        String timeTaken = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(total),
                TimeUnit.MILLISECONDS.toSeconds(total) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total))
        );
        Log.v(LOG_TAG, "Time to load non-alcoholic cocktails: " + timeTaken);
        return drinkList;
    }

    public static ArrayList<Drink> loadSavedCocktailList(Context aContext) {

        ArrayList<String> drinkListId = new ArrayList<>();
        ArrayList<Drink> drinkListDrink = new ArrayList<Drink>();

        TinyDB tinydb = new TinyDB(aContext);
        drinkListId = tinydb.getListString("saved_cocktails");

        String url = "http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=";

        for (String cocktailId : drinkListId) {
            Drink drink = (JsonHandler.getJSONFromUrl(url + cocktailId)).get(0);
            drinkListDrink.add(drink);
        }
        Log.d(LOG_TAG, "Loading " + drinkListDrink.size() + " saved cocktails.");
        return drinkListDrink;
    }

    public static void saveCocktailId(Context aContext, String drinkId) {

        alCocktailIds.add(drinkId);
        TinyDB tinydb = new TinyDB(aContext);
        tinydb.putListString("saved_cocktails", alCocktailIds);
        Log.d(LOG_TAG, "Saved cocktail id: " + drinkId);
    }

    public static void removeCocktail(Context aContext, int pos) {

        ArrayList<Drink> tempDrinksList = new ArrayList<>();
        tempDrinksList = loadSavedCocktailList(aContext);
        tempDrinksList.remove(pos);
        updateSavedCocktailList(aContext, tempDrinksList);
        Log.d(LOG_TAG, "Removing cocktail at position " + pos);
    }

    public static void updateSavedCocktailList(Context aContext, ArrayList<Drink> drinks) {

        alCocktailIds.clear();
        for (Drink drink : drinks) {
            alCocktailIds.add(drink.getIdDrink());
        }
        TinyDB tinydb = new TinyDB(aContext);
        tinydb.putListString("saved_cocktails", alCocktailIds);
        Log.d(LOG_TAG, "Updating saved cocktail list.");
    }

    public static ArrayList<String> loadSavedIngredientList(Context aContext) {

        ArrayList<String> tempIngredientsList = new ArrayList<>();
        TinyDB tinydb = new TinyDB(aContext);
        tempIngredientsList = tinydb.getListString("saved_ingredients");

        Log.d(LOG_TAG, "Loading " + tempIngredientsList.size() + " saved ingredients.");
        return tempIngredientsList;
    }

    public static void saveIngredient(Context aContext, String ingredient) {

        alIngredients.add(ingredient);
        TinyDB tinydb = new TinyDB(aContext);
        tinydb.putListString("saved_ingredients", alIngredients);
        Log.d(LOG_TAG, "Saved ingredient: " + ingredient);
    }

    public static void removeIngredient(Context aContext, int pos) {

        ArrayList<String> tempIngredientsList = new ArrayList<>();
        TinyDB tinydb = new TinyDB(aContext);
        tempIngredientsList = tinydb.getListString("saved_ingredients");
        tempIngredientsList.remove(pos);
        updateSavedIngredientList(aContext, tempIngredientsList);
        Log.d(LOG_TAG, "Removing ingredient at position: " + pos);
    }

    public static void updateSavedIngredientList(Context aContext, ArrayList<String> ingredientsList) {

        alIngredients.clear();
        for (String ingredient : ingredientsList) {
            alIngredients.add(ingredient);
        }
        TinyDB tinydb = new TinyDB(aContext);
        tinydb.putListString("saved_ingredients", alIngredients);
        Log.d(LOG_TAG, "Updated saved ingredients list.");
    }

    public static ArrayList<String> getUrlArray() {
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=1"); // 1
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=5"); // 5
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=7"); // 7
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=a"); // a
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=c"); // c
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=e"); // e
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=f"); // f
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=j"); // j
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=m"); // m
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=n"); // n
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=o"); // o
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=q"); // q
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=w"); // w
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=y"); // y
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=z"); // z
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic"); // alcoholic
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Non_Alcoholic"); // non-alcoholic
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Shot"); // Shot
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Other/Unknown"); // Other
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Milk_/_Float_/_Shake"); // Milk/Float/Shake
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink"); // Ordinary drink
        urlList.add("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Cocktail"); // Cocktail
        for (int i=0; i < 300;i++){
            urlList.add("http://www.thecocktaildb.com/api/json/v1/1/random.php"); // Cocktail
        }
        return urlList;
    }

    //Based on a stackoverflow snippet
    public static boolean isNetworkAvailable(Context aContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
