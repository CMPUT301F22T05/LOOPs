package com.example.loops;


import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.ingredientFragments.forms.IngredientFormFragment;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.forms.AddRecipeFormFragment;
import com.example.loops.recipeFragments.forms.AddRecipeIngredientFormFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

/**
 * Test for the add ingredient form fragment in recipe
 */
@RunWith(AndroidJUnit4.class)
public  class AddRecipeIngredientFormFragmentTest{
    private TestNavHostController navController;
    private FragmentScenario<AddRecipeIngredientFormFragment> fragmentScenario;

    @Before
    public void setUp() {
        Bundle args = new Bundle();
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(
                AddRecipeIngredientFormFragment.class,
                args,
                R.style.Theme_LOOPs,
                new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                AddRecipeIngredientFormFragment fragment = new AddRecipeIngredientFormFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.addRecipeIngredientFormFragment);
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

    private void typeToEditText(int id, String toType) {
        onView(withId(id)).perform(clearText(), typeText(toType), ViewActions.closeSoftKeyboard());
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
    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }

    private void setDescription(String description) {
        typeToEditText(R.id.ingredientFormDescriptionInput, description);
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
                .get(AddRecipeIngredientFormFragment.RESULT_KEY);
        return ingredientFromPreviousFragment;
    }


    @Test
    public void hideUnusedInput() {
//        setDescription("Tuna Can");
//        setAmount("25");
//        setUnit("g");
//        setCategory("Meat");
//        setBestBeforeDate(2022, 10, 22);
//        setLocation("Pantry");
//        clickSubmit();
        fragmentScenario.onFragment(fragment -> {
            navController.setCurrentDestination(R.id.addRecipeFormFragment);
            navController.navigate(R.id.addRecipeIngredientToAddRecipeForm);
        });
        Ingredient typedIngredient = new Ingredient(
                "Tuna Can",
                "2022-10-21",
                "Pantry",
                12.0,
                "g",
                "Meat"
        );
        setDescription(typedIngredient.getDescription());
        setAmount(Double.toString(typedIngredient.getAmount()));
        setUnit(typedIngredient.getUnit());
        setCategory(typedIngredient.getCategory());
        clickSubmit();

        Ingredient submittedIngredient = getSubmittedIngredient();
        assertFalse( typedIngredient.equals(submittedIngredient) );
    }

    @Test
    public void getInputtedIngredient() {
        fragmentScenario.onFragment(fragment -> {
            navController.setCurrentDestination(R.id.addRecipeFormFragment);
            navController.navigate(R.id.addRecipeIngredientToAddRecipeForm);
        });
        Ingredient typedIngredient = new Ingredient(
                "Tuna Can",
                12.0,
                "g",
                "Meat"
        );
        setDescription(typedIngredient.getDescription());
        setAmount(Double.toString(typedIngredient.getAmount()));
        setUnit(typedIngredient.getUnit());
        setCategory(typedIngredient.getCategory());
        clickSubmit();

        Ingredient submittedIngredient = getSubmittedIngredient();
        assertTrue( typedIngredient.equals(submittedIngredient) );
    }


    @Test
    public void testSubmitNothing(){
        clickSubmit();
        onView(withText("Error: Missing fields")).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmittingWithNoDescription() {
        setAmount("25");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_description))).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmittingWithNoAmount() {
        setDescription("Tuna Can");
        setUnit("g");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_amount))).check(matches(isDisplayed()));
    }


    @Test
    public void testSubmittingWithNoUnit() {
        setDescription("Tuna Can");
        setAmount("25");
        setCategory("Meat");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_unit))).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmittingWithNoCategory() {
        setDescription("Tuna Can");
        setAmount("25");
        setUnit("g");
        clickSubmit();
        onView(withText(getString(R.string.ingredient_no_category))).check(matches(isDisplayed()));
    }


}
