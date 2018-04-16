package com.example.ssurendran.bakingapp.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.adapters.RecipeDetailsAdapter;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;
import com.example.ssurendran.bakingapp.utils.VerticalSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ssurendran on 4/9/18.
 */

public class RecipeDetailFragment extends Fragment {

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";

    @BindView(R.id.detail_rv)
    RecyclerView detailRecyclerView;
    @BindView(R.id.loading_tv)
    TextView loadingTextView;

    Unbinder unbinder;

    private Cursor ingredientCursor;
    private Cursor stepsCursor;
    private RecipeDetailsAdapter detailsAdapter;
    private String recipeName;


    public static RecipeDetailFragment newInstance(String recipeId, String recipeName) {
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, recipeId);
        args.putString(RECIPE_NAME, recipeName);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String recipeId = getArguments().getString(RECIPE_ID);
        recipeName = getArguments().getString(RECIPE_NAME);

        setUpRecyclerView();
        fetchRecipeDetailsFromDb(recipeId);
    }

    private void setUpRecyclerView() {
        detailsAdapter = new RecipeDetailsAdapter(getActivity(), recipeName, null, null);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
        detailRecyclerView.setAdapter(detailsAdapter);
    }

    private void updateRecyclerData() {
        ((RecipeDetailsAdapter)detailRecyclerView.getAdapter()).refreshData(recipeName, ingredientCursor, stepsCursor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fetchRecipeDetailsFromDb(final String recipeId) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                loadingTextView.setVisibility(View.VISIBLE);
                detailRecyclerView.setVisibility(View.GONE);

                loadingTextView.setText(R.string.getting_details);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                String SELECTION_STRING = RecipeContract.IngredientTableColumns.COLUMN_RECIPE_ID + "=?";

                ingredientCursor = getContext().getContentResolver().query(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, null, SELECTION_STRING, new String[]{recipeId}, null, null);
                stepsCursor = getContext().getContentResolver().query(RecipeProvider.STEPS.STEPS_CONTENT_URI, null, SELECTION_STRING, new String[]{recipeId}, null, null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (ingredientCursor == null || stepsCursor == null){
                    loadingTextView.setText(R.string.something_went_wrong_try_again);
                    return;
                }

                loadingTextView.setVisibility(View.GONE);
                detailRecyclerView.setVisibility(View.VISIBLE);

                updateRecyclerData();
            }

        }.execute(null, null, null);
    }

}
