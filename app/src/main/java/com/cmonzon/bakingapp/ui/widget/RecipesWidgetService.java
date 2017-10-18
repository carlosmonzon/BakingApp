package com.cmonzon.bakingapp.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * @author cmonzon
 */


public class RecipesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}