package com.kieranflay.cocktailcompanion.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;
import com.kieranflay.cocktailcompanion.browse.MainActivity;

public class SplashActivity extends AppCompatActivity {

    CircularProgressView pbCocktails;
    TextView tvAppName;
    TextView tvSetupLabel;

    private String LOG_TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // https://github.com/rahatarmanahmed/CircularProgressView
        pbCocktails = (CircularProgressView) findViewById(R.id.pvLoadCocktails);
        pbCocktails.setThickness(10);
        pbCocktails.setColor(getResources().getColor(R.color.white));

        tvAppName = (TextView) findViewById(R.id.tvAppName);
        tvSetupLabel = (TextView) findViewById(R.id.tvSetupLabel);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Ubuntu-Medium.ttf");
        tvAppName.setText("Cocktail Companion");
        tvAppName.setTypeface(typeface);

        if (Utilities.checkDatabase(getApplicationContext()) == true) {
            Log.d(LOG_TAG, "Database already exists, skipping splash page.");
            pbCocktails.setVisibility(View.GONE);
            tvSetupLabel.setVisibility(View.GONE);
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
        } else {
            Log.d(LOG_TAG, "Database does not exist, performing initial setup.");
            pbCocktails.setVisibility(View.VISIBLE);
            tvSetupLabel.setVisibility(View.VISIBLE);
            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                new SaveLocalDatabase().execute();
            } else {
                Log.e(LOG_TAG, "No internet connection");
                Toast.makeText(this, R.string.no_internet_splash, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SaveLocalDatabase extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return Utilities.saveDatabase(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pbCocktails.resetAnimation();
            if (result) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
            } else {
                Log.e(LOG_TAG, "Error - Unable to save cocktails in shared preferences.");
            }
        }

        @Override
        protected void onPreExecute() {
            pbCocktails.startAnimation();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
