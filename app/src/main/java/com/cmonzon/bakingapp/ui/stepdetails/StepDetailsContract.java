package com.cmonzon.bakingapp.ui.stepdetails;

import com.cmonzon.bakingapp.data.Step;
import com.cmonzon.bakingapp.ui.BasePresenter;
import com.cmonzon.bakingapp.ui.BaseView;

/**
 * @author cmonzon
 */
public interface StepDetailsContract {


    interface View extends BaseView<Presenter> {

        void showStepDetails(Step step);

        void showVideoPlayerFullScreen();

        void showTitle(String recipeName, int stepIndex);

        void startVideoSource(String videoUrl);

        void showStepsProgress(int progress);

        void showControlSection(boolean isBackEnable, boolean isNextEnable);

        void showStepDetails(int stepIndex);

    }

    interface Presenter extends BasePresenter {

        void loadStepDetails(int stepIndex);

    }
}
