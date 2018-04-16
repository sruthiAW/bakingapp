package com.example.ssurendran.bakingapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.adapters.RecipeListAdapter;
import com.example.ssurendran.bakingapp.models.IngredientModel;
import com.example.ssurendran.bakingapp.models.RecipeModel;
import com.example.ssurendran.bakingapp.models.StepModel;
import com.example.ssurendran.bakingapp.network.RequestsBuilder;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;
import com.example.ssurendran.bakingapp.receivers.ConnectivityReceiver;
import com.example.ssurendran.bakingapp.utils.ItemOffsetDecoration;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeGridActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.no_content)
    TextView noContentTextView;
    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    private List<RecipeModel> mRecipeList;
    private RequestsBuilder mRequestsBuilder;
    private RecipeListAdapter mRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_grid);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mRequestsBuilder = new RequestsBuilder(this);

        setUpRecyclerView();

        fetchRecipeList();

    }

    private void setUpRecyclerView() {
        mRecipeListAdapter = new RecipeListAdapter(this, new ArrayList<RecipeModel>());
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_span_count))); //TODO: change span based on device size (in dimen file)
        recipeRecyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.grid_spacing));
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(mRecipeListAdapter);
    }

    private void updateRecyclerView(List<RecipeModel> recipeList) {
        mRecipeList = recipeList;
        mRecipeListAdapter.refreshData(recipeList);
    }

    private void fetchRecipeList() {
        new AsyncTask<Void, Void, List<RecipeModel>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                noContentTextView.setVisibility(View.VISIBLE);
                recipeRecyclerView.setVisibility(View.GONE);

                noContentTextView.setText(R.string.please_wait_while_we_load);
            }

            @Override
            protected List<RecipeModel> doInBackground(Void... voids) {
                if (!mRequestsBuilder.isNetworkAvailable()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noContentTextView.setText(R.string.no_internet_msg);
                        }
                    });
                    return null;
                }

                try {
                    mRecipeList = mRequestsBuilder.makeRecipeListRequest();
                    return mRecipeList;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<RecipeModel> recipeList) {
                if (recipeList == null && mRequestsBuilder.isNetworkAvailable()) {
                    noContentTextView.setText(R.string.error_try_again_msg);
                    return;
                } else if (recipeList != null && recipeList.size() == 0) {
                    noContentTextView.setText(R.string.no_recipes_found);
                    return;
                } else if (recipeList == null) {
                    return;
                }

                recipeRecyclerView.setVisibility(View.VISIBLE);
                noContentTextView.setVisibility(View.GONE);

                updateRecyclerView(recipeList);
                storeRecipesInDb();
            }
        }.execute(null, null, null);
    }

    private void storeRecipesInDb(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                getContentResolver().delete(RecipeProvider.RECIPES.RECIPES_CONTENT_URI, null, null);
                getContentResolver().delete(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, null, null);
                getContentResolver().delete(RecipeProvider.STEPS.STEPS_CONTENT_URI, null, null);

                for (RecipeModel recipeModel : mRecipeList) {
                    ContentValues newRecipe = new ContentValues();
                    newRecipe.put(RecipeContract.RecipeTableColumns.COLUMN_ID, recipeModel.getId());
                    newRecipe.put(RecipeContract.RecipeTableColumns.COLUMN_NAME, recipeModel.getName());
                    newRecipe.put(RecipeContract.RecipeTableColumns.COLUMN_SERVINGS, recipeModel.getServings());
                    newRecipe.put(RecipeContract.RecipeTableColumns.COLUMN_IMAGE_URL_STRING, recipeModel.getImageUrlString());
                    getContentResolver().insert(RecipeProvider.RECIPES.RECIPES_CONTENT_URI, newRecipe);

                    for (int i = 0; i < recipeModel.getIngredientsList().size(); i++){
                        IngredientModel ingredientModel = recipeModel.getIngredientsList().get(i);
                        ContentValues newIngredient = new ContentValues();
                        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_INGREDIENT_ID, i);
                        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_RECIPE_ID, recipeModel.getId());
                        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_NAME, ingredientModel.getName());
                        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_MEASURE, ingredientModel.getMeasure());
                        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_QUANTITY, ingredientModel.getQuantity());
                        getContentResolver().insert(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, newIngredient);
                    }

                    for (StepModel stepModel : recipeModel.getStepsList()){
                        ContentValues newStep = new ContentValues();
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_STEP_ID, stepModel.getId());
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_RECIPE_ID, recipeModel.getId());
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION, stepModel.getShortDescription());
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_DESCRIPTION, stepModel.getDescription());
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_VIDEO_URL_STRING, stepModel.getVideoUrlString());
                        newStep.put(RecipeContract.StepsTableColumns.COLUMN_THUMBNAIL_URL_STRING, stepModel.getThumbnailUrlString());
                        getContentResolver().insert(RecipeProvider.STEPS.STEPS_CONTENT_URI, newStep);
                    }

                }
                return null;
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected() {
        if (mRecipeList == null){
            fetchRecipeList();
        }
    }

}
