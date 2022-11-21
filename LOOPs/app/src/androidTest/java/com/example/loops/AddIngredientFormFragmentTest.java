package com.example.loops;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;

import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

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
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.models.Ingredient;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Test for the add ingredient form fragment
 */
@RunWith(AndroidJUnit4.class)
public class AddIngredientFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<AddIngredientFormFragment> fragmentScenario;

    /**
     * Sets up the test navigation host controller and fragment scenario before each test
     */
    @Before
    public void setUp() {
        Bundle args = new Bundle();
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(AddIngredientFormFragment.class, args, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                AddIngredientFormFragment fragment = new AddIngredientFormFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.addIngredientFormFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);
                        }
                    }
                });
                return fragment;
            }
        });
    }

    private String getString(int id) {
        /*
          https://stackoverflow.com/questions/39453986/android-espresso-assert-text-on-screen-against-string-in-resources
          Date Accessed: 2022-10-30
          Author: adalpari
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

    private Ingredient getSubmittedIngredient() {
        Ingredient ingredientFromPreviousFragment = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .get(AddIngredientFormFragment.RESULT_KEY);
        return ingredientFromPreviousFragment;
    }

    /**
     * Test submitting a valid ingredient in the form to the ingredient collection
     */
    @Test

    public void testSubmittingWithValidIngredientToCollection() {
        fragmentScenario.onFragment(fragment -> {
            navController.setCurrentDestination(R.id.ingredientCollectionEditorFragment);
            navController.navigate(R.id.addIngredientFromCollection);
        });
        int year = 2022; int month = 10; int day = 21;

        Ingredient typedIngredient = new Ingredient(
          "Tuna Can",
          "2022-10-21",
          "Pantry",
          12.0,
          "g",
          "Meat"
        );
        setDescription(typedIngredient.getDescription());
        setBestBeforeDate(year, month, day);
        setLocation(typedIngredient.getStoreLocation());
        setAmount(Double.toString(typedIngredient.getAmount()));
        setUnit(typedIngredient.getUnit());
        setCategory(typedIngredient.getCategory());
        clickSubmit();
        sleep(1000);

        Ingredient submittedIngredient = getSubmittedIngredient();
        assertTrue( typedIngredient.equals(submittedIngredient) );
    }

    /**
     * Test submitting with untouched inputs
     */
    @Test
    public void testSubmittingWithNothing() {
        clickSubmit();
        onView(withText("Please fill out the form properly")).check(matches(isDisplayed()));
    }

    /**
     * Test empty description
     */
    @Test
    public void testSubmittingWithNoDescription() {
        setBestBeforeDate(2022, 10, 22);
        setLocation("Pantry");
        setAmount("25");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_description))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting with empty best before date
     */
    @Test
    public void testSubmittingWithNoBestBeforeDate() {
        setDescription("Tuna Can");
        setLocation("Pantry");
        setAmount("25");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_bestbeforedate))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing location
     */
    @Test
    public void testSubmittingWithNoLocation() {
        setDescription("Tuna Can");
        setBestBeforeDate(2022, 10, 22);
        setAmount("25");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_location))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without typing in amount
     */
    @Test
    public void testSubmittingWithNoAmount() {
        setDescription("Tuna Can");
        setBestBeforeDate(2022, 10, 22);
        setLocation("Pantry");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_amount))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing unit
     */
    @Test
    public void testSubmittingWithNoUnit() {
        setDescription("Tuna Can");
        setBestBeforeDate(2022, 10, 22);
        setLocation("Pantry");
        setAmount("25");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_unit))).check(matches(isDisplayed()));
    }

    /**
     * Test submitting without choosing category
     */
    @Test
    public void testSubmittingWithNoCategory() {
        setDescription("Tuna Can");
        setBestBeforeDate(2022, 10, 22);
        setLocation("Pantry");
        setAmount("25");
        setUnit("g");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_category))).check(matches(isDisplayed()));
    }
}