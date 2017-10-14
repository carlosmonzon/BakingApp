package com.cmonzon.bakingapp.data.remote;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.RecipesDataSource;
import com.cmonzon.bakingapp.data.Step;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author cmonzon
 */

public class RecipesRemoteDataSource implements RecipesDataSource {

    private static volatile RecipesRemoteDataSource INSTANCE;

    private RecipesApi api;

    public RecipesRemoteDataSource(RecipesApi api) {
        this.api = api;
    }

    public static RecipesRemoteDataSource getInstance(RecipesApi api) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource(api);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<List<Recipe>> getRecipes() {
        return api.getRecipes().subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Ingredient>> getRecipeIngredients(int recipeId) {
        //not supported
        return null;
    }

    @Override
    public Observable<List<Step>> getRecipeSteps(int recipeId) {
        //not supported
        return null;
    }

    @Override
    public Observable<Step> getRecipeStep(int recipeId, int index) {
        //not supported
        return null;
    }

    @Override
    public void saveRecipes(List<Recipe> recipes) {
        //not supported
    }
}
