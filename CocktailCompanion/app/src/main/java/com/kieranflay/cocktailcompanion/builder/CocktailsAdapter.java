package com.kieranflay.cocktailcompanion.builder;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kieran on 02/10/2015.
 */
public class CocktailsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cocktailNames;
    private final ArrayList<String> cocktailIngredients;
    private final ArrayList<Integer> cocktailIngredientsNeeded;

    public CocktailsAdapter(Activity context, ArrayList<String> names, ArrayList<String> ingredients, ArrayList<Integer> cocktailIngredientsNeeded) {
        super(context, R.layout.cocktails_list_view_item, names);
        this.context = context;
        this.cocktailNames = names;
        this.cocktailIngredients = ingredients;
        this.cocktailIngredientsNeeded = cocktailIngredientsNeeded;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CocktailListViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new CocktailListViewHolder();
            rowView = inflater.inflate(R.layout.cocktails_list_view_item, parent, false);
            holder.cocktailName = (TextView) rowView.findViewById(R.id.builderCocktailName);
            holder.cocktailIngredients = (TextView) rowView.findViewById(R.id.builderCocktailIngredients);
            rowView.setTag(holder);
        } else {
            holder = (CocktailListViewHolder) convertView.getTag();
        }

        holder.cocktailName.setText(cocktailNames.get(position));
        holder.cocktailIngredients.setText(cocktailIngredients.get(position) + " (" + Integer.toString(cocktailIngredientsNeeded.get(position)) + ") ");

        return rowView;
    }

    public static class CocktailListViewHolder {

        public TextView cocktailName;
        public TextView cocktailIngredients;
    }
}