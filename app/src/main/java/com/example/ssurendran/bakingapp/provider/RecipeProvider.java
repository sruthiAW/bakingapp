package com.example.ssurendran.bakingapp.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by ssurendran on 4/10/18.
 */

@ContentProvider(authority = RecipeProvider.AUTHORITY, database = RecipeDatabase.class)
public final class RecipeProvider {

    public static final String AUTHORITY = "com.example.ssurendran.bakingapp.provider";

    @TableEndpoint(table = RecipeDatabase.RECIPE_TABLE)
    public static class RECIPES {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeContract.RecipeTableColumns.COLUMN_ID + " ASC")
        public static final Uri RECIPES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");
    }

    @TableEndpoint(table = RecipeDatabase.INGREDIENT_TABLE)
    public static class INGREDIENTS {

        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredients",
                defaultSort = RecipeContract.IngredientTableColumns.COLUMN_ID + " ASC")
        public static final Uri INGREDIENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");
    }

    @TableEndpoint(table = RecipeDatabase.STEPS_TABLE)
    public static class STEPS {

        @ContentUri(
                path = "steps",
                type = "vnd.android.cursor.dir/steps",
                defaultSort = RecipeContract.StepsTableColumns.COLUMN_ID + " ASC")
        public static final Uri STEPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/steps");
    }
}
