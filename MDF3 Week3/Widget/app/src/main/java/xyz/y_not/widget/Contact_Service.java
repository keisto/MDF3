package xyz.y_not.widget;
// ##########################################################
// ######   Created by: TONY KEISER  MDF3 TERM: 1512   ######
// ##########################################################

import android.content.Intent;
import android.widget.RemoteViewsService;

public class Contact_Service extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Remote_Factory(getApplicationContext());
    }
}
