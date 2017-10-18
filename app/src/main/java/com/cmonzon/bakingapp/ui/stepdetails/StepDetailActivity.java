package com.cmonzon.bakingapp.ui.stepdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.RecipesRepository;
import com.cmonzon.bakingapp.data.local.RecipesLocalDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesRemoteDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesService;
import com.cmonzon.bakingapp.util.ActivityUtils;

public class StepDetailActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "RECIPE_ID";

    public static final String STEP_INDEX = "STEP_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();

        if (intent.hasExtra(RECIPE_ID) && intent.hasExtra(STEP_INDEX)) {
            int recipeId = intent.getIntExtra(RECIPE_ID, 0);
            int stepIndex = intent.getIntExtra(STEP_INDEX, 0);
            StepDetailFragment fragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_container);
            if (fragment == null) {
                // Create the fragment
                fragment = StepDetailFragment.newInstance(stepIndex, false);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.step_details_container);
            }

            RecipesRepository repository = RecipesRepository.getInstance(
                    RecipesRemoteDataSource.getInstance(new RecipesService().getMoviesApi()), RecipesLocalDataSource.getInstance(this));

            // Create the presenter
            new StepDetailsPresenter(fragment, repository, recipeId);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
