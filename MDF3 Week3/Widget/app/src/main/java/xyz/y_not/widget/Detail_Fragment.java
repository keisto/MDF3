package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import xyz.y_not.widget.Contact;

public class Detail_Fragment extends Fragment {
    Contact contact;

    public Detail_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Contact.CONTACT_NAME)) {
            contact = new Contact(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        if (contact !=null) {

            // Display Contact
            TextView name   = (TextView) view.findViewById(R.id.name);
            TextView age    = (TextView) view.findViewById(R.id.age);
            TextView gender = (TextView) view.findViewById(R.id.gender);
            TextView phone  = (TextView) view.findViewById(R.id.phone);

            name.setText(contact.getName());
            age.setText(contact.getAge());
            gender.setText(contact.getGender());
            phone.setText(contact.getPhone());

        }

        return view;
    }
}
