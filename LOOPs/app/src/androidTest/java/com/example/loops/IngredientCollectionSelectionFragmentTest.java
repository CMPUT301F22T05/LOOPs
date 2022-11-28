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

import com.example.loops.factory.IngredientCollectionFactory.CollectionType;
import com.example.loops.ingredientFragments.IngredientCollectionSelectionFragment;
import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *  Test cases for selecting an ingredient collection
 */
@RunWith(AndroidJUnit4.class)
public class IngredientCollectionSelectionFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<IngredientCollectionSelectionFragment> fragmentScenario;


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
            IngredientCollectionSelectionFragment.class,
            args,
            R.style.Theme_LOOPs,
            new FragmentFactory() {
                @NonNull
                @Override
                public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                    IngredientCollectionSelectionFragment fragment = new IngredientCollectionSelectionFragment();

                    fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                        @Override
                        public void onChanged(LifecycleOwner viewLifecycleOwner) {
                            // The fragment’s view has just been created
                            if (viewLifecycleOwner != null) {
                                navController.setViewModelStore(new ViewModelStore());
                                navController.setGraph(R.navigation.nav_graph);
                                navController.setCurrentDestination(R.id.ingredientCollectionSelectionFragment);
                                Navigation.setViewNavController(fragment.requireView(), navController);
                            }
                        }
                    });
                    return fragment;
                }
        });
    }

    /**
     * Launches the fragment to test with no ingredients to filter
     */
    private void launchFragmentWithNoIngredientsToFilter() {
        Bundle args = new Bundle();
        args.putSerializable("collectionType", CollectionType.PRESET_FOR_VIEW);
        launchFragmentWithArgs(args);
    }

    /**
     * Launches the fragment to test with ingredients to filter
     * @param filter ingredients to filter
     */
    private void launchFragmentWithIngredientsToFilter(IngredientCollection filter) {
        Bundle args = new Bundle();
        args.putSerializable("collectionType", CollectionType.PRESET_FOR_VIEW);
        args.putSerializable("ingredientsToFilter", filter);
        launchFragmentWithArgs(args);
    }

    /**
     * Test for sorting by description by both ascending or descending
     */
    @Test
    public void testSortByDescription() {
        launchFragmentWithNoIngredientsToFilter();

        assertListSizeIs(2);

        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_description));
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_description_in_selection, "AAA");
        assertAdapterItemHasTextInTextView(1, R.id.ingredient_description_in_selection, "BBB");
        toggleSortOptionToAscendingOrDescending();
        assertAdapterItemHasTextInTextView(1, R.id.ingredient_description_in_selection, "AAA");
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_description_in_selection, "BBB");
        toggleSortOptionToAscendingOrDescending();
    }

    /**
     * Test by sorting by category by both ascending or descending
     */
    @Test
    public void testSortByCategory() {
        launchFragmentWithNoIngredientsToFilter();

        assertListSizeIs(2);

        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_Category));
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_category_in_selection, "XXX");
        assertAdapterItemHasTextInTextView(1, R.id.ingredient_category_in_selection, "YYY");
        toggleSortOptionToAscendingOrDescending();
        assertAdapterItemHasTextInTextView(1, R.id.ingredient_category_in_selection, "XXX");
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_category_in_selection, "YYY");
        toggleSortOptionToAscendingOrDescending();
    }

    /**
     * Tests that an ingredient that is passed to the fragment to filter the ingredients in the
     * fragment are actually filtered.
     * It tests to see if the second ingredient gets filtered
     */
    @Test
    public void testFilteringSingleIngredient() {
        IngredientCollection toFilter = new IngredientCollection();
        Ingredient toFilterIngredient = new Ingredient(
                "AAA",
                "2022-10-29",
                "cupboard",
                1,
                "unit",
                "YYY"
        );
        toFilter.addIngredient( toFilterIngredient );
        launchFragmentWithIngredientsToFilter(toFilter);

        assertListSizeIs(1);
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_description_in_selection, "BBB");
        onView(withText("AAA"))
                .check(doesNotExist());
    }

    /**
     * Tests that an ingredients that are passed to the fragment to filter the ingredients in the
     * fragment are actually filtered.
     * It tests to see if all ingredients are filtered
     */
    @Test
    public void testFilteringAllIngredients() {
        IngredientCollection toFilter = new IngredientCollection();
        Ingredient ing1 = new Ingredient(
                "BBB",
                "2022-10-28",
                "fridge",
                1,
                "unit",
                "XXX"
        );
        Ingredient ing2 = new Ingredient(
                "AAA",
                "2022-10-29",
                "cupboard",
                1,
                "unit",
                "YYY"
        );
        toFilter.addIngredient( ing1 );
        toFilter.addIngredient( ing2 );
        launchFragmentWithIngredientsToFilter(toFilter);

        assertListSizeIs(0);
        onView(withText(ing1.getDescription()))
                .check(doesNotExist());
        onView(withText(ing2.getDescription()))
                .check(doesNotExist());
    }

    /**
     * Tests that an ingredient with same description and same category (but not necessarily, same
     * values for other attributes) are filtered out
     */
    @Test
    public void testFilteringForDuplicateIngredient() {
        IngredientCollection toFilter = new IngredientCollection();
        // Category and descriptions are the same but everything else differs
        Ingredient ing1 = new Ingredient(
                "BBB",
                "2022-12-28",
                "DIFFERENT",
                1000000,
                "DIFFERENT",
                "XXX"
        );
        toFilter.addIngredient( ing1 );
        launchFragmentWithIngredientsToFilter(toFilter);

        assertListSizeIs(1);
        assertAdapterItemHasTextInTextView(0, R.id.ingredient_description_in_selection, "AAA");
        onView(withText("BBB"))
                .check(doesNotExist());
    }

    /**
     * Tests for a selection of a single ingredient
     * Makes sure that the selected ingredient is returned to the previous fragment properly
     */
    @Test
    public void testSingleSelection() {
        launchFragmentWithNoIngredientsToFilter();

        double amount1 = 25.0; String unit1 = "unit";

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for BBB")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_amount_input, Double.toString( amount1 ));
        // selectSpinnerOption(R.id.dialog_unit_textview, unit);    FIXME: for some reason, the unit input is not a spinner
        clickOkOnDialog();
        onView(withId(R.id.select_button)).perform(click());

        IngredientCollection selected = getSelectionResult();
        assertEquals(1, selected.size());
        Ingredient selectedIngredient = selected.get(0);
        assertEquals("BBB", selectedIngredient.getDescription());
        assertEquals("XXX", selectedIngredient.getCategory());
        assertEquals(amount1, selectedIngredient.getAmount(), 0.00001);
        assertEquals(unit1, selectedIngredient.getUnit());
    }

    /**
     * Tests for a selection of two ingredients
     * Makes sure that the selected ingredients are returned to the previous fragment properly
     */
    @Test
    public void testMultipleSelection() {
        launchFragmentWithNoIngredientsToFilter();

        double amount1 = 25.0; String unit1 = "unit";
        double amount2 = 0.5; String unit2 = "unit";

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for BBB")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_amount_input, Double.toString( amount1 ));
        // selectSpinnerOption(R.id.dialog_unit_textview, unit);    FIXME: for some reason, the unit input is not a spinner
        clickOkOnDialog();

        getAdapterItemAtPosition(1).perform(click());
        onView(withText("Select Quantity for AAA")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_amount_input, Double.toString( amount2 ));
        // selectSpinnerOption(R.id.dialog_unit_textview, unit);    FIXME: for some reason, the unit input is not a spinner
        clickOkOnDialog();

        onView(withId(R.id.select_button)).perform(click());

        IngredientCollection selected = getSelectionResult();
        assertEquals(2, selected.size());
        Ingredient selectedIngredient1 = selected.get(0);
        Ingredient selectedIngredient2 = selected.get(1);
        if ( selectedIngredient1.getDescription().equals("BBB") ) {
            assertEquals("BBB", selectedIngredient1.getDescription());
            assertEquals("XXX", selectedIngredient1.getCategory());
            assertEquals(amount1, selectedIngredient1.getAmount(), 0.00001);
            assertEquals(unit1, selectedIngredient1.getUnit());

            assertEquals("AAA", selectedIngredient2.getDescription());
            assertEquals("YYY", selectedIngredient2.getCategory());
            assertEquals(amount2, selectedIngredient2.getAmount(), 0.00001);
            assertEquals(unit2, selectedIngredient2.getUnit());
        }
        else if ( selectedIngredient1.getDescription().equals("AAA") ) {
            assertEquals("BBB", selectedIngredient2.getDescription());
            assertEquals("XXX", selectedIngredient2.getCategory());
            assertEquals(amount1, selectedIngredient2.getAmount(), 0.00001);
            assertEquals(unit1, selectedIngredient2.getUnit());
            assertEquals("AAA", selectedIngredient1.getDescription());
            assertEquals("YYY", selectedIngredient1.getCategory());
            assertEquals(amount2, selectedIngredient1.getAmount(), 0.00001);
            assertEquals(unit2, selectedIngredient1.getUnit());
        }
        else {
            assertFalse("Unexpected ingredient is in selection", false);
        }
    }

    /**
     * Tests if the cancel button in the dialog is properly working. That is, it closes the dialog
     * when it is clicked
     */
    @Test
    public void testCancelOnDialog() {
        launchFragmentWithNoIngredientsToFilter();

        double amount1 = 25.0; String unit1 = "unit";

        getAdapterItemAtPosition(0).perform(click());
        onView(withText("Select Quantity for BBB")).check(matches(isDisplayed()));
        typeToEditText(R.id.dialog_amount_input, Double.toString( amount1 ));
        // selectSpinnerOption(R.id.dialog_unit_textview, unit);    FIXME: for some reason, the unit input is not a spinner
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText("Select Quantity for BBB")).check(doesNotExist());
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
        return onData(is(instanceOf(Ingredient.class)))
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

    private IngredientCollection getSelectionResult() {
        IngredientCollection selected = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .get(IngredientCollectionSelectionFragment.RESULT_KEY);
        return selected;
    }
}
