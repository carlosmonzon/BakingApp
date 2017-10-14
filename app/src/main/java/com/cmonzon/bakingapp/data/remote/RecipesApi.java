package com.cmonzon.bakingapp.data.remote;

import com.cmonzon.bakingapp.data.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author cmonzon
 */

public interface RecipesApi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipes();
}
