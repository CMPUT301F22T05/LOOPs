package com.example.loops;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertFalse;

import android.os.Bundle;
import android.widget.Button;

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
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.loops.ingredientFragments.IngredientFragment;
import com.example.loops.models.Ingredient;
import com.example.loops.recipeFragments.RecipeFragment;
import com.example.loops.recipeFragments.forms.AddRecipeIngredientFormFragment;
import com.example.loops.userPreferencesFragments.UserPreferencesFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test cases for the UserPreferencesFragment
 */
@RunWith(AndroidJUnit4.class)
public class UserPreferencesFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<UserPreferencesFragment> fragmentScenario;
    /**
     * SetUp and display the view
     */
    @Before
    public void setUp() {
        Bundle args = new Bundle();
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(
                UserPreferencesFragment.class,
                args,
                R.style.Theme_LOOPs,
                new FragmentFactory() {
                    @NonNull
                    @Override
                    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                        UserPreferencesFragment fragment = new UserPreferencesFragment();

                        fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                            @Override
                            public void onChanged(LifecycleOwner viewLifecycleOwner) {
                                // The fragment’s view has just been created
                                if (viewLifecycleOwner != null) {
                                    navController.setViewModelStore(new ViewModelStore());
                                    navController.setGraph(R.navigation.nav_graph);
                                    navController.setCurrentDestination(R.id.userPreferencesFragment);
                                    Navigation.setViewNavController(fragment.requireView(), navController);
                                }
                            }
                        });
                        return fragment;
                    }
                });
    }

    /**
     * button for  clickGotoStoredLocation
     */
    private void clickGotoStoredLocationButton() {
        onView(withId(R.id.goto_storage_option_editor))
                .perform(ViewActions.click());
    }
    /**
     * button for clickGotoIngredientCategory
     */
    private void clickGotoIngredientCategoryButton() {
        onView(withId(R.id.goto_icategory_option_editor))
                .perform(ViewActions.click());

    }
    /**
     * button for clickGotoRecipeCategory
     */
    private void clickGotoRecipeCategoryButton() {
        onView(withId(R.id.goto_rcategory_option_editor))
                .perform(ViewActions.click());

    }
    /**
     * Test for clickGotoStoredLocationButton
     */
    @Test
    public void clickGotoStoredLocationButtonTest() {

        clickGotoIngredientCategoryButton();
    }
    /**
     * Test for clickGotoIngredientCategoryButton
     */
    @Test
    public void clickGotoIngredientCategoryButtonTest() {

        clickGotoStoredLocationButton();
    }
    /**
     * Test for clickGotoRecipeCategoryButton
     */
    @Test
    public void clickGotoRecipeCategoryButtonTest() {

        clickGotoRecipeCategoryButton();
    }

}
