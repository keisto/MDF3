package com.tonykeiser.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class AddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        AddFragment frag = new AddFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, frag)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
