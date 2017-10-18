package com.cmonzon.bakingapp.ui.stepdetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cmonzon.bakingapp.data.RecipesDataSource;
import com.cmonzon.bakingapp.data.RecipesRepository;
import com.cmonzon.bakingapp.data.Step;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * @author cmonzon
 */
public class StepDetailsPresenter implements StepDetailsContract.Presenter {

    @NonNull
    StepDetailsContract.View view;

    @NonNull
    private RecipesDataSource repository;

    private int recipeId;

    private int stepsCount;

    private int stepIndex;
    @NonNull
    private CompositeDisposable composite;

    public StepDetailsPresenter(@NonNull StepDetailsContract.View view, @NonNull RecipesRepository repository, int recipeId) {
        this.view = view;
        this.repository = checkNotNull(repository);
        this.recipeId = recipeId;
        composite = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void unSubscribe() {
        composite.clear();
    }

    @Override
    public void loadStepDetails(final int stepIndex) {
//        Step step = new Step();
//        step.description = "Desription";
//        step.shortDescription = "Short desciption";
////        step.videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
//        step.thumbnailURL = "http://square.github.io/picasso/static/debug.png";

//        boolean showVideo = step.videoURL != null && !step.videoURL.isEmpty();
//        if (showVideo) {
//            view.startVideoSource(step.videoURL);
//        }

        this.stepIndex = stepIndex;
//        composite.add(repository.getRecipeStep(recipeId, stepIndex).subscribe(this));
        final String recipeName = repository.getRecipeName(recipeId);
        composite.clear();
        composite.add(repository.getRecipeSteps(recipeId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Step>>() {
            @Override
            public void accept(@NonNull List<Step> steps) throws Exception {
                StepDetailsPresenter.this.stepsCount = steps.size();
                StepDetailsPresenter.this.handleStep(steps.get(stepIndex), recipeName);
            }
        }));
    }

    private int calculateStepProgress() {
        int progress = 0;
        if (stepsCount > 0) {
            progress = (int) (((float) (stepIndex + 1) / (float) stepsCount) * 100);
        }
        return progress;
    }


    private void handleStep(@Nullable Step step, String recipeName) throws Exception {
        if (step == null) {
            return;
        }
        boolean showVideo = step.videoURL != null && !step.videoURL.isEmpty();
        if (showVideo) {
            view.startVideoSource(step.videoURL);
        }
        if (stepIndex == 0) {
            view.showControlSection(false, true);
        } else if (stepIndex + 1 == stepsCount) {
            view.showControlSection(true, false);
        } else {
            view.showControlSection(true, true);
        }
        view.showStepsProgress(calculateStepProgress());
        view.showStepDetails(step);
        view.showTitle(recipeName, step.stepIndex);
    }
}
