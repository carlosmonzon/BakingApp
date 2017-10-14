package com.cmonzon.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmonzon.bakingapp.data.RecipesRepository;
import com.cmonzon.bakingapp.data.local.RecipesLocalDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesRemoteDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesService;
import com.cmonzon.bakingapp.ui.recipes.RecipesFragment;
import com.cmonzon.bakingapp.ui.recipes.RecipesPresenter;
import com.cmonzon.bakingapp.util.ActivityUtils;


public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RecipesFragment fragment =
                (RecipesFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            // Create the fragment
            fragment = RecipesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.container);
        }

        RecipesRepository repository = RecipesRepository.getInstance(
                RecipesRemoteDataSource.getInstance(new RecipesService().getMoviesApi()), RecipesLocalDataSource.getInstance(this));

        // Create the presenter
        new RecipesPresenter(fragment, repository);
    }
}
