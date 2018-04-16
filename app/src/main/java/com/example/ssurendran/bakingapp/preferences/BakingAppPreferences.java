package com.example.ssurendran.bakingapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ssurendran on 4/16/18.
 */

public class BakingAppPreferences {

    private static final String FAVORITE_RECIPE_ID = "favorite_recipe_id";
    private static final String FAVORITE_RECIPE_NAME = "favorite_recipe_name";

    private SharedPreferences sharedPreferences;

    public BakingAppPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor edit() {
        return sharedPreferences.edit();
    }

    public void setFavoriteRecipeId(String recipeId) {
        edit().putString(FAVORITE_RECIPE_ID, recipeId).commit();
    }

    public String getFavoriteRecipeId() {
        return sharedPreferences.getString(FAVORITE_RECIPE_ID, "");
    }

    public void setFavoriteRecipeName(String recipeName) {
        edit().putString(FAVORITE_RECIPE_NAME, recipeName).commit();
    }

    public String getFavoriteRecipeName() {
        return sharedPreferences.getString(FAVORITE_RECIPE_NAME, "");
    }
}
