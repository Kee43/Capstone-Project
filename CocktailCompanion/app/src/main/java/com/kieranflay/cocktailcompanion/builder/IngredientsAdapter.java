package com.kieranflay.cocktailcompanion.builder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieranflay.cocktailcompanion.R;
import com.kieranflay.cocktailcompanion.Utilities;

import java.util.ArrayList;

/**
 * Created by Kieran on 02/10/2015.
 */
public class IngredientsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ingredientsName;

    public IngredientsAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.ingredients_list_view_item, names);
        this.context = context;
        this.ingredientsName = names;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CocktailListViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new CocktailListViewHolder();
            rowView = inflater.inflate(R.layout.ingredients_list_view_item, parent, false);
            holder.ingredient = (TextView) rowView.findViewById(R.id.saved_ingredient_name);
            holder.delete = (ImageView) rowView.findViewById(R.id.saved_ingredient_delete);
            rowView.setTag(holder);
        } else {
            holder = (CocktailListViewHolder) convertView.getTag();
        }

        holder.ingredient.setText(ingredientsName.get(position));
        holder.delete.setImageResource(R.drawable.ic_clear_grey);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Utilities.removeIngredient(getContext(), position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
                ingredientsName.remove(position);
                IngredientsAdapter.this.notifyDataSetChanged();
            }
        });

        return rowView;
    }

    public static class CocktailListViewHolder {

        public TextView ingredient;
        public ImageView delete;

    }
}