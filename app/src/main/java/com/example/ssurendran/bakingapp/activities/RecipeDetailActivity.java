package com.example.ssurendran.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.adapters.RecipeDetailsAdapter;
import com.example.ssurendran.bakingapp.fragments.RecipeDetailFragment;
import com.example.ssurendran.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.ssurendran.bakingapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailsAdapter.FragmentChanger {

    private static final String CURRENT_STEP_ID = "current_recipe_id";
    private static final String CURRENT_SELECTED_POSITION = "current_selected_position";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String currentRecipeId;
    private String currentStepId;
    private int selectedPositionInList = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        if (receivedIntent.getStringExtra(Constants.EXTRA_RECIPE_NAME) != null) {
            getSupportActionBar().setTitle(receivedIntent.getStringExtra(Constants.EXTRA_RECIPE_NAME));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null){
            currentStepId = savedInstanceState.getString(CURRENT_STEP_ID);
            selectedPositionInList = savedInstanceState.getInt(CURRENT_SELECTED_POSITION, -1);
        }

        setUpFragments();
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
        if (currentRecipeId != null && currentStepId != null){
            outState.putString(CURRENT_STEP_ID, currentStepId);
        }
        if (selectedPositionInList !=  -1){
            outState.putInt(CURRENT_SELECTED_POSITION, selectedPositionInList);
        }
        super.onSaveInstanceState(outState);
    }

    private void setUpFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (!getResources().getBoolean(R.bool.isTablet)) {
            fragmentTransaction.replace(R.id.detail_frag_container, RecipeDetailFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_RECIPE_ID),
                    getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME)));
            fragmentTransaction.commit();
        } else {

            if (selectedPositionInList == -1) {
                fragmentTransaction.replace(R.id.detail_frag_container, RecipeDetailFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_RECIPE_ID),
                        getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME)));
            } else {
                fragmentTransaction.replace(R.id.detail_frag_container, RecipeDetailFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_RECIPE_ID),
                        getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME), selectedPositionInList));
            }

            if (currentStepId != null) {
                fragmentTransaction.replace(R.id.step_detail_frag_container, RecipeStepDetailFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_RECIPE_ID),
                        currentStepId));
            } else {
                fragmentTransaction.replace(R.id.step_detail_frag_container, RecipeStepDetailFragment.newInstance());
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    public void changeStepDetailFragment(String recipeId, String stepId) {

        currentRecipeId = recipeId;
        currentStepId = stepId;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_detail_frag_container, RecipeStepDetailFragment.newInstance(recipeId, stepId));
        fragmentTransaction.commit();
    }

    @Override
    public void saveSelectedPositionInList(int selectedPosition) {
        selectedPositionInList = selectedPosition;
    }
}
