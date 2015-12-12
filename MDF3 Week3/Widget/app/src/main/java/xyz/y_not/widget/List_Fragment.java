package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import xyz.y_not.widget.Contact;
import xyz.y_not.widget.Contact_Data;

import java.util.List;

public class List_Fragment extends ListFragment {
    List<Contact> contacts;
    private Callbacks activity;

    public List_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Contact_Array adapter = new Contact_Array(getActivity(),
                R.layout.list_item, contacts);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_widget, container, false);
        return view;
    }
    public interface Callbacks {
        void onItemSelected(Contact contact);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Contact contact = contacts.get(position);
        activity.onItemSelected(contact);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Callbacks) activity;
    }
}
