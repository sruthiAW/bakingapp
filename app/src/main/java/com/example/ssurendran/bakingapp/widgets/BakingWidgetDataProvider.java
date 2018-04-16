package com.example.ssurendran.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ssurendran.bakingapp.preferences.BakingAppPreferences;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 4/16/18.
 */

public class BakingWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> ingredients = new ArrayList<>();
    private Context mContext;
    private Intent mIntent;

    public BakingWidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.mIntent = intent;
    }

    private void populateData() {
        ingredients.clear();
        ingredients = fetchIngredientsForFavoriteRecipe();
    }

    private List<String> fetchIngredientsForFavoriteRecipe() {
        List<String> ingredientsList = new ArrayList<>();
        BakingAppPreferences preferences = new BakingAppPreferences(mContext);
        String favoriteRecipeId = preferences.getFavoriteRecipeId();

        if (!TextUtils.isEmpty(favoriteRecipeId)) {
            String SELECTION_STRING = RecipeContract.IngredientTableColumns.COLUMN_RECIPE_ID + "=?";

            Cursor ingredientCursor = mContext.getContentResolver().query(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, null, SELECTION_STRING, new String[]{favoriteRecipeId}, null, null);
            if (ingredientCursor != null && ingredientCursor.getCount() > 0) {
                while (ingredientCursor.moveToNext()) {
                    String ingredientName = ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_NAME));
                    String ingredientQuantity = ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_QUANTITY));
                    String ingredientMeasure = ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_MEASURE));

                    StringBuilder stringBuilder = new StringBuilder(ingredientName);
                    stringBuilder.append(" - ")
                            .append(ingredientQuantity)
                            .append(" ")
                            .append(ingredientMeasure);

                    ingredientsList.add(stringBuilder.toString());
                }
            }
        }

        return ingredientsList;
    }

    @Override
    public void onCreate() {
        populateData();
    }

    @Override
    public void onDataSetChanged() {
        populateData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        remoteView.setTextViewText(android.R.id.text1, ingredients.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
