package com.cmonzon.bakingapp.data.local;

import android.provider.BaseColumns;

/**
 * @author cmonzon
 */

public class RecipesModelContract {

    public static final class RecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_RECIPE_NAME = "name";

        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_RECIPE_IMAGE = "image";

        public static final String COLUMN_RECIPE_SERVINGS = "servings";
    }

    public static final class IngredientEntry implements BaseColumns {

        public static final String TABLE_NAME = "ingredient";

        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_INGREDIENT_NAME = "ingredient";

        public static final String COLUMN_INGREDIENT_MEASURE = "measure";

        public static final String COLUMN_INGREDIENT_QTY = "quantity";
    }

    public static final class StepEntry implements BaseColumns {

        public static final String TABLE_NAME = "step";

        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String COLUMN_STEP_ID = "step_id";

        public static final String COLUMN_STEP_SHORT_DESC = "short_description";

        public static final String COLUMN_STEP_DESCRIPTION = "description";

        public static final String COLUMN_STEP_VIDEO_URL = "videoURL";

        public static final String COLUMN_STEP_THUMB_URL = "thumbnailURL";
    }
}
