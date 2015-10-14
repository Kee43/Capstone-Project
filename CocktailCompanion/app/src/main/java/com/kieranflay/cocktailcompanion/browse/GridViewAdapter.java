package com.kieranflay.cocktailcompanion.browse;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kieranflay.cocktailcompanion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kieran on 29/09/2015.
 */
public class GridViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cocktailNames;
    private final ArrayList<String> cocktailImages;

    public GridViewAdapter(Activity context, ArrayList<String> names, ArrayList<String> images) {
        super(context, R.layout.grid_view_item, names);
        this.context = context;
        this.cocktailNames = names;
        this.cocktailImages = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CocktailGridViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new CocktailGridViewHolder();
            rowView = inflater.inflate(R.layout.grid_view_item, parent, false);
            holder.cocktailName = (TextView) rowView.findViewById(R.id.cocktailName);
            holder.cocktailImage = (ImageView) rowView.findViewById(R.id.cocktailImage);
            rowView.setTag(holder);
        } else {
            holder = (CocktailGridViewHolder) convertView.getTag();
        }

        holder.cocktailName.setText(cocktailNames.get(position));
        if (cocktailImages.get(position) == "") {
            Picasso.with(getContext()).load(R.drawable.splash_icon).resize(280, 280).centerCrop().into(holder.cocktailImage);
        } else {
            Picasso.with(getContext()).load(cocktailImages.get(position)).resize(280, 280).centerCrop().into(holder.cocktailImage);
        }
        return rowView;
    }

    public static class CocktailGridViewHolder {
        public TextView cocktailName;
        public ImageView cocktailImage;
    }
}