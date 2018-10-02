package com.devandroid.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devandroid.bakingapp.Model.Recipe;
import com.devandroid.bakingapp.Model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String LOG_TAG = StepFragment.class.getSimpleName();

    /**
     * intent/bundle
     */
    public static final String INTRA_STEP_FRAG_TIMEVIDEO = "intra_step_frag_timevideo";

    @BindView(R.id.tv_step) TextView mTvStep;
    @BindView(R.id.ep_player_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_none_video) ImageView mNoneVideo;

    private Recipe mRecipe;
    private int mStep;
    private Step mActualStep;
    private SimpleExoPlayer mExoPlayer;
    private long mLastTimeVideo = 0;

    public StepFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            if(getActivity() instanceof StepActivity) {
                mRecipe = Parcels.unwrap(getArguments().getParcelable(StepActivity.EXTRA_STEP_ACT_STEP_FRAG_OBJ));
                mStep = getArguments().getInt(StepActivity.EXTRA_STEP_ACT_STEP_FRAG_POS);
            } else {
                mRecipe = Parcels.unwrap(getArguments().getParcelable(RecipeActivity.EXTRA_RECIPE_ACT_STEP_FRAG_OBJ));
                mStep = getArguments().getInt(RecipeActivity.EXTRA_RECIPE_ACT_STEP_FRAG_POS);
            }
        }

        if(savedInstanceState!=null) {
            mLastTimeVideo = savedInstanceState.getLong(INTRA_STEP_FRAG_TIMEVIDEO, 0);
        }


        String strStep;
        mActualStep = mRecipe.getLstSteps().get(mStep);
        strStep = Integer.toString(mActualStep.getmId()) + ". " + mActualStep.getmShortDescription();
        mTvStep.setText(strStep);

        // Initialize the player.
        if(mActualStep.getmVideoUrl().isEmpty()) {
            mNoneVideo.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            mNoneVideo.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            //Log.d("27092018", "width: " + getActivity().findViewById(R.id.fl_step_fragment).getWidth());
            //mPlayerView.setMinimumHeight(getActivity().findViewById(R.id.fl_step_fragment).getWidth());
            initializePlayer(Uri.parse(mActualStep.getmVideoUrl()));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(INTRA_STEP_FRAG_TIMEVIDEO, mLastTimeVideo);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(Uri.parse(mActualStep.getmVideoUrl()));
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mLastTimeVideo = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            try {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);
                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(
                        mediaUri,
                        new DefaultDataSourceFactory(getActivity(), userAgent),
                        new DefaultExtractorsFactory(),
                        null,
                        null);
                mExoPlayer.seekTo(mLastTimeVideo);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void releasePlayer() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
