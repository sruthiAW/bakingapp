package com.example.ssurendran.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.ssurendran.bakingapp.utils.BottomNavigationViewBehavior;
import com.example.ssurendran.bakingapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String CURRENT_STEP_ID = "current_step_id";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.step_detail_frag_container)
    FrameLayout stepDetailFragContainer;
    @BindView(R.id.prev_tv)
    TextView prevTv;
    @BindView(R.id.page_id)
    TextView pageId;
    @BindView(R.id.next_tv)
    TextView nextTv;
    @BindView(R.id.bottom_navigator)
    RelativeLayout bottomNavigator;

    private String recipeId;
    private int currentStepId;
    private int totalStepCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        if (receivedIntent.getStringExtra(Constants.EXTRA_RECIPE_NAME) != null) {
            getSupportActionBar().setTitle(receivedIntent.getStringExtra(Constants.EXTRA_RECIPE_NAME));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeId = receivedIntent.getStringExtra(Constants.EXTRA_RECIPE_ID);
        totalStepCount = receivedIntent.getIntExtra(Constants.EXTRA_STEP_COUNT, -1);

        if (savedInstanceState == null) {
            currentStepId = Integer.valueOf(receivedIntent.getStringExtra(Constants.EXTRA_STEP_ID));
        } else {
            currentStepId = savedInstanceState.getInt(CURRENT_STEP_ID);
        }

        setUpBottomNavigatorScroll();
        setUpBottomNavigationUI();
        setUpFragments(recipeId, currentStepId, false);
    }

    private void setUpBottomNavigatorScroll() {
        if(getResources().getBoolean(R.bool.isLandscape)) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigator.getLayoutParams();
            layoutParams.setBehavior(new BottomNavigationViewBehavior());
        }
    }

    private void setUpBottomNavigationUI() {
        if (currentStepId == 0) {
            prevTv.setEnabled(false);
        } else {
            prevTv.setEnabled(true);
        }

        if (currentStepId + 1 == totalStepCount) {
            nextTv.setEnabled(false);
        } else {
            nextTv.setEnabled(true);
        }

        setUpPageCount();
    }

    private void setUpPageCount() {
        if (totalStepCount != -1) {
            pageId.setText((currentStepId + 1) + "/" + totalStepCount);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_ID, currentStepId);
        super.onSaveInstanceState(outState);
    }

    private void setUpFragments(String recipeId, int stepId, boolean forceLoad) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.step_detail_frag_container) == null || forceLoad) {
            fragmentTransaction.replace(R.id.step_detail_frag_container, RecipeStepDetailFragment.newInstance(recipeId, String.valueOf(stepId)));
            fragmentTransaction.commit();
        }
    }

    @OnClick({R.id.prev_tv, R.id.next_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prev_tv:
                currentStepId -= 1;
                break;
            case R.id.next_tv:
                currentStepId += 1;
                break;
        }

        setUpBottomNavigationUI();
        setUpFragments(recipeId, currentStepId, true);
    }
}
