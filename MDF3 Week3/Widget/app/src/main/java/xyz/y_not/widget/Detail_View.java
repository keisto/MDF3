package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


public class Detail_View extends AppCompatActivity {

    public static final String EXTRA_ITEM = "xyz.y_not.widget.Detail_View.EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If App Intent
        Bundle bundle = getIntent().getBundleExtra(List_View.CONTACT_BUNDLE);
        if (bundle!=null) {
            setContentView(R.layout.activity_detail);
            Detail_Fragment fragment = new Detail_Fragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.detailContainer, fragment)
                    .commit();
        } else {
            // If Widget Intent
            setContentView(R.layout.detail_fragment);
            Intent intent = getIntent();
            Contact contact = (Contact) intent.getSerializableExtra(EXTRA_ITEM);
            if(contact == null) {
                finish();
                return;
            }

            // Display Contact

            TextView name   = (TextView) findViewById(R.id.name);
            TextView age    = (TextView) findViewById(R.id.age);
            TextView gender = (TextView) findViewById(R.id.gender);
            TextView phone  = (TextView) findViewById(R.id.phone);

            name.setText(contact.getName());
            age.setText(contact.getAge());
            gender.setText(contact.getGender());
            phone.setText(contact.getPhone());
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