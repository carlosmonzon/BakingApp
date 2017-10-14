package com.cmonzon.bakingapp.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.Step;

import io.reactivex.annotations.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author cmonzon
 */

public final class DbMapper {

    public static String getSelectAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String getSelectByIdQuery(String tableName, String column) {
        return "SELECT * FROM " + tableName + " WHERE " + column + " = ?";
    }

    @NonNull
    public static Recipe getRecipe(@NonNull Cursor c) {
        Recipe recipe = new Recipe();
        recipe.id = c.getInt(c.getColumnIndex(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_ID));
        recipe.name = c.getString(c.getColumnIndex(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_NAME));
        recipe.servings = c.getInt(c.getColumnIndex(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_SERVINGS));
        recipe.image = c.getString(c.getColumnIndex(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_IMAGE));
        return recipe;
    }

    @NonNull
    public static ContentValues recipeToContentValues(Recipe recipe) {
        checkNotNull(recipe);
        ContentValues values = new ContentValues();
        values.put(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.id);
        values.put(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe.name);
        values.put(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_SERVINGS, recipe.servings);
        values.put(RecipesModelContract.RecipeEntry.COLUMN_RECIPE_IMAGE, recipe.image);
        return values;
    }

    @NonNull
    public static Ingredient getIngredient(@NonNull Cursor c) {
        Ingredient ingredient = new Ingredient();
        ingredient.ingredient = c.getString(c.getColumnIndex(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_NAME));
        ingredient.measure = c.getString(c.getColumnIndex(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE));
        ingredient.quantity = c.getDouble(c.getColumnIndex(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_QTY));
        return ingredient;
    }

    @NonNull
    public static ContentValues ingredientToContentValues(Ingredient ingredient, int recipeId) {
        checkNotNull(ingredient);
        ContentValues values = new ContentValues();
        values.put(RecipesModelContract.IngredientEntry.COLUMN_RECIPE_ID, recipeId);
        values.put(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredient.measure);
        values.put(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.ingredient);
        values.put(RecipesModelContract.IngredientEntry.COLUMN_INGREDIENT_QTY, ingredient.quantity);
        return values;
    }

    @NonNull
    public static Step getStep(@NonNull Cursor c) {
        Step step = new Step();
        step.id = c.getInt(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_STEP_ID));
        step.recipeId = c.getInt(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_RECIPE_ID));
        step.shortDescription = c.getString(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_STEP_SHORT_DESC));
        step.description = c.getString(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_STEP_DESCRIPTION));
        step.thumbnailURL = c.getString(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_STEP_THUMB_URL));
        step.videoURL = c.getString(c.getColumnIndex(RecipesModelContract.StepEntry.COLUMN_STEP_VIDEO_URL));
        return step;
    }

    @NonNull
    public static ContentValues stepToContentValues(Step step, int recipeId) {
        checkNotNull(step);
        ContentValues values = new ContentValues();
        values.put(RecipesModelContract.StepEntry.COLUMN_RECIPE_ID, recipeId);
        values.put(RecipesModelContract.StepEntry.COLUMN_STEP_ID, step.id);
        values.put(RecipesModelContract.StepEntry.COLUMN_STEP_SHORT_DESC, step.shortDescription);
        values.put(RecipesModelContract.StepEntry.COLUMN_STEP_DESCRIPTION, step.description);
        values.put(RecipesModelContract.StepEntry.COLUMN_STEP_THUMB_URL, step.thumbnailURL);
        values.put(RecipesModelContract.StepEntry.COLUMN_STEP_VIDEO_URL, step.videoURL);
        return values;
    }
}
