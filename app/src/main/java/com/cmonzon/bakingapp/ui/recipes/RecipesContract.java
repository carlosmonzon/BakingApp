package com.cmonzon.bakingapp.ui.recipes;

import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.ui.BasePresenter;
import com.cmonzon.bakingapp.ui.BaseView;

import java.util.ArrayList;

/**
 * @author cmonzon
 */
public interface RecipesContract {

    interface View extends BaseView<Presenter> {

        void showRecipes(ArrayList<Recipe> recipes);

        void showProgressIndicator(boolean isVisible);

        void showLoadingError();

        void showRecipeDetails(Recipe recipe);

        void showNoDataFound();
    }

    interface Presenter extends BasePresenter {

        void loadRecipes(boolean forceUpdate);

        void openRecipeDetails(Recipe recipe);

    }
}
