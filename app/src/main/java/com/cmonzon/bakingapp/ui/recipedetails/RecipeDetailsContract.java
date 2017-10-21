package com.cmonzon.bakingapp.ui.recipedetails;

import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.ui.BasePresenter;
import com.cmonzon.bakingapp.ui.BaseView;

/**
 * @author cmonzon
 */

public interface RecipeDetailsContract {


    interface View extends BaseView<Presenter> {

        void showRecipeDetails(Recipe recipe);

        void showStepSelected(int position);

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {

        void loadRecipeDetails();
    }
}
