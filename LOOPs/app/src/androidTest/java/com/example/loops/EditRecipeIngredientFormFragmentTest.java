package com.example.loops;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.ingredientFragments.forms.EditIngredientFormFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.forms.AddRecipeIngredientFormFragment;
import com.example.loops.recipeFragments.forms.EditRecipeFormFragment;
import com.example.loops.recipeFragments.forms.EditRecipeIngredientFormFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing EditRecipeIngredientFormFragment in isolation
 */
@RunWith(AndroidJUnit4.class)
public class EditRecipeIngredientFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<EditRecipeIngredientFormFragment> fragmentScenario;
    private Ingredient ingredientEdit;
    private Bundle args;

    /**
     * Sets up the test navigation host controller and fragment scenario before each test
     */
    @Before
    public void setUp() {
        ingredientEdit = new Ingredient(
                "Tuna Can",
                25,
                "kg",
                "Meat"
        );
        args = new Bundle();
        args.putSerializable("editedIngredient", ingredientEdit);

        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(EditRecipeIngredientFormFragment.class, args, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                EditRecipeIngredientFormFragment fragment = new EditRecipeIngredientFormFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.editRecipeIngredientFormFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);
                        }
                    }
                });
                return fragment;
            }
        });
    }

    /**
     * Creates an EditRecipeIngredientFormFragment for testing
     */
    public void launchFragment() {
        /*
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(EditRecipeIngredientFormFragment.class, args, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                EditRecipeIngredientFormFragment editRecipeIngredientFormFragment = new EditRecipeIngredientFormFragment();

                editRecipeIngredientFormFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.editRecipeIngredientFormFragment,args);
                            Navigation.setViewNavController(editRecipeIngredientFormFragment.requireView(), navController);
                        }
                    }
                });
                return editRecipeIngredientFormFragment;
            }

        });

    }
    /**
     * gets a string from strings.xml
     * @param id id of string needed
     * @return string in strings.xml
     */
    private String getString(int id) {
        /*
          https://stackoverflow.com/questions/39453986/android-espresso-assert-text-on-screen-against-string-in-resources
          Date Accessed: 2022-10-30
          Author: adalpari
         */
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return targetContext.getResources().getString(id);
    }
    /**
     * Method to type given string into an editText of the id given and close the keyboard
     * @param id
     * @param toType
     */
    private void typeToEditText(int id, String toType) {
        onView(withId(id)).perform(clearText(), typeText(toType), closeSoftKeyboard());
    }
    /**
     * Method to select an item corresponding to option in the spinner that has id
     * @param id
     * @param option
     */
    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }
    /**
     * Type in the description in EditRecipeIngredientFormFragment editText
     * @param description
     */
    private void setDescription(String description) {
        typeToEditText(R.id.ingredientFormDescriptionInput, description);
    }
    /**
     * Type in the amount in EditRecipeIngredientFormFragment editText
     * @param amount
     */
    private void setAmount(String amount) {
        typeToEditText(R.id.ingredientFormAmountInput, amount);
    }
    /**
     * Type in the unit in EditRecipeIngredientFormFragment editText
     * @param unit
     */
    private void setUnit(String unit) {
        selectSpinnerOption(R.id.ingredientFormUnitInput, unit);
    }
    /**
     * Type in the category in EditRecipeIngredientFormFragment editText
     * @param category
     */
    private void setCategory(String category) {
        selectSpinnerOption(R.id.ingredientFormCategoryInput, category);
    }
    /**
     * submit EditRecipeIngredientFormFragment
     */
    private void clickSubmit() {
        onView(withId(R.id.ingredientFormSubmitButton))
                .perform(ViewActions.click());
        closeSoftKeyboard();
    }
//    /**
//     * Gets arguments of the next fragment that a button in
//     * EditRecipeFragment navigates to
//     * @return
//     */
//    private Bundle getReturnBundle() {
//        return navController.getBackStack()
//                .get(navController.getBackStack().size()-1)
//                .getArguments();
//    }
    /**
     * Test initializeFormWithIngredientAttributes
     */
    @Test
    public void initializeFormWithIngredientAttributesTest() {
        onView(withId(R.id.ingredientFormDescriptionInput))
                .check(matches( withText(ingredientEdit.getDescription()) ));
        onView(withId(R.id.ingredientFormAmountInput))
                .check(matches( withText(Double.toString(ingredientEdit.getAmount())) ));
        onView(withId(R.id.ingredientFormUnitInput))
                .check(matches( withSpinnerText(ingredientEdit.getUnit()) ));
        onView(withId(R.id.ingredientFormCategoryInput))
                .check(matches( withSpinnerText(ingredientEdit.getCategory()) ));
    }
    /**
     * Test ChangeDescription
     */
    @Test
    public void testChangeDescription() {
        launchFragment();
        Ingredient ingredientToEdit = new Ingredient(
                "Tuna Can",
                25,
                "kg",
                "Meat"
        );
        ingredientToEdit.setDescription("aa");
        clickSubmit();
        assertNotEquals(ingredientEdit.getDescription(),ingredientToEdit.getDescription());
    }
    /**
     * Test ChangeAmount
     */
    @Test
    public void testChangeAmount() {
        launchFragment();
        Ingredient ingredientToEdit = new Ingredient(
                "Tuna Can",
                25,
                "kg",
                "Meat"
        );
        ingredientToEdit.setAmount(10);
        clickSubmit();
        assertNotEquals(ingredientEdit.getAmount(),ingredientToEdit.getAmount());
    }

    /**
     * Test ChangeUnit
     */
    @Test
    public void testChangeUnit() {
        launchFragment();
        Ingredient ingredientToEdit = new Ingredient(
                "Tuna Can",
                25,
                "kg",
                "Meat"
        );
        ingredientToEdit.setUnit("g");
        clickSubmit();
        assertNotEquals(ingredientEdit.getUnit(),ingredientToEdit.getUnit());
    }
    /**
     * Test ChangeCategory
     */
    @Test
    public void testChangeCategory() {
        launchFragment();
        Ingredient ingredientToEdit = new Ingredient(
                "Tuna Can",
                25,
                "kg",
                "Meat"
        );
        ingredientToEdit.setCategory("Food");
        clickSubmit();
        assertNotEquals(ingredientEdit.getCategory(),ingredientToEdit.getCategory());
    }

}
