package com.cmonzon.bakingapp.ui.recipedetails;

import android.support.annotation.NonNull;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.RecipesDataSource;
import com.cmonzon.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
//        Ingredient ingredient = new Ingredient();
//        ingredient.quantity = 2;
//        ingredient.measure = "CUP";
//        ingredient.ingredient = "Graham Cracker crumbs";
//
//        Ingredient ingredient2 = new Ingredient();
//        ingredient2.quantity = 6;
//        ingredient2.measure = "TBLSP";
//        ingredient2.ingredient = "unsalted butter, melted";
//
//        Ingredient ingredient3 = new Ingredient();
//        ingredient3.quantity = 0.5;
//        ingredient3.measure = "CUP";
//        ingredient3.ingredient = "ranulated sugar";
//
//        ArrayList<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(ingredient);
//        ingredients.add(ingredient2);
//        ingredients.add(ingredient3);
//        view.showIngredients(ingredients);
//
//        Step step1 = new Step();
//        step1.shortDescription = "Recipe Introduction";
//        step1.videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4";
//
//        Step step2 = new Step();
//        step2.shortDescription = "1. Preheat the oven to 350°F. Butter the bottoms and sides of two 9\\\" round pans with 2\\\"-high sides. Cover the bottoms of the pans with rounds of parchment paper, and butter the paper as well.";
//
//        Step step3 = new Step();
//        step3.shortDescription = "combine dry ingredients.";
//        step3.videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4";
//
//
//        Step step4 = new Step();
//        step4.shortDescription = "Recipe Introduction";
//        step4.videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4";
//
//        Step step5 = new Step();
//        step5.shortDescription = "1. Preheat the oven to 350°F. Butter the bottoms and sides of two 9\\\" round pans with 2\\\"-high sides. Cover the bottoms of the pans with rounds of parchment paper, and butter the paper as well.";
//
//        Step step6 = new Step();
//        step6.shortDescription = "combine dry ingredients.";
//        step6.videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4";
//
//        ArrayList<Step> steps = new ArrayList<>();
//        steps.add(step1);
//        steps.add(step2);
//        steps.add(step3);
//        steps.add(step4);
//        steps.add(step5);
//        steps.add(step6);
//        view.showSteps(steps);

        composite.clear();
        composite.add(repository.getRecipeIngredients(recipeId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Ingredient>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Ingredient> ingredients) throws Exception {
                view.showIngredients(new ArrayList<>(ingredients));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        }));

        composite.add(repository.getRecipeSteps(recipeId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Step>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Step> steps) throws Exception {
                view.showSteps(new ArrayList<>(steps));
                view.showStepSelected(selectedItem);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        }));
        view.showTitle(repository.getRecipeName(recipeId));
    }

    @Override
    public void openRecipeStepDetail(Step[] steps) {

    }
}
