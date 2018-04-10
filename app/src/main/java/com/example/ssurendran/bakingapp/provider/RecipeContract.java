package com.example.ssurendran.bakingapp.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by ssurendran on 4/10/18.
 */

public class RecipeContract {

    public static class RecipeTableColumns {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_NAME = "name";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_SERVINGS = "servings";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_IMAGE_URL_STRING = "image_url_string";
    }

    public static class IngredientTableColumns {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_INGREDIENT_ID = "ingredient_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_NAME = "name";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_MEASURE = "measure";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_QUANTITY = "quantity";
    }

    public static class StepsTableColumns {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_STEP_ID = "step_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_DESCRIPTION = "description";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_VIDEO_URL_STRING = "video_url_string";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_THUMBNAIL_URL_STRING = "thumbnail_url_string";
    }

}
