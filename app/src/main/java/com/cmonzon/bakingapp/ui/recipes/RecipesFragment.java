package com.cmonzon.bakingapp.ui.recipes;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.databinding.FragmentRecipesBinding;
import com.cmonzon.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.cmonzon.bakingapp.util.ActivityUtils;
import com.cmonzon.bakingapp.util.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * @author cmonzon
 */
public class RecipesFragment extends Fragment implements RecipesContract.View, RecipesAdapter.RecipesAdapterOnClickHandler {

    private final static String RECIPES = "recipes";

    private RecipesAdapter adapter;

    FragmentRecipesBinding binding;

    private RecipesContract.Presenter presenter;

    private ArrayList<Recipe> recipes;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPES);
        }
        if (recipes != null) {
            showRecipes(recipes);
        } else {
            presenter.loadRecipes(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes, container, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), ActivityUtils.numberOfColumns(getActivity()));
        binding.rvRecipes.setLayoutManager(layoutManager);
        binding.rvRecipes.setHasFixedSize(false);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.grid_offset);
        binding.rvRecipes.addItemDecoration(itemDecoration);
        adapter = new RecipesAdapter(this);
        binding.rvRecipes.setAdapter(adapter);
        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadRecipes(false);
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (recipes != null) {
            outState.putParcelableArrayList(RECIPES, recipes);
        }
        super.onSaveInstanceState(outState);
    }

    //region View contract
    @Override
    public void setPresenter(RecipesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        binding.rvRecipes.setVisibility(View.VISIBLE);
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        adapter.setRecipes(recipes);
    }

    @Override
    public void showProgressIndicator(boolean isVisible) {
        binding.swipeToRefresh.setRefreshing(isVisible);
    }

    @Override
    public void showLoadingError() {
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
        binding.tvErrorMessageDisplay.setText(R.string.error_message);
        binding.rvRecipes.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void showRecipeDetails(Recipe recipe) {

    }

    @Override
    public void showNoDataFound() {
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
        binding.tvErrorMessageDisplay.setText(R.string.no_data_found);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent recipeDetailsIntent = new Intent(getActivity(), RecipeDetailsActivity.class);
        recipeDetailsIntent.putExtra(RecipeDetailsActivity.RECIPE_ID, recipe.id);
        startActivity(recipeDetailsIntent);
    }
    //endregion
}
