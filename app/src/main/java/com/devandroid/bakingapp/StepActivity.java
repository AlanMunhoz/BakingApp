package com.devandroid.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.devandroid.bakingapp.Model.Recipe;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    public static final String BUNDLE_FRAGMENT_EXTRA = "fragment_extra";
    public static final String BUNDLE_STEP_EXTRA = "fragment_step_extra";
    private static final String LOG_TAG = StepActivity.class.getSimpleName();
    Recipe mRecipe;
    int mStep;

    @BindView(R.id.btnBack) Button mBtnBack;
    @BindView(R.id.btnForward) Button mBtnForward;

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
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        /**
         * Gets the object passed by intent
         */
        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(RecipeActivity.BUNDLE_DETAILS_EXTRA));
        mStep = getIntent().getIntExtra(RecipeActivity.BUNDLE_STEP_EXTRA, 0);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStep>0) {
                    mStep--;

                    createUpdateFragment(false);
                }
            }
        });

        mBtnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStep<mRecipe.getLstSteps().size()-1) {
                    mStep++;
                    createUpdateFragment(false);
                }
            }
        });

        /**
         * Create new fragment only if there aren't any already created
         */
        if(savedInstanceState == null) {

            createUpdateFragment(true);
        }
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(RecipeActivity.BUNDLE_EXTRA_RESULT, mStep);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void createUpdateFragment(boolean bCreate) {

        StepFragment stepFragment = new StepFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_FRAGMENT_EXTRA, Parcels.wrap(mRecipe));
        bundle.putInt(BUNDLE_STEP_EXTRA, mStep);
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(bCreate) {
            Log.d(LOG_TAG, "Criando fragments");
            fragmentManager.beginTransaction()
                    .add(R.id.fl_step_fragment, stepFragment)
                    .commit();
        } else {
            Log.d(LOG_TAG, "Atualizando fragments");
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_step_fragment, stepFragment)
                    .commit();
        }
    }

}
