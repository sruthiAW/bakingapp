package com.example.ssurendran.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ssurendran.bakingapp.R;

/**
 * Created by ssurendran on 4/9/18.
 */

public class RecipeStepDetailFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";

    public static RecipeStepDetailFragment newInstance(String recipeId) {
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, recipeId);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail_layout, container, false);
        return view;
    }


}
