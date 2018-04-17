package com.example.ssurendran.bakingapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ssurendran.bakingapp.activities.RecipeDetailActivity;
import com.example.ssurendran.bakingapp.activities.RecipeGridActivity;
import com.example.ssurendran.bakingapp.provider.RecipeProvider;

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
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ssurendran on 4/17/18.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeGridActivityTest {

    @Rule
    public IntentsTestRule<RecipeGridActivity> mActivityRule = new IntentsTestRule<>(RecipeGridActivity.class);


    @Test
    public void test_clickRecyclerViewItem_sendsIntent(){
        int position = 0;
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        intended(allOf(hasExtra(EXTRA_RECIPE_ID, String.valueOf(position+1)),
                hasComponent(RecipeDetailActivity.class.getName())));

    }

    @Test
    public void test_clickRecyclerViewItem_opensRecipeDetailActivity_showsIngredientList(){
        int position = 0;
        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        onView(withId(R.id.detail_rv)).check(matches(isDisplayed()));
    }

}
