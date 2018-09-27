package com.devandroid.bakingapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.devandroid.bakingapp.Model.Recipe;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener {

    public static final String BUNDLE_DETAILS_EXTRA = "details_extra";
    public static final String BUNDLE_FRAGMENT_EXTRA = "fragment_extra";
    public static final String BUNDLE_STEP_EXTRA = "fragment_step_extra";
    public static final String BUNDLE_EXTRA_RESULT = "extra_result";
    public static final String BUNDLE_STEP_ACTIVITY = "step_activity";
    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    private Recipe mRecipe;
    private int mStep;
    private boolean bLargeScreen;
    RecipeFragment mRecipeFragment;

    @BindView(R.id.fl_fragment_list_steps) FrameLayout mFlListSteps;
    @BindView(R.id.fl_step_fragment) @Nullable FrameLayout mFlSteps;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        if(mFlSteps != null) {
            bLargeScreen = true;
        } else {
            bLargeScreen = false;
        }

        /**
         * Gets the object passed by intent
         */
        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.BUNDLE_DETAILS_EXTRA));

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getInt(BUNDLE_STEP_ACTIVITY);
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

        /**
         * Create new fragment only if there aren't any already created
         */
        if(savedInstanceState == null) {
            createUpdateFragment(true);
        } else {
            mRecipeFragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_fragment_list_steps);
            mRecipeFragment.setItemClick(mStep);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STEP_ACTIVITY, mStep);
    }

    @Override
    public void onItemSelected(int position) {

        mStep = position;

        if(!bLargeScreen) {
            Context context = RecipeActivity.this;
            Class stepActivity = StepActivity.class;
            Intent intent = new Intent(context, stepActivity);

            intent.putExtra(BUNDLE_DETAILS_EXTRA, Parcels.wrap(mRecipe));
            intent.putExtra(BUNDLE_STEP_EXTRA, position);
            startActivityForResult(intent, 1);
        } else {

            createUpdateFragment(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1) {
            if(resultCode == Activity.RESULT_OK){
                mStep = data.getIntExtra(BUNDLE_EXTRA_RESULT, 0);
                Log.d(LOG_TAG, "steps result: " + mStep);
                if(mRecipeFragment!=null) {
                    mRecipeFragment.setItemClick(mStep);
                }
            }
        }
    }

    private void createUpdateFragment(boolean bCreate) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_FRAGMENT_EXTRA, Parcels.wrap(mRecipe));
        bundle.putInt(BUNDLE_STEP_EXTRA, mStep);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(bCreate) {

            mRecipeFragment = new RecipeFragment();
            mRecipeFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.fl_fragment_list_steps, mRecipeFragment)
                    .commit();
        }

        if(bLargeScreen) {

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
}
