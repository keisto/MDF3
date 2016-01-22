package com.tonykeiser.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class DetailFragment extends Fragment {
    Asset asset;

    public DetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) { //&& bundle.containsKey("name")
            asset = new Asset(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        if (asset !=null) {

            // Display Contact
            TextView name  = (TextView) view.findViewById(R.id.nameText);
            TextView title = (TextView) view.findViewById(R.id.titleText);
            TextView rate  = (TextView) view.findViewById(R.id.rateText);

            name.setText(asset.getName());
            title.setText(asset.getTitle());
            rate.setText(asset.getRate());
        }

        return view;
    }
}
