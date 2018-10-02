package com.devandroid.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class RecipeService extends /*IntentService {*/ RemoteViewsService {

/*
    public static final String MY_ACTION_ = "com.devandroid.bakingapp.action.my_action";
    public static final String ACTION_UPDATE_RECIPE_STEP_ = "com.devandroid.bakingapp.action.update_recipe_step";

    public RecipeService(String name) {
        super(name);
    }

    public static void startUpdateRecipeStep(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_STEP_);
        //intent.putExtra(EXTRA_PLANT_ID, plantId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (MY_ACTION_.equals(action)) {
                handleMyAction();
            } else if(ACTION_UPDATE_RECIPE_STEP_.equals(action)) {
                handleActionUpdateRecipeStep(intent);
            }
        }
    }

    private void handleMyAction() { }

    private void handleActionUpdateRecipeStep(@Nullable Intent intent) {

    }
*/



    /*
     * So pretty simple just defining the Adapter of the listview
     * here Adapter is ListProvider
     * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d("01102018", "RecipeService -> onGetViewFactory");

        ArrayList<String> lstAdapter = new ArrayList<>();
        //ArrayList<String> lstAdapter = intent.getStringArrayListExtra("ADAPTER_LIST");

        return new ListProvider(this.getApplicationContext(), intent, lstAdapter);
    }


    /*
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null)
        {
            ArrayList<String> lstAdapter = intent.getStringArrayListExtra("ADAPTER_LIST");


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppProvider.class));
            BakingAppProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, mIngredients);
        }
    }
    */
}
