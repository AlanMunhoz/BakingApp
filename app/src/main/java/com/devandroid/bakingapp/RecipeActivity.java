package com.devandroid.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.devandroid.bakingapp.Model.Ingredient;
import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Util.Preferences;
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
    public static final String INTRA_RECIPE_ACT_RECIPEINDEX = "intra_recipe_act_recipeindex";


    @BindView(R.id.fl_fragment_list_steps) FrameLayout mFlListSteps;
    @BindView(R.id.ivImageRecipe) @Nullable ImageView mImageRecipe;
    @BindView(R.id.fl_step_fragment) @Nullable FrameLayout mFlSteps;

    private Recipe mRecipe;
    private ArrayList<Recipe> mLstRecipe;
    private int mStep;
    private int mRecipeIndex;
    private boolean bLargeScreen;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.next_menu:
                navigateRecipe(id);
                return true;

            case R.id.previous_menu:
                navigateRecipe(id);
                return true;

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
        mLstRecipe = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.EXTRA_MAIN_ACT_RECIPE_ACT_OBJ));
        mRecipeIndex = getIntent().getIntExtra(MainActivity.EXTRA_MAIN_ACT_RECIPE_ACT_POS, 0);
        mRecipe = mLstRecipe.get(mRecipeIndex);

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getInt(INTRA_RECIPE_ACT_POS);
            mRecipeIndex = savedInstanceState.getInt(INTRA_RECIPE_ACT_RECIPEINDEX);
            mRecipe = mLstRecipe.get(mRecipeIndex);
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

            setBackgroundImage();

            final Toolbar toolbar = findViewById(R.id.MyToolbar);
            toolbar.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
            collapsingToolbarLayout.setTitle(mRecipe.getName());

            collapsingToolbarLayout.setExpandedTitleColor(getColor(R.color.clLightText));
            collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(R.color.clLightText));
            collapsingToolbarLayout.setContentScrimColor(getColor(R.color.clSelectedBackground));

        }


        /**
         * Create new fragment only if there aren't any already created
         */
        if(savedInstanceState == null) {
            createUpdateRecipeFragment(true);
            createUpdateStepFragment(true);
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
        outState.putInt(INTRA_RECIPE_ACT_RECIPEINDEX, mRecipeIndex);
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

            createUpdateStepFragment(false);
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

    private void createUpdateRecipeFragment(boolean bCreate) {

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

        } else {

            bundle.putParcelable(EXTRA_RECIPE_ACT_RECIPE_FRAG_OBJ, Parcels.wrap(mRecipe));
            bundle.putInt(EXTRA_RECIPE_ACT_RECIPE_FRAG_POS, mStep);

            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_list_steps, recipeFragment)
                    .commit();
        }
    }

    private void createUpdateStepFragment(boolean bCreate) {

        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();

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

    private void setBackgroundImage() {

        if(mRecipe.getName().equalsIgnoreCase(getResources().getResourceEntryName(R.drawable.nutella_pie).replace("_", " "))) {
            mImageRecipe.setImageResource(R.drawable.nutella_pie);
        } else if(mRecipe.getName().equalsIgnoreCase(getResources().getResourceEntryName(R.drawable.brownies).replace("_", " "))) {
            mImageRecipe.setImageResource(R.drawable.brownies);
        } else if(mRecipe.getName().equalsIgnoreCase(getResources().getResourceEntryName(R.drawable.yellow_cake).replace("_", " "))) {
            mImageRecipe.setImageResource(R.drawable.yellow_cake);
        } else if(mRecipe.getName().equalsIgnoreCase(getResources().getResourceEntryName(R.drawable.cheesecake).replace("_", " "))) {
            mImageRecipe.setImageResource(R.drawable.cheesecake);
        }
    }

    private void navigateRecipe(int command) {

        switch (command) {

            case R.id.next_menu:
                if(mRecipeIndex<mLstRecipe.size()-1) {
                    mRecipeIndex++;
                    mRecipe = mLstRecipe.get(mRecipeIndex);
                    mStep=0;
                    createUpdateRecipeFragment(false);
                    createUpdateStepFragment(false);
                    if(bLargeScreen) {
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(mRecipe.getName());
                    } else {
                        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
                        collapsingToolbarLayout.setTitle(mRecipe.getName());
                        setBackgroundImage();
                    }
                }
                break;

            case R.id.previous_menu:
                if(mRecipeIndex>0) {
                    mRecipeIndex--;
                    mRecipe = mLstRecipe.get(mRecipeIndex);
                    mStep=0;
                    createUpdateRecipeFragment(false);
                    createUpdateStepFragment(false);
                    if(bLargeScreen) {
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(mRecipe.getName());
                    } else {
                        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);
                        collapsingToolbarLayout.setTitle(mRecipe.getName());
                        setBackgroundImage();
                    }
                }
                break;
        }
    }

    private void startWidgetService()
    {

        ArrayList<String> lstIngredients = new ArrayList<>();
        for(Ingredient ingredient: mRecipe.getLstIngredients()) {
            String strLine = ingredient.getmDescription() + " " +
                    Double.toString(ingredient.getmQuantity()) + " " +
                    ingredient.getmMeasure();
            lstIngredients.add(strLine);
        }

        Preferences.saveStringList(this, lstIngredients);
        Preferences.saveStringRecipe(this, mRecipe.getName());

        RecipeService.startUpdateIngredients(this);

    }

}
