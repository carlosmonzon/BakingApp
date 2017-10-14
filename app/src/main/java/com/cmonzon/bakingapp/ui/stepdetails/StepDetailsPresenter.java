package com.cmonzon.bakingapp.ui.stepdetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cmonzon.bakingapp.data.Recipe;
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
public class StepDetailsPresenter implements StepDetailsContract.Presenter, Consumer<Step> {

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

        composite.clear();
        composite.add(repository.getRecipeSteps(recipeId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Step>>() {
            @Override
            public void accept(@NonNull List<Step> steps) throws Exception {
                StepDetailsPresenter.this.stepsCount = steps.size();
                StepDetailsPresenter.this.accept(steps.get(stepIndex));
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

    @Override
    public void accept(@Nullable Step step) throws Exception {
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
    }
}
