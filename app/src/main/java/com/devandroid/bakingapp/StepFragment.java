package com.devandroid.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Model.Step;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String LOG_TAG = StepFragment.class.getSimpleName();

    @BindView(R.id.fl_video_container) FrameLayout mFlVideoContainer;
    @BindView(R.id.tv_step) TextView mTvStep;

    private Recipe mRecipe;
    private int mStep;

    public StepFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(StepActivity.BUNDLE_FRAGMENT_EXTRA));
            mStep = getArguments().getInt(StepActivity.BUNDLE_STEP_EXTRA);
        }

        String strStep;
        Step step = mRecipe.getLstSteps().get(mStep);
        strStep = Integer.toString(step.getmId()) + ". " + step.getmShortDescription();
        mTvStep.setText(strStep);

        return rootView;
    }
}
