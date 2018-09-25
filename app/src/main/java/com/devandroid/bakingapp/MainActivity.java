package com.devandroid.bakingapp;

import android.content.Context;
import android.content.Intent;
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

    public static final String BUNDLE_DETAILS_EXTRA = "details_extra";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_list) RecyclerView mRvList;

    private MainAdapter mAdapter;
    ArrayList<Recipe> lstRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        try {
            lstRecipe = JSON.ParseRecipe(JSON.JSON_STRING);
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
            mAdapter.setListAdapter(strRecipe);
        }


    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        //Toast.makeText(this, lstRecipe.get(clickedItemIndex).getName(), Toast.LENGTH_SHORT).show();

        Context context = MainActivity.this;
        Class detailsActivity = RecipeActivity.class;
        Intent intent = new Intent(context, detailsActivity);

        intent.putExtra(BUNDLE_DETAILS_EXTRA, Parcels.wrap(lstRecipe.get(clickedItemIndex)));
        startActivity(intent);
    }
}
