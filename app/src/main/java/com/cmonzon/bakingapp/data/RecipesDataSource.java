package com.cmonzon.bakingapp.data;

import android.database.Cursor;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author cmonzon
 */

public interface RecipesDataSource {

    Observable<List<Recipe>> getRecipes();

    Observable<Recipe> getRecipe(int recipeId);

    String getRecipeName(int recipeId);

    Observable<List<Ingredient>> getRecipeIngredients(int recipeId);

    Cursor getRecipeIngredientsCursor(int recipeId);

    Observable<List<Step>> getRecipeSteps(int recipeId);

    Observable<Step> getRecipeStep(int recipeId, int index);

    void saveRecipes(List<Recipe> recipes);
}
