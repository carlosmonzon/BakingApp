package com.cmonzon.bakingapp.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.cmonzon.bakingapp.R;
import com.squareup.picasso.Picasso;

/**
 * @author cmonzon
 */

public final class BindingAdapters {

    @BindingAdapter("stepImageUrl")
    public static void setStepImageUrl(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            view.setVisibility(View.VISIBLE);
            Picasso.with(view.getContext()).load(url).into(view);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("recipeImageUrl")
    public static void setRecipeImageUrl(ImageView view, String url){
        if (url != null && !url.isEmpty()) {
            Picasso.with(view.getContext()).load(url).into(view);
        } else {
            view.setImageResource(R.drawable.ic_food);
        }
    }
}
