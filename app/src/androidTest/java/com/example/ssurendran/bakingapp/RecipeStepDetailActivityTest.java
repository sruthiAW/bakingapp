package com.example.ssurendran.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_ID;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_NAME;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_STEP_COUNT;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_STEP_ID;
import static org.hamcrest.Matchers.not;

/**
 * Created by ssurendran on 4/17/18.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeStepDetailActivityTest {
    @Rule
    public IntentsTestRule<RecipeStepDetailActivity> mActivityRule = new IntentsTestRule<>(RecipeStepDetailActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        createMockStepEntry();
        launchActivity();
    }

    @Test
    public void test_StepDescriptionDisplayed(){

        onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));
    }

    @Test
    public void test_ExoPlayerDisplayed(){

        onView(withId(R.id.video_view)).check(matches(isDisplayed()));
    }

    @Test
    public void test_BottomNavigatorDisplayedInPhone(){

        onView(withId(R.id.bottom_navigator)).check(matches(isDisplayed()));
    }

    @Test
    public void test_BottomNavigatorNotDisplayedInTablet(){

        if (mActivityRule.getActivity().getResources().getBoolean(R.bool.isTablet)) {
            onView(withId(R.id.bottom_navigator)).check(matches(not(isDisplayed())));
        }
    }

    private void launchActivity(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE_ID, "1");
        intent.putExtra(EXTRA_RECIPE_NAME, "Nutella Pie");
        intent.putExtra(EXTRA_STEP_ID, "0");
        intent.putExtra(EXTRA_STEP_COUNT, 1);

        mActivityRule.launchActivity(intent);
    }

    private void createMockStepEntry(){
        ContentValues newStep = new ContentValues();
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_STEP_ID, "0");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_RECIPE_ID, "1");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION, "Intro");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_DESCRIPTION, "Intro");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_VIDEO_URL_STRING,
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        newStep.put(RecipeContract.StepsTableColumns.COLUMN_THUMBNAIL_URL_STRING, "");
        InstrumentationRegistry.getTargetContext().getContentResolver().insert(RecipeProvider.STEPS.STEPS_CONTENT_URI, newStep);
    }
}
