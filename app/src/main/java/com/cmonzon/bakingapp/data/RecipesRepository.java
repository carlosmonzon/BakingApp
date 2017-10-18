package com.cmonzon.bakingapp.data;

import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author cmonzon
 */

public class RecipesRepository implements RecipesDataSource {

    private static volatile RecipesRepository INSTANCE;

    @NonNull
    private RecipesDataSource remoteDataSource;

    @NonNull
    private RecipesDataSource localDataSource;

    @Nullable
    List<Step> cachedSteps;

    private int cachedRecipeIdSteps;

    private RecipesRepository(@NonNull RecipesDataSource remoteDataSource, @NonNull RecipesDataSource localDataSource) {
        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = localDataSource;
    }

    public static RecipesRepository getInstance(@NonNull RecipesDataSource remoteDataSource,
                                                @NonNull RecipesDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        return getAndSaveRemoteRecipes();
    }

    @Override
    public Observable<Recipe> getRecipe(int recipeId) {
        return localDataSource.getRecipe(recipeId);
    }

    @Override
    public String getRecipeName(int recipeId) {
        return localDataSource.getRecipeName(recipeId);
    }

    @Override
    public Observable<List<Ingredient>> getRecipeIngredients(int recipeId) {
        return localDataSource.getRecipeIngredients(recipeId);
    }

    @Override
    public Cursor getRecipeIngredientsCursor(int recipeId) {
        return localDataSource.getRecipeIngredientsCursor(recipeId);
    }

    @Override
    public Observable<List<Step>> getRecipeSteps(int recipeId) {
        // Respond immediately with cache if available and not dirty
        if (cachedSteps != null && cachedRecipeIdSteps == recipeId) {
            return Observable.just(cachedSteps);
        }
        return getAndCacheSteps(recipeId);
    }

    private Observable<List<Step>> getAndCacheSteps(final int recipeId) {
        return localDataSource.getRecipeSteps(recipeId).doOnNext(new Consumer<List<Step>>() {
            @Override
            public void accept(@NonNull List<Step> steps) throws Exception {
                cachedSteps = steps;
                cachedRecipeIdSteps = recipeId;
            }
        });
    }

    @Override
    public Observable<Step> getRecipeStep(int recipeId, int index) {
        if (cachedSteps != null && cachedRecipeIdSteps == recipeId && index < cachedSteps.size()) {
            //get from cached steps
            return Observable.just(cachedSteps.get(index));
        } else {
            return localDataSource.getRecipeStep(recipeId, index);
        }
    }

    @Override
    public void saveRecipes(List<Recipe> recipes) {

    }

    private Observable<List<Recipe>> getAndSaveRemoteRecipes() {
        return remoteDataSource.getRecipes().doOnNext(new Consumer<List<Recipe>>() {
            @Override
            public void accept(@NonNull List<Recipe> recipes) throws Exception {
                localDataSource.saveRecipes(recipes);
            }
        });

    }
}
