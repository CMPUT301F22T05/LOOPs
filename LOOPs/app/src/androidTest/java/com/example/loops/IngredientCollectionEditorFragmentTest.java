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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;

import com.example.loops.factory.IngredientCollectionFactory;
import com.example.loops.ingredientFragments.IngredientCollectionEditorFragment;
import com.example.loops.ingredientFragments.IngredientCollectionFragment;
import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.ingredientFragments.forms.EditIngredientFormFragment;
import com.example.loops.models.Ingredient;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *  Test cases for editing an ingredient collection
 */
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
        //1. Ingredient("BBB", "2022-10-28", "fridge", 1, "", "XXX")
        //2. Ingredient("AAA", "2022-10-29", "cupboard", 1, "", "YYY")
        //check method setIngredientCollectionToDisplay(CollectionType type)
        //in IngredientCollectionFragment.java
        bundle.putSerializable("collectionType",
                IngredientCollectionFactory
                        .CollectionType.PRESET);
    }

    private void launchFragment() {
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(IngredientCollectionEditorFragment.class, bundle,R.style.AppTheme, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                IngredientCollectionEditorFragment fragment = new IngredientCollectionEditorFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.ingredientCollectionEditorFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);
                        }
                    }
                });
                return fragment;
            }
        });
    }

    private void chooseSortOption(String option) {
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
        //IdlingPolicies.setIdlingResourceTimeout(5000, TimeUnit.HOURS);

    }

    /**
     * toggle between ascending or descending
     */
    private void changeSortOrder() {
        onView(withId(R.id.sort_order_button)).perform(click());
    }

    /**
     * Test for sorting by description in both ordering
     */
    private void testSortByDescription() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_description));
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
        changeSortOrder();
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage),
                        withText(containsString("AAA"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage),
                        withText(containsString("BBB"))))));
        changeSortOrder();
    }

    /**
     * Test for sorting by best before date in both ordering
     */
    private void testSortByBestBeforeDate() {
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_best_before_date));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("2022-10-28"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("2022-10-29"))))));
        changeSortOrder();
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("2022-10-28"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_bbd_in_storage),
                        withText(containsString("2022-10-29"))))));
        changeSortOrder();
    }

    /**
     * Test by sorting by location in both ordering
     */
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
        changeSortOrder();
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_location_in_storage),
                        withText(containsString("cupboard"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_location_in_storage),
                        withText(containsString("fridge"))))));
        changeSortOrder();
    }

    /**
     * Test by sorting by category in both ordering
     */
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
        changeSortOrder();
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(1)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("XXX"))))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("YYY"))))));
        changeSortOrder();
    }

    /**
     * Tests all sort types by running each individual sorting test in both ordering
     */
    @Test
    public void testSort() {
        launchFragment();
        testSortByDescription();
        testSortByBestBeforeDate();
        testSortByLocation();
        testSortByCategory();
    }

    private void mimicAddIngredientRequest() {
        fragmentScenario = FragmentScenario.launchInContainer(IngredientCollectionEditorFragment.class, bundle, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                IngredientCollectionEditorFragment fragment = new IngredientCollectionEditorFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.ingredientCollectionEditorFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);

                            navController.getCurrentBackStackEntry()
                            .getSavedStateHandle()
                            .set(
                                    AddIngredientFormFragment.RESULT_KEY,
                                    new Ingredient(
                                            "apple",
                                            "2022-11-04",
                                            "pantry",
                                            1,
                                            "kg",
                                            "fruit")
                            );
                        }
                    }
                });
                return fragment;
            }
        });
    }

    /**
     * Tests adding an ingredient to the collection
     */
    @Test
    public void testAddIngredientRequest() {
        // launchFragment();
        mimicAddIngredientRequest();
        onView(withId(R.id.generic_collection_view)).check(ViewAssertions.matches(withListSize(3)));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(2)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_storage),
                        withText(containsString("fruit"))))));
    }

    private void mimicEditIngredientRequest() {
        bundle.putSerializable("editedIngredient",
                new Ingredient("apple",
                        "2022-11-04",
                        "pantry",
                        1,
                        "kg",
                        "fruit"));
        bundle.putInt("editedIngredientIndex", 0);
    }

    /**
     * Tests editing an ingredient in the collection
     */
    @Test
    public void testEditIngredientRequest() {
        mimicEditIngredientRequest();
        launchFragment();
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

    /**
     * Tests deleting an ingredient in the collection
     */
    @Test
    public void testDeleteIngredientRequest() {
        mimicDeleteIngredientRequest();
        launchFragment();
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
