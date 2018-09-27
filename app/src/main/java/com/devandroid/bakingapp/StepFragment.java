package com.devandroid.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
    public static final String BUNDLE_LAST_TIME_VIDEO_EXTRA = "last_time_video";

    @BindView(R.id.tv_step) TextView mTvStep;
    @BindView(R.id.ep_player_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_none_video) ImageView mNoneVideo;

    private SimpleExoPlayer mExoPlayer;
    private Recipe mRecipe;
    private int mStep;
    private long mLastTimeVideo;

    public StepFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(StepActivity.BUNDLE_FRAGMENT_EXTRA));
            mStep = getArguments().getInt(StepActivity.BUNDLE_STEP_EXTRA);
        }

        if(savedInstanceState!=null) {
            mLastTimeVideo = savedInstanceState.getLong(BUNDLE_LAST_TIME_VIDEO_EXTRA, 0);
        }


        String strStep;
        Step step = mRecipe.getLstSteps().get(mStep);
        strStep = Integer.toString(step.getmId()) + ". " + step.getmShortDescription();
        mTvStep.setText(strStep);

        // Initialize the player.
        if(step.getmVideoUrl().isEmpty()) {
            mNoneVideo.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            mNoneVideo.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(step.getmVideoUrl()));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mLastTimeVideo = mExoPlayer.getCurrentPosition();
        outState.putLong(BUNDLE_LAST_TIME_VIDEO_EXTRA, mLastTimeVideo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
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
