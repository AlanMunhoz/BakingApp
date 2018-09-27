package com.devandroid.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Util.JSON;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainAdapter.ListItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String EXTRA_MAIN_ACT_RECIPE_ACT = "extra_main_act_recipe_act";

    @BindView(R.id.rv_list) RecyclerView mRvList;

    private MainAdapter mAdapter;
    private ArrayList<Recipe> lstRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.clSelectedBackground)));
        }

        try {
            lstRecipe = JSON.ParseRecipe(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setHasFixedSize(true);

        mAdapter = new MainAdapter(MainActivity.this);
        mRvList.setAdapter(mAdapter);

        /**
         * Write list with name of recipes
         */
        if(lstRecipe!=null) {
            ArrayList<String> strRecipe = new ArrayList();
            for(Recipe recipe: lstRecipe) {
                strRecipe.add(recipe.getName());
            }
            mAdapter.setListAdapter(strRecipe, -1);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class detailsActivity = RecipeActivity.class;
        Intent intent = new Intent(context, detailsActivity);

        intent.putExtra(EXTRA_MAIN_ACT_RECIPE_ACT, Parcels.wrap(lstRecipe.get(clickedItemIndex)));
        startActivity(intent);
    }
}
