package com.devandroid.bakingapp.Retrofit;

import android.util.Log;

import com.devandroid.bakingapp.Model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String LOG_TAG = RetrofitClient.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    public static final String FILE_NAME = "baking.json";

    private listReceivedListenter mListReceivedListenter;

    public interface listReceivedListenter {
        void listReceived(ArrayList<Recipe> lstRecipe);
    }

    public RetrofitClient(listReceivedListenter listener) {
        mListReceivedListenter = listener;
    }

    public void getRequestRecipes() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                try {
                    ArrayList<Recipe> lstRecipe = response.body();
                    mListReceivedListenter.listReceived(lstRecipe);
                    Log.d(LOG_TAG, "Load successful with " + lstRecipe.size() + " elements!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, "Retrofit load error");
            }
        });

    }

}
