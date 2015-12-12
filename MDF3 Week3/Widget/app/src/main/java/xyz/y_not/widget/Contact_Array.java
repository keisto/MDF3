package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class Contact_Array extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> objects;

    public Contact_Array(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = objects.get(position);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, null);

        TextView name   = (TextView) view.findViewById(R.id.name);
        TextView age    = (TextView) view.findViewById(R.id.age);
        TextView gender = (TextView) view.findViewById(R.id.gender);
        TextView phone  = (TextView) view.findViewById(R.id.phone);

        name.setText(contact.getName());
        age.setText(contact.getAge());
        gender.setText(contact.getGender());
        phone.setText(contact.getPhone());

        return view;
    }
}

