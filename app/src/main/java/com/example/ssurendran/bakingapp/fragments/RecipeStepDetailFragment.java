package com.example.ssurendran.bakingapp.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.models.StepModel;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ssurendran on 4/9/18.
 */

public class RecipeStepDetailFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";

    @BindView(R.id.media_container)
    FrameLayout mediaContainer;
    @BindView(R.id.step_description_tv)
    TextView stepDescriptionTv;
    Unbinder unbinder;
    @BindView(R.id.loading_tv)
    TextView loadingTv;
    @BindView(R.id.main_container_ll)
    LinearLayout mainContainerLl;
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;

    private SimpleExoPlayer mExoPlayer;

    private String recipeId;
    private String stepId;

    public static RecipeStepDetailFragment newInstance(String recipeId, String stepId) {
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, recipeId);
        args.putString(STEP_ID, stepId);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        recipeId = getArguments().getString(RECIPE_ID);
        stepId = getArguments().getString(STEP_ID);

        fetchStepDetails();

        return view;
    }

    private void populateUI(StepModel stepModel) {
        stepDescriptionTv.setText(stepModel.getDescription());

        String videoUrlString = stepModel.getVideoUrlString();
        String imageUrlString = stepModel.getThumbnailUrlString();

        if (TextUtils.isEmpty(videoUrlString) && isVideoFile(imageUrlString)) {
            videoUrlString = imageUrlString;
        }

        if (!TextUtils.isEmpty(videoUrlString)) {
            playerView.setVisibility(View.VISIBLE);
            thumbnail.setVisibility(View.GONE);

            initializePlayer(Uri.parse(videoUrlString));

        } else if (!TextUtils.isEmpty(imageUrlString)) {
            playerView.setVisibility(View.GONE);
            thumbnail.setVisibility(View.VISIBLE);

            Picasso.with(getActivity())
                    .load(Uri.parse(imageUrlString))
                    .placeholder(R.drawable.default_recipe_image)
                    .error(R.drawable.default_recipe_image)
                    .into(thumbnail);

        } else {
            mediaContainer.setVisibility(View.GONE);
        }
    }

    public static boolean isVideoFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("video");
        }
        return false;
    }


    private void fetchStepDetails() {
        new AsyncTask<Void, Void, StepModel>() {

            @Override
            protected void onPreExecute() {
                loadingTv.setVisibility(View.VISIBLE);
                mainContainerLl.setVisibility(View.GONE);

                loadingTv.setText(R.string.getting_details);
            }

            @Override
            protected StepModel doInBackground(Void... voids) {

                String SELECTION_STRING = RecipeContract.StepsTableColumns.COLUMN_RECIPE_ID + "=? AND " + RecipeContract.StepsTableColumns.COLUMN_STEP_ID + "=?";
                Cursor stepsCursor = getContext().getContentResolver().query(RecipeProvider.STEPS.STEPS_CONTENT_URI, null, SELECTION_STRING, new String[]{recipeId, stepId}, null, null);

                if (stepsCursor != null && stepsCursor.moveToNext()) {
                    StepModel stepModel = new StepModel();
                    stepModel.setId(stepId);
                    stepModel.setThumbnailUrlString(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_THUMBNAIL_URL_STRING)));
                    stepModel.setShortDescription(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION)));
                    stepModel.setDescription(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_DESCRIPTION)));
                    stepModel.setVideoUrlString(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_VIDEO_URL_STRING)));
                    return stepModel;
                }

                return null;
            }

            @Override
            protected void onPostExecute(StepModel obtainedStepModel) {
                if (obtainedStepModel == null) {
                    loadingTv.setText(R.string.something_went_wrong_try_again);
                    return;
                }

                loadingTv.setVisibility(View.GONE);
                mainContainerLl.setVisibility(View.VISIBLE);
                populateUI(obtainedStepModel);
            }
        }.execute(null, null, null);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent))
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        releasePlayer();
        super.onDestroyView();
        unbinder.unbind();
    }
}
