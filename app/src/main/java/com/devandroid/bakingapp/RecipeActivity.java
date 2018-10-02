package com.devandroid.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;

import com.devandroid.bakingapp.Model.Ingredient;
import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Util.Preferences;
import com.devandroid.bakingapp.widget.BakingAppProvider;
import com.devandroid.bakingapp.widget.RecipeService;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String EXTRA_RECIPE_ACT_STEP_ACT_OBJ = "extra_recipe_act_step_act_obj";
    public static final String EXTRA_RECIPE_ACT_STEP_ACT_POS = "extra_recipe_act_step_act_pos";
    public static final String EXTRA_STEP_ACT_RECIPE_ACT_OBJ = "extra_step_act_recipe_act_obj";
    public static final String EXTRA_RECIPE_ACT_RECIPE_FRAG_OBJ = "extra_recipe_act_recipe_frag_obj";
    public static final String EXTRA_RECIPE_ACT_RECIPE_FRAG_POS = "extra_recipe_act_recipe_frag_pos";
    public static final String EXTRA_RECIPE_ACT_STEP_FRAG_OBJ = "extra_recipe_act_step_frag_obj";
    public static final String EXTRA_RECIPE_ACT_STEP_FRAG_POS = "extra_recipe_act_step_frag_pos";
    public static final String INTRA_RECIPE_ACT_POS = "intra_recipe_act_pos";

    @BindView(R.id.fl_fragment_list_steps) FrameLayout mFlListSteps;
    @BindView(R.id.fl_step_fragment) @Nullable FrameLayout mFlSteps;

    private Recipe mRecipe;
    private int mStep;
    private boolean bLargeScreen;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        /**
         * Gets the object passed by intent
         */
        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.EXTRA_MAIN_ACT_RECIPE_ACT));

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getInt(INTRA_RECIPE_ACT_POS);
        }

        if(mFlSteps != null) {
            bLargeScreen = true;

            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.clSelectedBackground)));
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mRecipe.getName());
            }

        } else {
            bLargeScreen = false;

            final Toolbar toolbar = findViewById(R.id.MyToolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
            collapsingToolbarLayout.setTitle(mRecipe.getName());

        }


        /**
         * Create new fragment only if there aren't any already created
         */
        if(savedInstanceState == null) {
            createUpdateFragment(true);
        } else {
            RecipeFragment frag = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fragment_list_steps);
            frag.setItemClick(mStep);
        }

        startWidgetService();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INTRA_RECIPE_ACT_POS, mStep);
    }

    @Override
    public void onItemSelected(int position) {

        mStep = position;

        if(!bLargeScreen) {
            Context context = RecipeActivity.this;
            Class stepActivity = StepActivity.class;
            Intent intent = new Intent(context, stepActivity);

            intent.putExtra(EXTRA_RECIPE_ACT_STEP_ACT_OBJ, Parcels.wrap(mRecipe));
            intent.putExtra(EXTRA_RECIPE_ACT_STEP_ACT_POS, position);
            startActivityForResult(intent, 1);
        } else {

            createUpdateFragment(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1) {
            if(resultCode == Activity.RESULT_OK){
                mStep = data.getIntExtra(EXTRA_STEP_ACT_RECIPE_ACT_OBJ, 0);

                RecipeFragment frag = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fragment_list_steps);
                frag.setItemClick(mStep);

            }
        }
    }

    private void createUpdateFragment(boolean bCreate) {

        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(bCreate) {

            bundle.putParcelable(EXTRA_RECIPE_ACT_RECIPE_FRAG_OBJ, Parcels.wrap(mRecipe));
            bundle.putInt(EXTRA_RECIPE_ACT_RECIPE_FRAG_POS, mStep);

            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.fl_fragment_list_steps, recipeFragment)
                    .commit();
        }

        if(bLargeScreen) {

            bundle.putParcelable(EXTRA_RECIPE_ACT_STEP_FRAG_OBJ, Parcels.wrap(mRecipe));
            bundle.putInt(EXTRA_RECIPE_ACT_STEP_FRAG_POS, mStep);

            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);

            if (bCreate) {
                fragmentManager.beginTransaction()
                        .add(R.id.fl_step_fragment, stepFragment)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fl_step_fragment, stepFragment)
                        .commit();
            }
        }
    }

    void startWidgetService()
    {

        String strIng = "";
        ArrayList<String> lstIngredients = new ArrayList<>();
        for(Ingredient ingredient: mRecipe.getLstIngredients()) {
            String strLine = ingredient.getmDescription() + " " +
                    Double.toString(ingredient.getmQuantity()) + " " +
                    ingredient.getmMeasure();
            strIng += " -" + " " + strLine + "\n";
            lstIngredients.add(strLine);
        }



        Log.d("01102018", "RecipeActivity -> startWidgetService");
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.baking_app_provider);
        view.setTextViewText(R.id.appwidget_text, mRecipe.getName());

        ComponentName theWidget = new ComponentName(this, BakingAppProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        manager.updateAppWidget(theWidget, view);


        Preferences.saveStringList(this, lstIngredients);





    }

}
