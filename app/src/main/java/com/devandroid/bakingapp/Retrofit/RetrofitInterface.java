package com.devandroid.bakingapp.Retrofit;

import com.devandroid.bakingapp.Model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET(RetrofitClient.FILE_NAME)
    Call<ArrayList<Recipe>> getRecipes();

}