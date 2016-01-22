package com.tonykeiser.widget;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class AddFragment extends Fragment {
    private EditText name;
    private EditText title;
    private EditText rate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        // Get Data Form
        name  = (EditText) view.findViewById(R.id.nameText);
        title = (EditText) view.findViewById(R.id.titleText);
        rate  = (EditText) view.findViewById(R.id.rateText);
        Button addAsset = (Button) view.findViewById(R.id.addAsset);
        addAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = String.valueOf(name.getText());
                String titleString = String.valueOf(title.getText());
                String rateString = String.valueOf(rate.getText());
                // Save Data
                WidgetActivity.saveData(nameString, titleString, rateString);
                // Updating ListView
                WidgetActivity.onUpdate();
                getActivity().finish();
            }
        });
        return view;
    }
}