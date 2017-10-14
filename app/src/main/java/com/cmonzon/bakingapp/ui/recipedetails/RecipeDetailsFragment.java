package com.cmonzon.bakingapp.ui.recipedetails;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.databinding.FragmentRecipeDetailsBinding;
import com.cmonzon.bakingapp.util.StringUtils;

import java.util.ArrayList;

/**
 * @author cmonzon
 */
public class RecipeDetailsFragment extends Fragment implements RecipeDetailsContract.View, RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {

    private RecipeStepsAdapter adapter;

    FragmentRecipeDetailsBinding binding;

    private RecipeDetailsContract.Presenter presenter;

    private OnStepSelectedListener listener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance() {
        return new RecipeDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        binding.rvSteps.setLayoutManager(layoutManager);
        binding.rvSteps.setNestedScrollingEnabled(false);
        binding.rvSteps.setHasFixedSize(true);
        adapter = new RecipeStepsAdapter(this);
        binding.rvSteps.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepSelectedListener) {
            listener = (OnStepSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeDetailsFragment.OnStepSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadRecipeDetails();
    }

    //region View contract
    @Override
    public void setPresenter(RecipeDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void showIngredients(ArrayList<Ingredient> ingredients) {
        buildIngredientsList(ingredients);
    }

    @Override
    public void showSteps(ArrayList<Step> steps) {
        adapter.setSteps(steps);
    }

    @Override
    public void showStepSelected(int position) {
        if (adapter != null) {
            adapter.setSelectedItem(position);
        }
    }
    //endregion

    private void buildIngredientsList(ArrayList<Ingredient> ingredients) {
        binding.cvIngredients.tvIngredientsTitle.setText(String.format(getString(R.string.ingredients_title_format), ingredients.size()));

        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append(StringUtils.fromHtml(String.format(getString(R.string.ingredient_format), String.valueOf(ingredient.quantity), ingredient.measure, ingredient.ingredient)));
        }
        builder.substring(0, builder.length() - 1);//remove last \n
        binding.cvIngredients.tvIngredients.setText(builder.toString());
    }

    @Override
    public void onStepClick(Step step) {
        listener.onStepSelected(step);
    }

    public interface OnStepSelectedListener {
        void onStepSelected(Step step);
    }
}
