package com.devandroid.bakingapp.widget;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.devandroid.bakingapp.R;
import com.devandroid.bakingapp.Util.Preferences;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class ListProvider implements RemoteViewsFactory {
    private ArrayList<String> listItemList = new ArrayList<>();
    private Context context;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent, ArrayList<String> lstAdapter) {
        this.context = context;

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        //listItemList = lstAdapter;
        populateListItem();
    }

    private void populateListItem() {
        /*
        for (int i = 0; i < 10; i++) {
            ArrayList<String> listItem = new ArrayList<>();
            listItemList.add(i + " This is the content of the app widget listview.Nice content though");
        }
        */
        listItemList = Preferences.restoreStringList(context);
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.list_row);
        String strItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.tv_row_content, strItem);

        Log.d("01102018", "ListProvider -> getViewAt");

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }
}
