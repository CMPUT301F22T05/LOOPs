package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Intent test for MealPlanHomePageFragment
 */

@RunWith(AndroidJUnit4.class)
public class MealPlanHomePageFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<MealPlanHomePageFragment> fragmentScenario;
    private MealPlan mockMealPlan;
    private MealPlanCollection mockMealPlanCollection;
    private Recipe mockRecipe;
    private Bundle bundle;
    private RecipeCollection mockRecipeCollection;

    @Before
    public void setUp(){
        bundle = new Bundle();
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        IngredientCollection recipeIngredients = new IngredientCollection();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap image = BitmapFactory.decodeResource(targetContext.getResources(),R.drawable.friedchicken);
        mockRecipe = new Recipe(
                "fried chicken",
                0,
                45,
                4,
                "Lunch",
                image,
                recipeIngredients,
                "Fried Chicken"
        );
        IngredientCollection mockIngredientCollection = new IngredientCollection();
        mockRecipeCollection = new RecipeCollection();
        mockRecipeCollection.addRecipe(mockRecipe);
        mockMealPlan = new MealPlan("mockMealPlan",mockIngredientCollection,mockRecipeCollection);
        mockMealPlanCollection = new MealPlanCollection();


    }
    /**
     * Launches a fragment with no arguments
     */
    private void launchFragment(){
        bundle.putSerializable("updatedMealPlan",mockMealPlan);
        fragmentScenario = FragmentScenario.launchInContainer(MealPlanHomePageFragment.class,bundle ,R.style.AppTheme);
        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.mealPlanHomePageFragment);
            Navigation.setViewNavController(fragment.requireView(),navController);
        });
    }

    /**
     * Gets arguments of the next fragment that a button in
     * MealPlanHomePageFragment navigates to
     * @return
     */
    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }

    /**
     * Launches a fragment that has receive a result that updates a meal plan
     */
    private void mimicUpdateMealPlan(){
        bundle.putSerializable("updatedMealPlan",mockMealPlan);
        bundle.putInt("mealPlanIndex", 0);
        fragmentScenario = FragmentScenario.launchInContainer(MealPlanHomePageFragment.class,bundle ,R.style.AppTheme);
        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.mealPlanHomePageFragment);
            Navigation.setViewNavController(fragment.requireView(),navController);
        });
    }

    /**
     * Assertions for the size of a list view
     * @param size size of list view
     * @return boolean
     */
    public static Matcher<View> withListSize (final int size) {
        /*
         * https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso
         * Date accessed: 11/25/2022
         */
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount() == size;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    /**
     * Test if the list view items are being initialized properly
     */
    @Test
    public void testDisplay(){
        launchFragment();
        onView(withId(R.id.meal_plan_collection)).check(matches(withListSize(21)));
        for (int i = 0; i<21; i++){
            if (i%3 == 0){
                onData(is(instanceOf(MealPlan.class))).inAdapterView(withId(R.id.meal_plan_collection))
                        .atPosition(i).check(matches(hasDescendant(allOf(withId(R.id.meal_plan_name),
                                withText(containsString("Breakfast"))))));
            }
            else if (i%3 == 1){
                onData(is(instanceOf(MealPlan.class))).inAdapterView(withId(R.id.meal_plan_collection))
                        .atPosition(i).check(matches(hasDescendant(allOf(withId(R.id.meal_plan_name),
                                withText(containsString("Lunch"))))));
            }
            else{
                onData(is(instanceOf(MealPlan.class))).inAdapterView(withId(R.id.meal_plan_collection))
                        .atPosition(i).check(matches(hasDescendant(allOf(withId(R.id.meal_plan_name),
                                withText(containsString("Supper"))))));
            }

        }
    }

    /**
     * Checks if an updated item in list view changes and if you click on an item it goes to the right
     * fragment
     */
    @Test
    public void testClickOnItemAndUpdateItem(){
        mimicUpdateMealPlan();
        onView(withId(R.id.meal_plan_collection)).check(matches(withListSize(21)));
        onData(is(instanceOf(MealPlan.class))).inAdapterView(withId(R.id.meal_plan_collection))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.meal_plan_name),
                        withText(containsString("mockMealPlan"))))));
        onData(is(instanceOf(MealPlan.class))).inAdapterView(withId(R.id.meal_plan_collection))
                .atPosition(0).perform(click());
        assertEquals(navController.getCurrentDestination().getId(),R.id.mealPlanFragment);
        Bundle returnVal = getReturnBundle();
        assertEquals(returnVal.getSerializable("mealPlan"),mockMealPlan);
        assertEquals(returnVal.getInt("mealPlanIndex"),0);

    }

}
