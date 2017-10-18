package com.cmonzon.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.RecipeActivity;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.local.PreferencesUtils;
import com.cmonzon.bakingapp.data.local.RecipesLocalDataSource;
import com.cmonzon.bakingapp.ui.recipedetails.RecipeDetailsActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class RecipesAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        int recipeId = PreferencesUtils.getRecipePinned(context);
        String recipeName = RecipesLocalDataSource.getInstance(context).getRecipeName(recipeId);
        RemoteViews rv = getIngredientListRemoteView(context, recipeName, recipeId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getIngredientListRemoteView(Context context, String recipeName, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_app_widget);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, RecipesWidgetService.class);
        views.setRemoteAdapter(R.id.ingredients, intent);
        if (recipeName != null && !recipeName.isEmpty()) {
            views.setTextViewText(R.id.recipeName, recipeName);
            Intent parent = new Intent(context, RecipeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(RecipeActivity.class);
            stackBuilder.addNextIntent(parent);
            Intent detailIntent = new Intent(context, RecipeDetailsActivity.class);
            detailIntent.putExtra(RecipeDetailsActivity.RECIPE_ID, recipeId);
            stackBuilder.addNextIntent(detailIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.recipeName, pendingIntent);
            // Handle
            views.setEmptyView(R.id.ingredients, R.id.empty_view);
        } else {
            Intent appIntent = new Intent(context, RecipeActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.empty_view, appPendingIntent);
        }

        return views;
    }
}

