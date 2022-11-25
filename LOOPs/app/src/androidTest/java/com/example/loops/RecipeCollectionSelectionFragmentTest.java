package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.factory.RecipeCollectionFactory;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionSelectionFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *  Test cases for selecting a recipe collection
 */
@RunWith(AndroidJUnit4.class)
public class RecipeCollectionSelectionFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<RecipeCollectionSelectionFragment> fragmentScenario;


    /**
     * Launches the fragment to test with the arguments
     * @param args args bundle
     */
    private void launchFragmentWithArgs(Bundle args) {
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(
                RecipeCollectionSelectionFragment.class,
                args,
                R.style.AppTheme,
                new FragmentFactory() {
                    @NonNull
                    @Override
                    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                        RecipeCollectionSelectionFragment fragment = new RecipeCollectionSelectionFragment();

                        fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                            @Override
                            public void onChanged(LifecycleOwner viewLifecycleOwner) {
                                // The fragment’s view has just been created
                                if (viewLifecycleOwner != null) {
                                    navController.setViewModelStore(new ViewModelStore());
                                    navController.setGraph(R.navigation.nav_graph);
                                    navController.setCurrentDestination(R.id.recipeCollectionSelectionFragment);
                                    Navigation.setViewNavController(fragment.requireView(), navController);
                                }
                            }
                        });
                        return fragment;
                    }
                });
    }

    /**
     * Launches the fragment to test with no recipes to filter
     */
    private void launchFragmentWithNoRecipesToFilter() {
        Bundle args = new Bundle();
        args.putSerializable("collectionType", RecipeCollectionFactory.CollectionType.PRESET_FOR_VIEW);
        launchFragmentWithArgs(args);
    }

    /**
     * Launches the fragment to test with recipes to filter
     * @param filter recipes to filter
     */
    private void launchFragmentWithRecipesToFilter(BaseRecipeCollection filter) {
        Bundle args = new Bundle();
        args.putSerializable("collectionType", RecipeCollectionFactory.CollectionType.PRESET_FOR_VIEW);
        args.putSerializable("recipesToFilter", filter);
        launchFragmentWithArgs(args);
    }

    /**
     * Test for sorting by title by both ascending or descending
     */
    @Test
    public void testSortByTitle() {
        launchFragmentWithNoRecipesToFilter();

        assertListSizeIs(2);

        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_title));
        assertAdapterItemHasTextInTextView(0, R.id.recipe_title_in_collection, "Grilled Cheese");
        assertAdapterItemHasTextInTextView(1, R.id.recipe_title_in_collection, "Pizza");
        toggleSortOptionToAscendingOrDescending();
        assertAdapterItemHasTextInTextView(1, R.id.recipe_title_in_collection, "Grilled Cheese");
        assertAdapterItemHasTextInTextView(0, R.id.recipe_title_in_collection, "Pizza");
        toggleSortOptionToAscendingOrDescending();
    }

    /**
     * Test by sorting by category by both ascending or descending
     */
    @Test
    public void testSortByCategory() {
        launchFragmentWithNoRecipesToFilter();

        assertListSizeIs(2);

        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_category));
        assertAdapterItemHasTextInTextView(0, R.id.recipe_category_in_collection, "Breakfast");
        assertAdapterItemHasTextInTextView(1, R.id.recipe_category_in_collection, "Dinner");
        toggleSortOptionToAscendingOrDescending();
        assertAdapterItemHasTextInTextView(1, R.id.recipe_category_in_collection, "Breakfast");
        assertAdapterItemHasTextInTextView(0, R.id.recipe_category_in_collection, "Dinner");
        toggleSortOptionToAscendingOrDescending();
    }

    /**
     * Tests that an recipe that is passed to the fragment to filter the recipes in the
     * fragment are actually filtered.
     * It tests to see if the second recipe gets filtered
     */
    @Test
    public void testFilteringSingleRecipe() {
        BaseRecipeCollection toFilter = new BaseRecipeCollection();
        IngredientCollection pizzaIngredients = new IngredientCollection();
        Bitmap pizza = getPhoto(R.drawable.pizza_test_image);
        Recipe toFilterRecipe = new Recipe(
                "Pizza",
                0,
                45,
                4,
                "Dinner",
                pizza,
                pizzaIngredients,
                "Italian Pizza"
        );
        toFilter.addRecipe( toFilterRecipe );
        launchFragmentWithRecipesToFilter(toFilter);

        assertListSizeIs(1);
        assertAdapterItemHasTextInTextView(0, R.id.recipe_title_in_collection, "Grilled Cheese");
        onView(withText("Pizza"))
                .check(doesNotExist());
    }

    /**
     * Tests that an ingredients that are passed to the fragment to filter the ingredients in the
     * fragment are actually filtered.
     * It tests to see if all ingredients are filtered
     */
    @Test
    public void testFilteringAllIngredients() {
        BaseRecipeCollection toFilter = new BaseRecipeCollection();
        IngredientCollection grilledCheeseIngredients = new IngredientCollection();
        Bitmap grilledCheese = getPhoto(R.drawable.grilled_cheese_test_image);
        Recipe recipe1 = new Recipe("Grilled Cheese",
                0,
                15,
                1,
                "Breakfast",
                grilledCheese,
                grilledCheeseIngredients,
                "Classic");
        IngredientCollection pizzaIngredients = new IngredientCollection();
        Bitmap pizza = getPhoto(R.drawable.pizza_test_image);
        Recipe recipe2 = new Recipe("Pizza",
                0,
                45,
                4,
                "Dinner",
                pizza,
                pizzaIngredients,
                "Italian Pizza");
        toFilter.addRecipe(recipe1);
        toFilter.addRecipe(recipe2);
        launchFragmentWithRecipesToFilter(toFilter);

        assertListSizeIs(0);
        onView(withText(recipe1.getTitle()))
                .check(doesNotExist());
        onView(withText(recipe2.getTitle()))
                .check(doesNotExist());
    }

    /**
     * Tests that a recipe with same attributes except number of servings is filtered out
     */
    @Test
    public void testFilteringForDuplicateRecipe() {
        BaseRecipeCollection toFilter = new BaseRecipeCollection();
        IngredientCollection grilledCheeseIngredients = new IngredientCollection();
        Bitmap grilledCheese = getPhoto(R.drawable.grilled_cheese_test_image);
        // All same attributes except number of servings
        Recipe recipe1 = new Recipe("Grilled Cheese",
                0,
                15,
                100000,         // DIFFERENT
                "Breakfast",
                grilledCheese,
                grilledCheeseIngredients,
                "Classic");
        toFilter.addRecipe( recipe1 );
        launchFragmentWithRecipesToFilter(toFilter);

        assertListSizeIs(1);
        assertAdapterItemHasTextInTextView(0, R.id.recipe_title_in_collection, "Pizza");
        onView(withText("Grilled Cheese"))
                .check(doesNotExist());
    }

    /**
     * Tests for a selection of a single recipe
     * Makes sure that the selected recipe is returned to the previous fragment properly
     */
    @Test
    public void testSingleSelection() {
        launchFragmentWithNoRecipesToFilter();

        int numServing1 = 5;

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for Grilled Cheese")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_num_serving_input, Integer.toString( numServing1 ));
        clickOkOnDialog();
        onView(withId(R.id.select_recipe_button)).perform(click());

        BaseRecipeCollection selected = getSelectionResult();
        assertEquals(1, selected.size());
        Recipe selectedRecipe = selected.getRecipe(0);
        assertEquals("Grilled Cheese", selectedRecipe.getTitle());
        assertEquals("Breakfast", selectedRecipe.getCategory());
        assertEquals(numServing1, selectedRecipe.getNumServing(), 0.00001);

        IngredientCollection recipeIngredients = selectedRecipe.getIngredients();
        assertEquals(0, recipeIngredients.size());
    }

    /**
     * Tests for a selection of two recipes
     * Makes sure that the selected recipes are returned to the previous fragment properly
     */
    @Test
    public void testMultipleSelection() {
        launchFragmentWithNoRecipesToFilter();

        int numServing1 = 5;
        int numServing2 = 25;

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for Grilled Cheese")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_num_serving_input, Integer.toString( numServing1 ));
        clickOkOnDialog();

        getAdapterItemAtPosition(1).perform(click());
        onView(withText("Select Quantity for Pizza")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_num_serving_input, Integer.toString( numServing2 ));
        clickOkOnDialog();

        onView(withId(R.id.select_recipe_button)).perform(click());

        BaseRecipeCollection selected = getSelectionResult();
        assertEquals(2, selected.size());
        Recipe selectedRecipe1 = selected.getRecipe(0);
        Recipe selectedRecipe2 = selected.getRecipe(1);
        if ( selectedRecipe1.getTitle().equals("Grilled Cheese") ) {
            assertEquals("Grilled Cheese", selectedRecipe1.getTitle());
            assertEquals("Breakfast", selectedRecipe1.getCategory());
            assertEquals(numServing1, selectedRecipe1.getNumServing(), 0.00001);
            IngredientCollection recipeIngredients1 = selectedRecipe1.getIngredients();
            assertEquals(0, recipeIngredients1.size());

            assertEquals("Pizza", selectedRecipe2.getTitle());
            assertEquals("Dinner", selectedRecipe2.getCategory());
            assertEquals(numServing2, selectedRecipe2.getNumServing(), 0.00001);
            IngredientCollection recipeIngredients2 = selectedRecipe2.getIngredients();
            assertEquals(0, recipeIngredients2.size());
        }
        else if ( selectedRecipe1.getTitle().equals("Pizza") ) {
            assertEquals("Grilled Cheese", selectedRecipe2.getTitle());
            assertEquals("Breakfast", selectedRecipe2.getCategory());
            assertEquals(numServing1, selectedRecipe2.getNumServing(), 0.00001);
            IngredientCollection recipeIngredients2 = selectedRecipe2.getIngredients();
            assertEquals(0, recipeIngredients2.size());

            assertEquals("Pizza", selectedRecipe1.getTitle());
            assertEquals("Dinner", selectedRecipe1.getCategory());
            assertEquals(numServing2, selectedRecipe1.getNumServing(), 0.00001);
            IngredientCollection recipeIngredients1 = selectedRecipe1.getIngredients();
            assertEquals(0, recipeIngredients1.size());
        }
        else {
            assertFalse("Unexpected recipe is in selection", false);
        }
    }

    /**
     * Tests if the cancel button in the dialog is properly working. That is, it closes the dialog
     * when it is clicked
     */
    @Test
    public void testCancelOnDialog() {
        launchFragmentWithNoRecipesToFilter();

        int numServing1 = 5;

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for Grilled Cheese")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_num_serving_input, Double.toString( numServing1 ));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Select Quantity for Grilled Cheese")).check(doesNotExist());
    }



    /*
        HELPER FUNCTIONS
     */
    private static Matcher<View> withListSize (final int size) {
        // https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso

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

    private void chooseSortOption(String option) {
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
    }

    private void toggleSortOptionToAscendingOrDescending() {
        onView(withId(R.id.sort_order_button)).perform(click());
    }

    private void assertListSizeIs(int size) {
        onView(withId(R.id.generic_collection_view)).check(matches(withListSize(size)));
    }

    private DataInteraction getAdapterItemAtPosition(int index) {
        return onData(is(instanceOf(Recipe.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(index);
    }

    private void assertAdapterItemHasTextInTextView(int index, int textViewId, String toCheckText) {
        // Checks the text inside one of the text view inside the adapter item for toCheckText
        getAdapterItemAtPosition(index).
                check(matches(hasDescendant(allOf(withId(textViewId),
                        withText(containsString(toCheckText))))));
    }

    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }

    private void typeToEditText(int id, String toType) {
        onView(withId(id)).perform(clearText(), typeText(toType), ViewActions.closeSoftKeyboard());
    }

    private void clickOkOnDialog() {
        onView(withId(android.R.id.button1)).perform(click());
    }

    private Bitmap getPhoto(int drawableID) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap photo = BitmapFactory.decodeResource(targetContext.getResources(), drawableID);
        return photo;
    }

    private BaseRecipeCollection getSelectionResult() {
        BaseRecipeCollection selected = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .get(RecipeCollectionSelectionFragment.RESULT_KEY);
        return selected;
    }
}

