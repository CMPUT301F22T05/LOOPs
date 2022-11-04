package com.example.loops;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class IngredientCollectionEditorFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<IngredientCollectionEditorFragment> fragmentScenario;
    private Bundle bundle;

    @Before
    public void setUp() {
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        bundle = new Bundle();
        //Two ingredients will be shown in the fragment for testing
        //1. Ingredient("BBB", "10/28/2022", "fridge", 1, "", "XXX")
        //2. Ingredient("AAA", "10/29/2022", "cupboard", 1, "", "YYY")
        // check method setIngredientCollectionToDisplay(CollectionType type)
        // in IngredientCollectionFragment.java
        bundle.putSerializable("collectionType",
                IngredientCollectionFragment
                        .CollectionType.FOR_TEST_INGREDIENT_COLLECTION_EDITOR_FRAGMENT);
    }
    private void lunchFragment() {

        fragmentScenario = FragmentScenario.
                launchInContainer(IngredientCollectionEditorFragment.class, bundle);

        fragmentScenario.onFragment(fragment -> {
            //setCurrentDestination
            navController.setGraph(R.navigation.nav_graph);
            Navigation.setViewNavController(fragment.requireView(), navController);
        });

        //IdlingPolicies.setMasterPolicyTimeout(5000, TimeUnit.HOURS);
        /*fragmentScenario.onFragment(new FragmentScenario.FragmentAction<IngredientCollectionEditorFragment>() {
            @Override
            public void perform(@NonNull IngredientCollectionEditorFragment ingredientCollectionEditorFragment) {

            }
        })*/
    }

    private void chooseSortOption(String option) {
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
        //IdlingPolicies.setIdlingResourceTimeout(5000, TimeUnit.HOURS);

    }

    private void testSortByDescription() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_description));
//        onData(allOf(instanceOf(Ingredient.class))).atPosition(0).check(matches(withText(containsString("AAA"))));
//        onData(is(instanceOf(IngredientStorageViewAdapter.class))).atPosition(0).check(matches(withText(containsString("AAA"))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage),
                        withText(containsString("AAA"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage),
                        withText(containsString("BBB"))))));
    }

    private void testSortByBestBeforeDate() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_best_before_date));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("10/28/2022"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("10/29/2022"))))));
    }

    private void testSortByLocation() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_Location));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_location_in_storage),
                        withText(containsString("cupboard"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_location_in_storage),
                        withText(containsString("fridge"))))));
    }

    private void testSortByCategory() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_Category));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("XXX"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("YYY"))))));
    }

    @Test
    public void testSort() {
        lunchFragment();
        testSortByDescription();
        testSortByBestBeforeDate();
        testSortByLocation();
        testSortByCategory();
    }

    private void mimicAddIngredientRequest() {
        bundle.putSerializable("addedIngredient",
                new Ingredient(
                        "apple",
                        "11/04/2022",
                        "pantry",
                        1,
                        "kg",
                        "fruit"));
    }

    @Test
    public void testAddIngredientRequest() {
        mimicAddIngredientRequest();
        lunchFragment();
        onView(withId(R.id.generic_collection_view)).check(ViewAssertions.matches(withListSize(3)));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(2)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("fruit"))))));
    }

    private void mimicEditIngredientRequest() {
        bundle.putSerializable("editedIngredient",
                new Ingredient("apple", "11/04/2022", "pantry", 1, "kg", "fruit"));
        bundle.putInt("editedIngredientIndex", 0);
    }

    @Test
    public void testEditIngredientRequest() {
        mimicEditIngredientRequest();
        lunchFragment();
        onView(withId(R.id.generic_collection_view)).check(ViewAssertions.matches(withListSize(2)));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_location_in_storage),
                        withText(containsString("pantry"))))));
    }

    private void mimicDeleteIngredientRequest() {
        bundle.putBoolean("deleteFlag", true);
        bundle.putInt("editedIngredientIndex", 0);
    }

    @Test
    public void testDeleteIngredientRequest() {
        mimicDeleteIngredientRequest();
        lunchFragment();
        onView(withId(R.id.generic_collection_view)).check(ViewAssertions.matches(withListSize(1)));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage),
                        withText(containsString("AAA"))))));
    }

    /**
     * reference:
     * https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso
     * @param size
     * @return
     */
    public static Matcher<View> withListSize (final int size) {
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
}
