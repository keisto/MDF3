package com.tonykeiser.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteFactory(getApplicationContext());
    }
}
