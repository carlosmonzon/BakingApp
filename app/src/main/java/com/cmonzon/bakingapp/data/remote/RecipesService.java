package com.cmonzon.bakingapp.data.remote;

import com.cmonzon.bakingapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import cmonzon.com.bakingapp.IdlingResources;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author cmonzon
 */

public class RecipesService {

    private static final String API_URL = "https://d17h27t6h515a5.cloudfront.net";

    private static final long CONNECT_TIMEOUT_SECS = 15;

    private static final long READ_TIMEOUT_SECS = 15;

    private RecipesApi recipesAPI;

    public RecipesService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS)
                .writeTimeout(READ_TIMEOUT_SECS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECS, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        if (BuildConfig.DEBUG) {
            IdlingResources.registerOkHttp(client);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        recipesAPI = retrofit.create(RecipesApi.class);
    }

    public RecipesApi getMoviesApi() {
        return recipesAPI;
    }
}
