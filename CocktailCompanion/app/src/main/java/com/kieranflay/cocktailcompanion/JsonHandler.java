package com.kieranflay.cocktailcompanion;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Kieran on 30/09/2015.
 */
public class JsonHandler {

    // Reference for method: http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
    public static ArrayList<Drink> getJSONFromUrl(String url) {

        InputStream is = null;
        String json = "";
        ArrayList<Drink> drinksList;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        drinksList = new ArrayList<>();

        // http://stackoverflow.com/questions/25017847/parsing-multiple-json-object-in-jsonarray-using-gson
        JsonParseResult jsonParseResult = new Gson().fromJson(json, JsonParseResult.class);

        if (jsonParseResult != null && jsonParseResult.getResult() != null) {
            for (Drink drink : jsonParseResult.getResult()) {
                //Log.d("", "Drink: " + drink.toString());
                drinksList.add(drink);
            }
        }

        return drinksList;
    }

    public class JsonParseResult {

        @SerializedName("drinks")
        private List<Drink> drinks;

        public JsonParseResult(List<Drink> results) {
            super();
            this.drinks = results;
        }

        public List<Drink> getResult() {
            return drinks;
        }
    }

}


