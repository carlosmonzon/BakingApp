package com.cmonzon.bakingapp.ui.recipedetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.data.RecipesRepository;
import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.data.local.PreferencesUtils;
import com.cmonzon.bakingapp.data.local.RecipesLocalDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesRemoteDataSource;
import com.cmonzon.bakingapp.data.remote.RecipesService;
import com.cmonzon.bakingapp.ui.recipes.RecipesAdapter;
import com.cmonzon.bakingapp.ui.stepdetails.StepDetailActivity;
import com.cmonzon.bakingapp.ui.stepdetails.StepDetailFragment;
import com.cmonzon.bakingapp.ui.stepdetails.StepDetailsPresenter;
import com.cmonzon.bakingapp.ui.widget.RecipesAppWidget;
import com.cmonzon.bakingapp.util.ActivityUtils;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepSelectedListener {

    public static final String RECIPE_ID = "RECIPE_ID";

    public static final String STEP_SELECTED_INDEX = "step_selected_index";

    private boolean isTwoPanel = false;

    private int itemSelected = RecipeStepsAdapter.NO_ITEM_SELECTED;

    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        if (savedInstanceState != null) {
            itemSelected = savedInstanceState.getInt(STEP_SELECTED_INDEX);
        }
        if (intent.hasExtra(RECIPE_ID)) {
            recipeId = intent.getIntExtra(RECIPE_ID, 0);
            if (findViewById(R.id.step_details_container) != null) {
                isTwoPanel = true;
            } else if (getSupportFragmentManager().findFragmentById(R.id.step_details_container) != null) {
                isTwoPanel = false;
                StepDetailFragment fragment =
                        (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_container);
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            setUpMultiPane(isTwoPanel, recipeId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_SELECTED_INDEX, itemSelected);
        super.onSaveInstanceState(outState);
    }

    private void setUpMultiPane(boolean isTwoPanel, int recipeId) {
        if (isTwoPanel) {
            if (itemSelected < 0) {
                itemSelected = 0;
            }
            StepDetailFragment fragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_container);
            if (fragment == null) {
                // Create the fragment
                fragment = StepDetailFragment.newInstance(itemSelected, true);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.step_details_container);
            }

            RecipesRepository repository = RecipesRepository.getInstance(
                    RecipesRemoteDataSource.getInstance(new RecipesService().getMoviesApi()), RecipesLocalDataSource.getInstance(this));

            // Create the presenter
            new StepDetailsPresenter(fragment, repository, recipeId);
        }

        RecipeDetailsFragment fragment =
                (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_container);
        if (fragment == null) {
            // Create the fragment
            fragment = RecipeDetailsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.details_container);
        }

        RecipesRepository repository = RecipesRepository.getInstance(
                RecipesRemoteDataSource.getInstance(new RecipesService().getMoviesApi()), RecipesLocalDataSource.getInstance(this));

        // Create the presenter
        new RecipeDetailsPresenter(fragment, repository, recipeId, isTwoPanel ? itemSelected : RecipeStepsAdapter.NO_ITEM_SELECTED);
    }

    @Override
    public void onStepSelected(Step step) {
        if (isTwoPanel) {
            StepDetailFragment fragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_details_container);
            if (fragment != null && itemSelected != step.stepIndex - 1) {
                fragment.showStepDetails(step.stepIndex - 1);
            }

            RecipeDetailsFragment listFragment =
                    (RecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_container);
            if (listFragment != null) {
                listFragment.showStepSelected(step.stepIndex - 1);
            }
        } else {
            Intent stepDetailsIntent = new Intent(this, StepDetailActivity.class);
            stepDetailsIntent.putExtra(StepDetailActivity.RECIPE_ID, step.recipeId);
            stepDetailsIntent.putExtra(StepDetailActivity.STEP_INDEX, step.stepIndex - 1);
            startActivity(stepDetailsIntent);
        }
        this.itemSelected = step.stepIndex - 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_pin) {
            PreferencesUtils.saveRecipePinned(this, recipeId);
            Toast.makeText(this, R.string.pin_recipe_message, Toast.LENGTH_SHORT).show();
            updateWidget();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipesAppWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients);
        //Now update all widgets
        RecipesAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
