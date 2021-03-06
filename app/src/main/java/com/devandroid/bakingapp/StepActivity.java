package com.devandroid.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.devandroid.bakingapp.Model.Recipe;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepActivity extends AppCompatActivity {

    private static final String LOG_TAG = StepActivity.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String EXTRA_STEP_ACT_STEP_FRAG_OBJ = "extra_step_act_step_frag_obj";
    public static final String EXTRA_STEP_ACT_STEP_FRAG_POS = "extra_step_act_step_frag_pos";
    public static final String INTRA_STEP_ACT_POS = "intra_step_act_pos";

    @BindView(R.id.btnBack) Button mBtnBack;
    @BindView(R.id.btnForward) Button mBtnForward;

    @OnClick(R.id.btnBack) public void backClick() {
        if(mStep>0) {
            mStep--;
            createUpdateStepFragment(false);
        }
    }

    @OnClick(R.id.btnForward) public void forwardClick() {
        if(mStep<mRecipe.getLstSteps().size()-1) {
            mStep++;
            createUpdateStepFragment(false);
        }
    }

    private Recipe mRecipe;
    private int mStep;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INTRA_STEP_ACT_POS, mStep);
    }

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
        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(RecipeActivity.EXTRA_RECIPE_ACT_STEP_ACT_OBJ));
        mStep = getIntent().getIntExtra(RecipeActivity.EXTRA_RECIPE_ACT_STEP_ACT_POS, 0);

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getInt(INTRA_STEP_ACT_POS);
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.clSelectedBackground)));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

        /**
         * Create new fragment only if there aren't any already created
         */
        if(savedInstanceState == null) {

            createUpdateStepFragment(true);
        }
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(RecipeActivity.EXTRA_STEP_ACT_RECIPE_ACT_OBJ, mStep);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void createUpdateStepFragment(boolean bCreate) {

        StepFragment stepFragment = new StepFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_STEP_ACT_STEP_FRAG_OBJ, Parcels.wrap(mRecipe));
        bundle.putInt(EXTRA_STEP_ACT_STEP_FRAG_POS, mStep);
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
