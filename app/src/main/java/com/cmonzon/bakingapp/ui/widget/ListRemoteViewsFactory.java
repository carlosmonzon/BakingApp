package com.cmonzon.bakingapp.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.text.Spanned;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.local.DbMapper;
import com.cmonzon.bakingapp.data.local.PreferencesUtils;
import com.cmonzon.bakingapp.data.local.RecipesLocalDataSource;
import com.cmonzon.bakingapp.util.StringUtils;

/**
 * @author cmonzon
 */

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private RecipesLocalDataSource localRepository;

    private Context context;

    private Cursor cursor;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
        localRepository = RecipesLocalDataSource.getInstance(context);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        int recipeId = PreferencesUtils.getRecipePinned(context);
        if (cursor != null) {
            cursor.close();
        }
        cursor = localRepository.getRecipeIngredientsCursor(recipeId);
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToPosition(i);
        Ingredient ingredient = DbMapper.getIngredient(cursor);
        RemoteViews ingredientView = new RemoteViews(context.getPackageName(), R.layout.ingredient_item_widget);
        Spanned ingredientSpan = StringUtils.fromHtml(String.format(context.getString(R.string.ingredient_format_single_line),
                String.valueOf(ingredient.quantity), ingredient.measure, ingredient.ingredient));
        ingredientView.setTextViewText(R.id.tvIngredients, ingredientSpan);
        return ingredientView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
