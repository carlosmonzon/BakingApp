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

import java.util.ArrayList;

/**
 * @author cmonzon
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private ArrayList<Step> steps;

    private RecipeStepsAdapterOnClickHandler listener;

    public final static int NO_ITEM_SELECTED = -1;

    private int selectedItem = NO_ITEM_SELECTED;

    public RecipeStepsAdapter(RecipeStepsAdapterOnClickHandler listener) {
        this.listener = listener;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentRecipeDetailsStepItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_recipe_details_step_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(steps.get(position));
        if (selectedItem == position) {
            holder.binding.getRoot().setBackgroundColor(ResourcesCompat.getColor(holder.binding.getRoot().getResources(), R.color.step_selected_background, null));
        } else {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = holder.binding.getRoot().getContext().obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            holder.binding.getRoot().setBackgroundResource(backgroundResource);
            typedArray.recycle();
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null) {
            return steps.size();
        }
        return 0;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FragmentRecipeDetailsStepItemBinding binding;

        public ViewHolder(FragmentRecipeDetailsStepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Step step) {
            int adapterPos = getAdapterPosition() + 1; //start at step 1 instead of 0
            step.stepIndex = adapterPos;
            binding.setVariable(BR.step, step);
            binding.setVariable(BR.stepCallback, listener);
            binding.executePendingBindings();
            if (adapterPos == 1) {
                binding.getRoot().setPadding(0, 32, 0, 0);
            } else if (adapterPos == getItemCount()) {
                binding.bottomLine.setVisibility(View.GONE);
            } else {
                binding.bottomLine.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface RecipeStepsAdapterOnClickHandler {

        void onStepClick(Step step);
    }
}
