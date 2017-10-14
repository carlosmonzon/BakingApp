package com.cmonzon.bakingapp.ui.stepdetails;


import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.databinding.FragmentStepDetailBinding;
import com.cmonzon.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * @author cmonzon
 */
public class StepDetailFragment extends Fragment implements StepDetailsContract.View, ExoPlayer.EventListener {

    private static final String VIDEO_LAST_POSITION = "video_last_position";

    private static final String VIDEO_IS_READY = "video_is_ready";

    private static final String STEP_INDEX = "step_index";

    private static final String MULTI_PANE = "multi_pane";

    FragmentStepDetailBinding binding;

    StepDetailsContract.Presenter presenter;

    SimpleExoPlayer exoPlayer;

    private MediaSessionCompat mediaSession;

    private PlaybackStateCompat.Builder stateBuilder;

    private boolean isVideoReady = true;

    private long videoLastPosition;

    private int stepIndex;

    private boolean multipane = false;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(int stepIndex, boolean multipane) {
        Bundle bundle = new Bundle();
        bundle.putInt(StepDetailActivity.STEP_INDEX, stepIndex);
        bundle.putBoolean(MULTI_PANE, multipane);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        binding.stepper.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepDetailFragment.this.stepIndex -= 1;
                showStepDetails(stepIndex);
            }
        });
        binding.stepper.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepDetailFragment.this.stepIndex += 1;
                showStepDetails(stepIndex);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMediaSession();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            videoLastPosition = savedInstanceState.getLong(VIDEO_LAST_POSITION, 0);
            isVideoReady = savedInstanceState.getBoolean(VIDEO_IS_READY, true);
            stepIndex = savedInstanceState.getInt(STEP_INDEX);
            multipane = savedInstanceState.getBoolean(MULTI_PANE);
        } else {
            stepIndex = getArguments().getInt(StepDetailActivity.STEP_INDEX);
            multipane = getArguments().getBoolean(MULTI_PANE);
        }
        if (multipane) {
            binding.stepper.getRoot().setVisibility(View.GONE);
        }
        if (presenter != null) {
            presenter.loadStepDetails(stepIndex);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(VIDEO_LAST_POSITION, videoLastPosition);
        outState.putBoolean(VIDEO_IS_READY, isVideoReady);
        outState.putInt(STEP_INDEX, stepIndex);
        outState.putBoolean(MULTI_PANE, multipane);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void setPresenter(StepDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStepDetails(Step step) {
        binding.setStep(step);
    }

    @Override
    public void showVideoPlayerFullScreen() {
        // Expand video, hide description, hide system UI
        binding.playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.playerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.thumbnail.setVisibility(View.GONE);
        binding.tvStepDescription.setVisibility(View.GONE);
        binding.stepper.getRoot().setVisibility(View.GONE);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void startVideoSource(String videoUrl) {
        Uri mediaUri = Uri.parse(videoUrl);
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            binding.playerView.setPlayer(exoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            exoPlayer.addListener(this);
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.seekTo(videoLastPosition);
        exoPlayer.setPlayWhenReady(isVideoReady);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !multipane) {
            showVideoPlayerFullScreen();
        }
    }

    @Override
    public void showStepsProgress(int progress) {
        binding.stepper.stepsProgress.setProgress(progress);
    }

    @Override
    public void showControlSection(boolean isBackEnable, boolean isNextEnable) {
        binding.stepper.btnBack.setVisibility(isBackEnable ? View.VISIBLE : View.INVISIBLE);
        binding.stepper.btnNext.setVisibility(isNextEnable ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showStepDetails(int stepIndex) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        videoLastPosition = 0;
        isVideoReady = true;
        this.stepIndex = stepIndex;
        presenter.loadStepDetails(stepIndex);
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "StepDetailFragment");
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            videoLastPosition = exoPlayer.getCurrentPosition();
            isVideoReady = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    //region ExoPlayer callbacks
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    //endregion
}
