package com.cmonzon.bakingapp.ui.recipedetails;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.ui.BasePresenter;
import com.cmonzon.bakingapp.ui.BaseView;

import java.util.ArrayList;

/**
 * @author cmonzon
 */

public interface RecipeDetailsContract {


    interface View extends BaseView<Presenter> {

        void showIngredients(ArrayList<Ingredient> ingredients);

        void showSteps(ArrayList<Step> steps);

        void showStepSelected(int position);

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {

        void loadRecipeDetails();

        void openRecipeStepDetail(Step[] steps);
    }
}
