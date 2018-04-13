package com.example.ssurendran.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.ssurendran.bakingapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    private void setUpFragments() {
        //if phone
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.step_detail_frag_container, RecipeStepDetailFragment.newInstance(getIntent().getStringExtra(Constants.EXTRA_RECIPE_ID)));
        fragmentTransaction.commit();
    }

}
