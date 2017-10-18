package com.cmonzon.bakingapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author cmonzon
 */

public final class PreferencesUtils {

    private static final String RECIPE_ID = "recipe_id";

    public static boolean saveRecipePinned(Context context, int recipeId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(RECIPE_ID, recipeId);
        return editor.commit();
    }

    public static int getRecipePinned(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(RECIPE_ID, -1);
    }
}
