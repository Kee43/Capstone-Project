

/**
 * Created by Kieran on 05/10/2015.
 */
package com.kieranflay.cocktailcompanion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


// Reference for class: http://v4all123.blogspot.co.uk/2014/05/simple-navigation-drawer-example-in.html
public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> drawerTitle;
    private final ArrayList<Integer> drawerIcon;
    private int[] selectedposition;

    public NavigationDrawerAdapter(Activity context, ArrayList<String> names, ArrayList<Integer> images, int[] selectedposition) {
        super(context, R.layout.grid_view_item, names);
        this.context = context;
        this.drawerTitle = names;
        this.drawerIcon = images;
        this.selectedposition = selectedposition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        NavigationDrawerHolder holder;

        if (convertView == null) {
            holder = new NavigationDrawerHolder();
            convertView = inflater.inflate(R.layout.fragment_navigation_drawer_list_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (NavigationDrawerHolder) convertView.getTag();
        }

        holder.drawerName = (TextView) convertView.findViewById(R.id.tvDrawerName);
        holder.drawerIcon = (ImageView) convertView.findViewById(R.id.ivDrawerIcon);

        holder.drawerName.setText(drawerTitle.get(position));
        holder.drawerIcon.setImageResource(drawerIcon.get(position));

        ArrayList<Integer> images = new ArrayList<Integer>();
        images.add(R.drawable.ic_cocktail_martini_white);
        images.add(R.drawable.ic_cocktail_outing_white);
        images.add(R.drawable.ic_cocktail_tumbler_white);
        images.add(R.drawable.ic_cocktail_wine_white);
        images.add(R.drawable.ic_cocktail_shifter_white);

        if (position == selectedposition[0]) {
            holder.drawerIcon.setImageResource(images.get(position));
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_blue));
            holder.drawerName.setTextColor(Color.WHITE);
        } else {
            holder.drawerName.setTextColor(convertView.getResources().getColor(R.color.nav_grey));
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public static class NavigationDrawerHolder {

        public TextView drawerName;
        public ImageView drawerIcon;
    }
}