package com.example.ssurendran.bakingapp.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by ssurendran on 4/10/18.
 */

@Database(version = RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.RecipeTableColumns.class)
    public static final String RECIPE_TABLE = "recipes_table";

    @Table(RecipeContract.IngredientTableColumns.class)
    public static final String INGREDIENT_TABLE = "ingredients_table";

    @Table(RecipeContract.StepsTableColumns.class)
    public static final String STEPS_TABLE = "steps_table";

}