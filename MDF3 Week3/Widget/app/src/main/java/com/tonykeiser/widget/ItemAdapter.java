package com.tonykeiser.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class ItemAdapter extends ArrayAdapter<Asset>{
        public ItemAdapter(Context context, ArrayList<Asset> asset) {
            super(context, 0, asset);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Asset asset = (Asset) getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.asset_item, parent, false);
            }
            // Lookup view for data population
            TextView name = (TextView) convertView.findViewById(R.id.nameText);
            TextView title = (TextView) convertView.findViewById(R.id.titleText);
            TextView rate = (TextView) convertView.findViewById(R.id.rateText);
            // Populate the data into the template view using the data object
            name.setText(asset.getName());
            title.setText(asset.getTitle());
            rate.setText(asset.getRate());
            // Return the completed view to render on screen
            return convertView;
        }
}
