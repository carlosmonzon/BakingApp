package com.cmonzon.bakingapp.ui.recipedetails;

import android.support.annotation.NonNull;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.RecipesDataSource;
import com.cmonzon.bakingapp.data.Step;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author cmonzon
 */
public class RecipeDetailsPresenter implements RecipeDetailsContract.Presenter {

    private int recipeId;

    private int selectedItem;

    @NonNull
    private RecipeDetailsContract.View view;

    @NonNull
    private RecipesDataSource repository;

    @NonNull
    private CompositeDisposable composite;

    public RecipeDetailsPresenter(@NonNull RecipeDetailsContract.View view, @NonNull RecipesDataSource repository, int recipeId, int selectedItem) {
        this.repository = checkNotNull(repository);
        this.view = view;
        this.recipeId = recipeId;
        this.selectedItem = selectedItem;
        composite = new CompositeDisposable();
        this.view.setPresenter(this);
    }


    @Override
    public void unSubscribe() {
        composite.clear();
    }

    @Override
    public void loadRecipeDetails() {
        composite.clear();
        composite.add(Observable.zip(repository.getRecipeIngredients(recipeId), repository.getRecipeSteps(recipeId), new BiFunction<List<Ingredient>, List<Step>, Recipe>() {
            @Override
            public Recipe apply(@NonNull List<Ingredient> ingredients, @NonNull List<Step> steps) throws Exception {
                Recipe recipe = new Recipe();
                recipe.ingredients = ingredients;
                recipe.steps = steps;
                return recipe;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Recipe>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Recipe recipe) throws Exception {
                view.showRecipeDetails(recipe);
                view.showStepSelected(selectedItem);
            }
        }));
        view.showTitle(repository.getRecipeName(recipeId));
    }
}
