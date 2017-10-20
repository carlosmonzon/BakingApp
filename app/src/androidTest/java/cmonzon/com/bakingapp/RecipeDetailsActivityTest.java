package cmonzon.com.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * @author cmonzon
 */

public class RecipeDetailsActivityTest {

    private static final String STEP_NAME_ONE = "Nutella Pie - Step 1";

    private static final String STEP_NAME_TWO = "Nutella Pie - Step 2";

    private static final String INGREDIENTS_TITLE = "9 Ingredients";

    private static final int EXTRA_RECIPE_ID_VALUE = 1;

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityRule =
            new ActivityTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetailsActivity.class);
                    result.putExtra(RecipeDetailsActivity.RECIPE_ID, EXTRA_RECIPE_ID_VALUE);
                    return result;
                }
            };

    @Test
    public void showRecipeIngredients() {
        onView(withId(R.id.tvIngredientsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.tvIngredientsTitle)).check(matches(withText(INGREDIENTS_TITLE)));
        onView(withId(R.id.tvIngredients)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnStep_showStepDetailsWithVideo() {
        onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(STEP_NAME_ONE)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.exoPlayerView))).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnStep_showStepDetailsWithoutVideo() {
        onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText(STEP_NAME_TWO)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.exoPlayerView))).check(matches(not(isDisplayed())));
    }

    @Test
    public void clickOnStepOne_goToNextStep() {
        onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(STEP_NAME_ONE)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.exoPlayerView))).check(matches(isDisplayed()));
        onView(withId(R.id.btnBack)).check(matches(not(isDisplayed())));

        //click on button next
        onView(withId(R.id.btnNext)).perform(click());
        onView(withText(STEP_NAME_TWO)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.exoPlayerView))).check(matches(not(isDisplayed())));
    }


}
