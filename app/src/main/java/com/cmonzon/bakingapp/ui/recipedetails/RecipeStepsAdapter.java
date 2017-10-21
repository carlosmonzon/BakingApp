package com.cmonzon.bakingapp.ui.recipedetails;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmonzon.bakingapp.BR;
import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.databinding.FragmentRecipeDetailsStepItemBinding;
import com.cmonzon.bakingapp.databinding.PartialIngredientsLayoutBinding;

import java.util.ArrayList;

/**
 * @author cmonzon
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Step> steps;

    private RecipeStepsAdapterOnClickHandler listener;

    public final static int NO_ITEM_SELECTED = -1;

    private int selectedItem = NO_ITEM_SELECTED;

    private String ingredientsTitle;

    private String ingredients;

    public RecipeStepsAdapter(RecipeStepsAdapterOnClickHandler listener) {
        this.listener = listener;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == R.layout.partial_ingredients_layout) {
            PartialIngredientsLayoutBinding ingredientsLayoutBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.partial_ingredients_layout, parent, false);
            return new IngredientsViewHolder(ingredientsLayoutBinding);
        } else {
            FragmentRecipeDetailsStepItemBinding binding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.fragment_recipe_details_step_item, parent, false);
            return new ViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof IngredientsViewHolder) {
            ((IngredientsViewHolder) holder).bind(ingredientsTitle, ingredients);
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            int fixedPosition = position - 1;
            viewHolder.bind(steps.get(fixedPosition));
            if (selectedItem == fixedPosition) {
                viewHolder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(viewHolder.binding.getRoot().getResources(), R.color.step_selected_background, null));
            } else {
                int[] attrs = new int[]{R.attr.selectableItemBackground};
                TypedArray typedArray = viewHolder.binding.getRoot().getContext().obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);
                viewHolder.binding.getRoot().setBackgroundResource(backgroundResource);
                typedArray.recycle();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null) {
            //ingredients extra row
            return steps.size() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0) {
            viewType = R.layout.partial_ingredients_layout;
        } else {
            viewType = R.layout.fragment_recipe_details_step_item;
        }
        return viewType;
    }

    public void setSteps(ArrayList<Step> steps, String ingredients, String ingredientsTitle) {
        this.steps = steps;
        this.ingredients = ingredients;
        this.ingredientsTitle = ingredientsTitle;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FragmentRecipeDetailsStepItemBinding binding;

        public ViewHolder(FragmentRecipeDetailsStepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Step step) {
            int adapterPos = getAdapterPosition(); //start at step 1 instead of 0
            step.stepIndex = adapterPos;
            binding.setVariable(BR.step, step);
            binding.setVariable(BR.stepCallback, listener);
            binding.executePendingBindings();
            if (adapterPos == 1) {
                binding.getRoot().setPadding(0, 32, 0, 0);
            } else if (adapterPos == getItemCount() - 1) {
                binding.bottomLine.setVisibility(View.GONE);
            } else {
                binding.bottomLine.setVisibility(View.VISIBLE);
            }
        }
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        PartialIngredientsLayoutBinding binding;

        public IngredientsViewHolder(PartialIngredientsLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String ingredientsTitle, String ingredients) {
            binding.tvIngredients.setText(ingredients);
            binding.tvIngredientsTitle.setText(ingredientsTitle);
        }
    }

    public interface RecipeStepsAdapterOnClickHandler {

        void onStepClick(Step step);
    }
}
