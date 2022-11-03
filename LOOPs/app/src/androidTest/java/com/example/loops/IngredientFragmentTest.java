package com.example.loops;

import static android.os.SystemClock.sleep;
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

import org.mockito.Mockito;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class IngredientFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<IngredientFragment> fragmentScenario;

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

    private Date getDate(int year, int month, int day) {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    private Ingredient getTestIngredient() {
        Ingredient ingredient = new Ingredient(
                "Roast Beef",
                getDate(2023, 12, 9),
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

    @Test
    public void testIngredientFragmentDisplay() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        String[] expected = {
                "Roast Beef",
                "12/09/2023",
                "Fridge",
                "23",
                "kg",
                "Meat"
        };
        for (String e : expected) {
            onView(withText(e)).check(matches(isDisplayed()));
        }
    }

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

    @Test
    public void testDeleteButtonCancel() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_delete_button)).perform(ViewActions.click());
        String[] expected = {
                "Warning",
                "delete ingredient roast beef?",
                "no",
                "yes"
        };
        for (String e : expected) {
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }

        onView(withId(R.id.delete_popup_no_button)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.ingredientFragment);
    }

    @Test
    public void testDeleteButtonConfirm() {
        Ingredient ingredient = getTestIngredient();
        displayIngredient(ingredient, 6, R.layout.fragment_ingredient_collection);

        onView(withId(R.id.ingredient_delete_button)).perform(ViewActions.click());
        String[] expected = {
                "Warning",
                "delete ingredient roast beef?",
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