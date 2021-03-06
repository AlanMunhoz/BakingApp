package com.devandroid.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devandroid.bakingapp.Model.Ingredient;
import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Model.Step;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements MainAdapter.ListItemClickListener {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String INTRA_RECIPE_FRAG_POS = "intra_recipe_frag_pos";

    @BindView(R.id.tv_ingredients) TextView mTvIngredients;
    @BindView(R.id.rv_steps) RecyclerView mRvSteps;

    private Recipe mRecipe;
    private int mStep;
    private MainAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mStrSteps;


    public interface OnFragmentInteractionListener {
        void onItemSelected(int position);
    }

    public RecipeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(RecipeActivity.EXTRA_RECIPE_ACT_RECIPE_FRAG_OBJ));
            mStep = getArguments().getInt(RecipeActivity.EXTRA_RECIPE_ACT_RECIPE_FRAG_POS);
        }

        if(savedInstanceState!=null) {
            mStep = savedInstanceState.getInt(INTRA_RECIPE_FRAG_POS, 0);
        }

        String strIng = "";
        for(Ingredient ingredient: mRecipe.getLstIngredients()) {
            strIng += " -" + " " +
                    ingredient.getmDescription() + " " +
                    Double.toString(ingredient.getmQuantity()) + " " +
                    ingredient.getmMeasure() + "\n";
        }
        mTvIngredients.setText(strIng);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvSteps.setLayoutManager(layoutManager);
        mRvSteps.setHasFixedSize(true);

        mAdapter = new MainAdapter(RecipeFragment.this);
        mRvSteps.setAdapter(mAdapter);

        /**
         * Write list with name of steps
         */
        if(mRecipe!=null) {
            mStrSteps = new ArrayList();
            for(Step step: mRecipe.getLstSteps()) {
                mStrSteps.add(Integer.toString(step.getmId()) + ". " + step.getmShortDescription());
            }
            if(savedInstanceState!=null) {
                mAdapter.setListAdapter(mStrSteps, mStep);
            } else {
                mAdapter.setListAdapter(mStrSteps, 0);
            }
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INTRA_RECIPE_FRAG_POS, mStep);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        mStep = clickedItemIndex;
        mListener.onItemSelected(clickedItemIndex);
    }

    public void setItemClick(int position) {
        mStep = position;
        if(mAdapter!=null)
            mAdapter.setListAdapter(mStrSteps, position);
    }
}
