package com.cmonzon.bakingapp.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

/**
 * @author cmonzon
 */

public class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment,
                                             int contentId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(contentId, fragment);
        transaction.commit();
    }

    //helper method to calculate the number of columns
    public static int numberOfColumns(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        return width / widthDivider;
    }
}
