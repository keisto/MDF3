package com.tonykeiser.widget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class DetailsActivity extends Activity {
    public static final String EXTRA_ITEM = "com.tonykeiser.widget.DetailsActivity.EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If App Intent
        Bundle bundle = getIntent().getBundleExtra(WidgetActivity.ASSET_BUNDLE);
        if (bundle!=null) {
            setContentView(R.layout.activity_other);
            DetailFragment frag = new DetailFragment();
            frag.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, frag)
                    .commit();
        } else {
            // If Widget Intent
            setContentView(R.layout.fragment_details);
            Intent intent = getIntent();
            Asset asset = (Asset) intent.getSerializableExtra(EXTRA_ITEM);
            if(asset == null) {
                finish();
                return;
            }
            // Display Asset
            TextView name  = (TextView) findViewById(R.id.nameText);
            TextView title = (TextView) findViewById(R.id.titleText);
            TextView rate  = (TextView) findViewById(R.id.rateText);
            name.setText(asset.getName());
            title.setText(asset.getTitle());
            rate.setText(asset.getRate());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}