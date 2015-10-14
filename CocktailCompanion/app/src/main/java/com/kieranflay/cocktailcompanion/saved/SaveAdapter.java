package com.kieranflay.cocktailcompanion.saved;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieranflay.cocktailcompanion.Drink;
import com.kieranflay.cocktailcompanion.JsonHandler;
import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;
import com.kieranflay.cocktailcompanion.browse.GridViewAdapter;
import com.kieranflay.cocktailcompanion.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kieran on 02/10/2015.
 */
public class SaveAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cocktailNames;
    private final ArrayList<String> cocktailImages;

    private String LOG_TAG = SaveAdapter.class.getSimpleName();

    public SaveAdapter(Activity context, ArrayList<String> names, ArrayList<String> images) {
        super(context, R.layout.save_list_view_item, names);
        this.context = context;
        this.cocktailNames = names;
        this.cocktailImages = images;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CocktailListViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new CocktailListViewHolder();
            rowView = inflater.inflate(R.layout.save_list_view_item, parent, false);
            holder.cocktailName = (TextView) rowView.findViewById(R.id.saved_cocktail_name);
            holder.cocktailImage = (ImageView) rowView.findViewById(R.id.saved_cocktail_image);
            holder.cocktailDelete = (ImageView) rowView.findViewById(R.id.saved_cocktail_delete);
            rowView.setTag(holder);
        } else {
            holder = (CocktailListViewHolder) convertView.getTag();
        }

        holder.cocktailName.setText(cocktailNames.get(position));
        Picasso.with(getContext())
                .load(cocktailImages.get(position))
                .placeholder(R.drawable.splash_icon)
                .resize(120, 120).centerCrop()
                .into(holder.cocktailImage);
        holder.cocktailDelete.setImageResource(R.drawable.ic_clear_grey);
        holder.cocktailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(LOG_TAG, "Removing cocktail at position:"  + position);
                            Utilities.removeCocktail(getContext(), position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
                cocktailNames.remove(position);
                SaveAdapter.this.notifyDataSetChanged();
            }
        });

        return rowView;
    }

    public static class CocktailListViewHolder {

        public TextView cocktailName;
        public ImageView cocktailImage;
        public ImageView cocktailDelete;

    }
}