package com.cmonzon.bakingapp.ui.recipes;

import android.support.annotation.NonNull;

import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.RecipesDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author cmonzon
 */
public class RecipesPresenter implements RecipesContract.Presenter {

    @NonNull
    private RecipesContract.View view;

    @NonNull
    private RecipesDataSource repository;

    @NonNull
    private CompositeDisposable composite;

    public RecipesPresenter(@NonNull RecipesContract.View view, @NonNull RecipesDataSource repository) {
        this.view = view;
        this.repository = repository;
        composite = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void unSubscribe() {
        composite.clear();
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
//
//        Recipe recipeNutela = new Recipe();
//        recipeNutela.name = "Nutella Pie";
//        recipeNutela.servings = 8;
//
//        Recipe cake = new Recipe();
//        cake.name = "ellow Cake";
//        cake.servings = 12;
//
//        ArrayList<Recipe> recipes = new ArrayList<>();
//        recipes.add(recipeNutela);
//        recipes.add(cake);
//        view.showRecipes(recipes);

        view.showProgressIndicator(true);
        composite.clear();
        composite.add(repository.getRecipes().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Recipe>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Recipe> recipes) throws Exception {
                view.showProgressIndicator(false);
                view.showRecipes(new ArrayList<>(recipes));
                if (recipes.isEmpty()) {
                    view.showNoDataFound();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                view.showProgressIndicator(false);
                view.showLoadingError();
            }
        }));
    }

    @Override
    public void openRecipeDetails(Recipe recipe) {

    }
}
