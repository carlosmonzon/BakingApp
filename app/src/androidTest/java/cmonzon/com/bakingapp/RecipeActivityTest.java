package cmonzon.com.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.cmonzon.bakingapp.R;
import com.cmonzon.bakingapp.RecipeActivity;
import com.cmonzon.bakingapp.ui.recipedetails.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * @author cmonzon
 */

public class RecipeActivityTest {

    private static final String RECIPE_NAME = "Nutella Pie";

    private static final int EXTRA_RECIPE_ID_VALUE = 1;

    @Rule
    public IntentsTestRule<RecipeActivity> intentsTestRule = new IntentsTestRule<>(RecipeActivity.class);

    @Test
    public void showRecipes_errorMessageIsHide() {
        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_error_message_display)).check(matches(not(isDisplayed())));
    }

    @Test
    public void showRecipes_openRecipeDetailActivity() {
        onView(withId(R.id.rv_recipes)).check(matches(hasDescendant(withText(RECIPE_NAME))));
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnRecipe_validateIntentExtra() {
        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasExtra(RecipeDetailsActivity.RECIPE_ID, EXTRA_RECIPE_ID_VALUE));
    }
}
