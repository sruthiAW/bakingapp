package com.example.ssurendran.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ssurendran.bakingapp.activities.RecipeDetailActivity;
import com.example.ssurendran.bakingapp.activities.RecipeStepDetailActivity;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_ID;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_NAME;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_STEP_ID;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ssurendran on 4/17/18.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mActivityRule = new IntentsTestRule<>(RecipeDetailActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        createMockIngredientEntry();
        createMockStepEntry();
        launchActivity();
    }

    @Test
    public void test_clickRecyclerViewItem_intentSent_ActivityStarted(){

        int position = 1;
        onView(withId(R.id.detail_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        if (!mActivityRule.getActivity().getResources().getBoolean(R.bool.isTablet)) {
            intended(allOf(hasExtra(EXTRA_STEP_ID, String.valueOf(position - 1)),
                    hasExtra(EXTRA_RECIPE_ID, "1"),
                    hasComponent(RecipeStepDetailActivity.class.getName())));

            onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));
        }

    }

    @Test
    public void test_clickRecyclerViewItem_RecipeStepDetailActivityStarted(){

        int position = 1;
        onView(withId(R.id.detail_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        if (!mActivityRule.getActivity().getResources().getBoolean(R.bool.isTablet)) {

            onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));
        }

    }

    private void launchActivity(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE_ID, "1");
        intent.putExtra(EXTRA_RECIPE_NAME, "Nutella Pie");

        mActivityRule.launchActivity(intent);
    }

    private void createMockIngredientEntry(){
        ContentValues newIngredient = new ContentValues();
        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_INGREDIENT_ID, 0);
        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_RECIPE_ID, 1);
        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_NAME, "Salt");
        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_MEASURE, "TBSP");
        newIngredient.put(RecipeContract.IngredientTableColumns.COLUMN_QUANTITY, "2");
        InstrumentationRegistry.getTargetContext().getContentResolver().insert(RecipeProvider.INGREDIENTS.INGREDIENTS_CONTENT_URI, newIngredient);
    }

    private void createMockStepEntry(){
        ContentValues newStep = new ContentValues();
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_STEP_ID, 0);
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_RECIPE_ID, 1);
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION, "Intro");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_DESCRIPTION, "Intro");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_VIDEO_URL_STRING, "");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_THUMBNAIL_URL_STRING, "");
        InstrumentationRegistry.getTargetContext().getContentResolver().insert(RecipeProvider.STEPS.STEPS_CONTENT_URI, newStep);
    }

}
