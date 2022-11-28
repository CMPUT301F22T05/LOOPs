package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.ingredientFragments.forms.EditIngredientFormFragment;
import com.example.loops.models.Ingredient;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


/**
 * Test for edit ingredient form fragment
 */
public class EditIngredientFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<EditIngredientFormFragment> fragmentScenario;
    private Ingredient ingredientToEdit;

    /**
     * Sets up the test navigation host controller and fragment scenario before each test
     */
    @Before
    public void setUp() {
        ingredientToEdit = new Ingredient(
                "Tuna Can",
                "2022-10-21",
                "Pantry",
                25,
                "kg",
                "Meat"
        );
        Bundle args = new Bundle();
        args.putSerializable("editedIngredient", ingredientToEdit);

        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(EditIngredientFormFragment.class, args, R.style.Theme_LOOPs, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                EditIngredientFormFragment fragment = new EditIngredientFormFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.editIngredientFormFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);
                        }
                    }
                });
                return fragment;
            }
        });
    }

    private String getString(int id) {
        /**
         * https://stackoverflow.com/questions/39453986/android-espresso-assert-text-on-screen-against-string-in-resources
         * Date Accessed: 2022-10-30
         * Author: adalpari
         */
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }

    private LocalDate getDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month-1, day);
        return date;
    }

    private void typeToEditText(int id, String toType) {
        onView(withId(id)).perform(clearText(), typeText(toType), ViewActions.closeSoftKeyboard());
    }

    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }

    private void setDescription(String description) {
        typeToEditText(R.id.ingredientFormDescriptionInput, description);
    }

    private void setLocation(String location) {
        selectSpinnerOption(R.id.ingredientFormLocationInput, location);
    }

    private void setBestBeforeDate(int year, int month, int dayOfMonth) {
        onView(withId(R.id.ingredientFormBestBeforeDateInput))
                .perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, dayOfMonth));
        onView(withText("OK"))
                .perform(click());
    }

    private void setAmount(String amount) {
        typeToEditText(R.id.ingredientFormAmountInput, amount);
    }

    private void setUnit(String unit) {
        selectSpinnerOption(R.id.ingredientFormUnitInput, unit);
    }

    private void setCategory(String category) {
        selectSpinnerOption(R.id.ingredientFormCategoryInput, category);
    }

    private void clickSubmit() {
        onView(withId(R.id.ingredientFormSubmitButton))
                .perform(ViewActions.click());
    }

    /**
     * Test ingredient form's inputs are properly initialized with the values of the ingredient to edit
     */
    @Test
    public void testEditFormInitializedWithIngredientToEdit() {
        onView(withId(R.id.ingredientFormDescriptionInput))
                .check(matches( withText(ingredientToEdit.getDescription()) ));
        DateFormat dateFormat = new SimpleDateFormat("10/21/2022");
        onView(withId(R.id.ingredientFormBestBeforeDateInput))
                .check(matches( withText(ingredientToEdit.getBestBeforeDateString()) ));
        onView(withId(R.id.ingredientFormLocationInput))
                .check(matches( withSpinnerText(ingredientToEdit.getStoreLocation()) ));
        onView(withId(R.id.ingredientFormAmountInput))
                .check(matches( withText(Double.toString(ingredientToEdit.getAmount())) ));
        onView(withId(R.id.ingredientFormUnitInput))
                .check(matches( withSpinnerText(ingredientToEdit.getUnit()) ));
        onView(withId(R.id.ingredientFormCategoryInput))
                .check(matches( withSpinnerText(ingredientToEdit.getCategory()) ));
    }

    /**
     * Test submitting with by emptying all possible fields
     */
    @Test
    public void testSubmittingWithNothing() {
        setDescription("");
        setLocation("");
        setAmount("");
        setUnit("");
        setCategory("");
        clickSubmit();
        onView(withText("Error: Missing fields")).check(matches(isDisplayed()));
    }

    /**
     * Test empty description
     */
    @Test
    public void testSubmittingWithNoDescription() {
        setDescription("");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_description))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing location
     */
    @Test
    public void testSubmittingWithNoLocation() {
        setLocation("");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_location))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without typing in amount
     */
    @Test
    public void testSubmittingWithNoAmount() {
        setAmount("");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_amount))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing unit
     */
    @Test
    public void testSubmittingWithNoUnit() {
        setUnit("");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_unit))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing category
     */
    @Test
    public void testSubmittingWithNoCategory() {
        setCategory("");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_category))).check(matches(isDisplayed()));
    }
}
