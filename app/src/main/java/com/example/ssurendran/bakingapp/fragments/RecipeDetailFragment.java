package com.example.ssurendran.bakingapp.fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class RecipeDetailFragment extends Fragment implements RecipeDetailsAdapter.SelectedPositionListener {

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    private static final String SELECTED_POSITION = "selected_item_position";
    private static final String RECYCLER_FIRST_COMPLETELY_VISIBLE_ITEM = "recycler_first_completely_visible_item";
    private static final String CURRENT_SELECTED_POSITION = "current_selected_position";

    @BindView(R.id.detail_rv)
    RecyclerView detailRecyclerView;
    @BindView(R.id.loading_tv)
    TextView loadingTextView;

    Unbinder unbinder;

    private Cursor ingredientCursor;
    private Cursor stepsCursor;
    private RecipeDetailsAdapter detailsAdapter;
    private String recipeName;
    private int selectedPosition = -1;


    public static RecipeDetailFragment newInstance(String recipeId, String recipeName) {
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, recipeId);
        args.putString(RECIPE_NAME, recipeName);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static RecipeDetailFragment newInstance(String recipeId, String recipeName, int selectedPositionInList) {
        Bundle args = new Bundle();
        args.putString(RECIPE_ID, recipeId);
        args.putString(RECIPE_NAME, recipeName);
        args.putInt(SELECTED_POSITION, selectedPositionInList);
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
        recipeName = getArguments().getString(RECIPE_NAME);
        selectedPosition = getArguments().getInt(SELECTED_POSITION, -1);

        setUpRecyclerView(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        String recipeId = getArguments().getString(RECIPE_ID);
        fetchRecipeDetailsFromDb(recipeId);
    }

    private void setUpRecyclerView(@Nullable Bundle savedInstanceState) {
        detailsAdapter = new RecipeDetailsAdapter(getActivity(), recipeName, null, null, this);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
        detailRecyclerView.setAdapter(detailsAdapter);

        if (savedInstanceState != null){

            int firstVisibleItem = savedInstanceState.getInt(RECYCLER_FIRST_COMPLETELY_VISIBLE_ITEM);
            selectedPosition = savedInstanceState.getInt(CURRENT_SELECTED_POSITION, -1);
            int scrollPosition = 0;

            if (getActivity().getResources().getBoolean(R.bool.isTablet) && selectedPosition > firstVisibleItem){
                scrollPosition = selectedPosition;
            } else {
                scrollPosition = firstVisibleItem;
            }
            detailRecyclerView.smoothScrollToPosition(scrollPosition);

        }
    }

    private void updateRecyclerData() {
        ((RecipeDetailsAdapter)detailRecyclerView.getAdapter()).refreshData(recipeName, ingredientCursor, stepsCursor, selectedPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECYCLER_FIRST_COMPLETELY_VISIBLE_ITEM, ((LinearLayoutManager)detailRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        super.onSaveInstanceState(outState);
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

                ingredientCursor = getActivity().getContentResolver().query(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, null, SELECTION_STRING, new String[]{recipeId}, null, null);
                stepsCursor = getActivity().getContentResolver().query(RecipeProvider.STEPS.STEPS_CONTENT_URI, null, SELECTION_STRING, new String[]{recipeId}, null, null);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (ingredientCursor == null || stepsCursor == null || ingredientCursor.getCount() == 0 || stepsCursor.getCount() == 0){
                    loadingTextView.setText(R.string.something_went_wrong_try_again);
                    return;
                }

                loadingTextView.setVisibility(View.GONE);
                detailRecyclerView.setVisibility(View.VISIBLE);

                updateRecyclerData();
            }

        }.execute(null, null, null);
    }

    @Override
    public void saveSelectedPositionInList(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
