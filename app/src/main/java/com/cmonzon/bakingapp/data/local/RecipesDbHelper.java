package com.cmonzon.bakingapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author cmonzon
 */

public class RecipesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "recipes.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String NOT_NULL = " NOT NULL";

    private static final String SQL_CREATE_RECIPE_TABLE =
            "CREATE TABLE " + RecipesModelContract.RecipeEntry.TABLE_NAME + " (" +
                    RecipesModelContract.RecipeEntry.COLUMN_RECIPE_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RecipesModelContract.RecipeEntry.COLUMN_RECIPE_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    RecipesModelContract.RecipeEntry.COLUMN_RECIPE_SERVINGS + INTEGER_TYPE + COMMA_SEP +
                    RecipesModelContract.RecipeEntry.COLUMN_RECIPE_IMAGE + TEXT_TYPE + COMMA_SEP +
                    "UNIQUE (" + RecipesModelContract.RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE);";

    private static final String SQL_CREATE_INGREDIENT_TABLE =
            "CREATE TABLE " + RecipesModelContract.IngredientEntry.TABLE_NAME + " (" +
                    RecipesModelContract.IngredientEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_QTY + INTEGER_TYPE + COMMA_SEP +
                    RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_NAME + TEXT_TYPE + COMMA_SEP +
                    RecipesModelContract.IngredientEntry.COLUMN_RECIPE_ID + INTEGER_TYPE + NOT_NULL + ")";


    private static final String SQL_CREATE_STEP_TABLE =
            "CREATE TABLE " + RecipesModelContract.StepEntry.TABLE_NAME + " (" +
                    RecipesModelContract.StepEntry.COLUMN_STEP_ID + INTEGER_TYPE + COMMA_SEP +
                    RecipesModelContract.StepEntry.COLUMN_RECIPE_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    RecipesModelContract.StepEntry.COLUMN_STEP_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RecipesModelContract.StepEntry.COLUMN_STEP_SHORT_DESC + TEXT_TYPE + COMMA_SEP +
                    RecipesModelContract.StepEntry.COLUMN_STEP_THUMB_URL + TEXT_TYPE + COMMA_SEP +
                    RecipesModelContract.StepEntry.COLUMN_STEP_VIDEO_URL + TEXT_TYPE + COMMA_SEP +
                    "PRIMARY KEY(" + RecipesModelContract.StepEntry.COLUMN_STEP_ID + COMMA_SEP + RecipesModelContract.StepEntry.COLUMN_RECIPE_ID + "));";

    public RecipesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        db.execSQL(SQL_CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //not required
    }
}
