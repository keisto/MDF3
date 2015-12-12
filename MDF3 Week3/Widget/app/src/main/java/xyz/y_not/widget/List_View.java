package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v4.app.Fragment;

public class List_View extends AppCompatActivity
    implements List_Fragment.Callbacks {

    public static final String CONTACT_BUNDLE = "contactBundle";
    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    public void onItemSelected(Contact contact) {
        Bundle bundle = contact.toBundle();
        Intent intent = new Intent(this, Detail_View.class);
        intent.putExtra(CONTACT_BUNDLE, bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void onNewContact(View v){
        Intent intent = new Intent(this, Form_View.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE){
            // Update & Save Data
        }
    }
}
