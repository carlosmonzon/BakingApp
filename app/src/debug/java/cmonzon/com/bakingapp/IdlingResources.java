package cmonzon.com.bakingapp;

import android.support.test.espresso.IdlingRegistry;

import com.jakewharton.espresso.OkHttp3IdlingResource;

import okhttp3.OkHttpClient;

/**
 * @author cmonzon
 */

public abstract class IdlingResources {
    public static void registerOkHttp(OkHttpClient client) {
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create(
                "okhttp", client));
    }
}
