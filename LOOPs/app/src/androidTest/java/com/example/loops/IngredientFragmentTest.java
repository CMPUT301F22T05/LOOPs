package com.example.loops;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import com.example.loops.ingredientFragments.IngredientFragment;
import com.example.loops.models.Ingredient;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Test cases for the Ingredient fragment
 */
@RunWith(AndroidJUnit4.class)
public class IngredientFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<IngredientFragment> fragmentScenario;

    /**
     * Opens the ingredient fragment displayed the ingredient from the argument
     * @param ingredient ingredient to display
     * @param ingredientInd index of the ingredient
     * @param parentFragment the fragment that opened the ingredient fragment
     */
    private void displayIngredient(Ingredient ingredient, int ingredientInd, int parentFragment) {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedIngredient", ingredient);
        bundle.putInt("selectedIngredientIndex", ingredientInd);
        bundle.putInt("fromWhichFragment", parentFragment);
        fragmentScenario = FragmentScenario.launchInContainer(IngredientFragment.class, bundle);
        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.ingredientFragment, bundle);
            Log.e("id", Integer.toString(R.id.ingredientFragment));
            Navigation.setViewNavController(fragment.requireView(), navController);
        });
    }

    private LocalDate getDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month-1, day);
        return date;
    }

    private Ingredient getTestIngredient() {
        Ingredient ingredient = new Ingredient(
                "Roast Beef",
                "2022-10-21",
                "Fridge",
                23,
                "kg",
                "Meat"
        );
        return ingredient;
    }

    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }

    /**
     * Checks if all the static UI texts are shown in the ingredient fragment that is opened by
     * the ingredient collection fragment
     */
    @Test
    public void testStaticDisplayFromIngredientCollection() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        String[] expected = {
                "Description:",
                "Best Before Date:",
                "Location:",
                "Amount:",
                "Unit:",
                "Ingredient Category:",
                "BACK",
                "EDIT",
                "DELETE"
        };
        for (String e : expected) {
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
    }

    /**
     * Checks if all the static UI texts are shown in the ingredient fragment that is opened by
     * the edit ingredient form fragment
     */
    @Test
    public void testStaticDisplayFromEditIngredientForm() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_form);

        String[] expected = {
                "Description:",
                "Best Before Date:",
                "Location:",
                "Amount:",
                "Unit:",
                "Ingredient Category:",
                "CONFIRM",
                "EDIT",
                "DELETE"
        };
        for (String e : expected) {
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
    }

    /**
     * Tests if the ingredient the form should be displaying is properly shown in the UI texts
     */
    @Test
    public void testIngredientFragmentDisplay() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        String[] expected = {
                "Roast Beef",
                "2022-10-21",
                "Fridge",
                "23.0",
                "kg",
                "Meat"
        };
        for (String e : expected) {
            onView(withText(e)).check(matches(isDisplayed()));
        }
    }

    /**
     * Tests the back button. Check if the ingredient fragment correctly backs out to the
     * ingredient collection and the ingredient fragment returns proper arguments back to the parent
     * fragment
     */
    @Test
    public void testBackButton() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_back_button)).perform(ViewActions.click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.ingredientCollectionEditorFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedIngredient"), ingredient);
        assertEquals(returnValue.getInt("editedIngredientIndex"), 6);
        assertEquals(returnValue.getBoolean("deleteFlag"), false);
    }

    /**
     * Tests the edit button. Check if the ingredient fragment correctly opens the edit ingredient
     * form fragment and if the ingredient fragment properly sends the arguments to the form
     */
    @Test
    public void testEditButton() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_edit_button)).perform(ViewActions.click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.editIngredientFormFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedIngredient"), ingredient);
        assertEquals(returnValue.getInt("editIngredientIndex"), 6);
    }

    /**
     * Check if the delete prompt shows on delete button click and the cancel button in the prompt
     * brings the user back to the ingredient fragment
     */
    @Test
    public void testDeleteButtonCancel() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_delete_button)).perform(ViewActions.click());
        String[] expected = {
                "Warning",
                "Delete Roast Beef?",
                "no",
                "yes"
        };
        for (String e : expected) {
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }

        onView(withId(R.id.delete_popup_no_button)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.ingredientFragment);
    }

    /**
     * Check if the delete prompt shows on delete button click and the confirm button in the prompt
     * brings the user back to the ingredient collection fragment with proper arguments sent back
     * especially the deleteFlag set to true
     */
    @Test
    public void testDeleteButtonConfirm() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_delete_button)).perform(ViewActions.click());
        String[] expected = {
                "Warning",
                "Delete Roast Beef?",
                "no",
                "yes"
        };
        for (String e : expected) {
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }

        onView(withId(R.id.delete_popup_yes_button)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.ingredientCollectionEditorFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedIngredient"), ingredient);
        assertEquals(returnValue.getInt("editedIngredientIndex"), 6);
        assertEquals(returnValue.getBoolean("deleteFlag"), true);
    }
}
