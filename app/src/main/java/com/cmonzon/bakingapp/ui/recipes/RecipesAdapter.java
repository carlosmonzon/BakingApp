package com.cmonzon.bakingapp.ui.recipes;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cmonzon.bakingapp.BR;
import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * @author cmonzon
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes;

    private RecipesAdapterOnClickHandler listener;

    public RecipesAdapter(RecipesAdapterOnClickHandler listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_recipe_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Recipe recipe) {
            binding.setVariable(BR.recipe, recipe);
            binding.setVariable(BR.callback, listener);
            binding.executePendingBindings();
        }
    }

    public interface RecipesAdapterOnClickHandler {

        void onRecipeClick(Recipe recipe);
    }
}
