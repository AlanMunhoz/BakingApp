package com.devandroid.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Retrofit.RetrofitClient;
import com.devandroid.bakingapp.Util.JSON;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainAdapter.ListItemClickListener, RetrofitClient.listReceivedListenter {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String EXTRA_MAIN_ACT_RECIPE_ACT = "extra_main_act_recipe_act";

    @BindView(R.id.rv_list) RecyclerView mRvList;

    private MainAdapter mAdapter;
    private ArrayList<Recipe> mLstRecipe;
    private RetrofitClient mRetrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.clSelectedBackground)));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(layoutManager);
        mRvList.setHasFixedSize(true);

        mAdapter = new MainAdapter(MainActivity.this);
        mRvList.setAdapter(mAdapter);


        try {
            mLstRecipe = JSON.ParseRecipe(JSON.getStringFromFile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Write list with name of recipes
        if(mLstRecipe!=null) {
            ArrayList<String> strRecipe = new ArrayList();
            for(Recipe recipe: mLstRecipe) {
                strRecipe.add(recipe.getName());
            }
            mAdapter.setListAdapter(strRecipe, -1);
        }

        /*
        mRetrofitClient = new RetrofitClient(this);
        mRetrofitClient.getRequestRecipes();
        */
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MainActivity.this;
        Class detailsActivity = RecipeActivity.class;
        Intent intent = new Intent(context, detailsActivity);

        intent.putExtra(EXTRA_MAIN_ACT_RECIPE_ACT, Parcels.wrap(mLstRecipe.get(clickedItemIndex)));
        startActivity(intent);
    }

    @Override
    public void listReceived(ArrayList<Recipe> lstRecipe) {

        mLstRecipe = lstRecipe;

        Log.d("Retrofit", "listener alive");
        /**
         * Write list with name of recipes
         */
        if(lstRecipe!=null) {
            ArrayList<String> strRecipe = new ArrayList();
            for(Recipe recipe: mLstRecipe) {
                strRecipe.add(recipe.getName());
            }
            mAdapter.setListAdapter(strRecipe, -1);
        }
    }
}
