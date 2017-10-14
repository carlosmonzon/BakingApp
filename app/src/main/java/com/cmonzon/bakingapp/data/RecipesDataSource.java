package com.cmonzon.bakingapp.data;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author cmonzon
 */

public interface RecipesDataSource {

    Observable<List<Recipe>> getRecipes();

    Observable<List<Ingredient>> getRecipeIngredients(int recipeId);

    Observable<List<Step>> getRecipeSteps(int recipeId);

    Observable<Step> getRecipeStep(int recipeId, int index);

    void saveRecipes(List<Recipe> recipes);
}
