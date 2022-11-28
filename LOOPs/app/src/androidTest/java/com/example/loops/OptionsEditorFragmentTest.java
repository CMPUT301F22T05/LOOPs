package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.factory.IngredientCollectionFactory;
import com.example.loops.factory.RecipeCollectionFactory;
import com.example.loops.ingredientFragments.IngredientCollectionEditorFragment;
import com.example.loops.ingredientFragments.IngredientFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionEditorFragment;
import com.example.loops.recipeFragments.forms.AddRecipeIngredientFormFragment;
import com.example.loops.userPreferencesFragments.OptionsEditorFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for the OptionsEditorFragment
 */
@RunWith(AndroidJUnit4.class)
public class OptionsEditorFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<OptionsEditorFragment> fragmentScenario;
    private Bundle bundle;
    private String option3;

    @Before
    public void setUp() {
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        bundle = new Bundle();
        bundle.putSerializable("optionsType",
                OptionsEditorFragment
                        .OptionsType.INGREDIENT_CATEGORY);
        option3 = "A Box";
    }
    private void launchFragment() {
        /**
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(OptionsEditorFragment.class, bundle, R.style.Theme_LOOPs, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                OptionsEditorFragment fragment = new OptionsEditorFragment();

                fragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner viewLifecycleOwner) {
                        // The fragment’s view has just been created
                        if (viewLifecycleOwner != null) {
                            navController.setViewModelStore(new ViewModelStore());
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.optionsEditorFragment);
                            Navigation.setViewNavController(fragment.requireView(), navController);
                        }
                    }
                });
                return fragment;
            }
        });
    }

    private void chooseSortOption(String option){
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
    }

    @Test
    public void testSortByAlphabet(){
        launchFragment();
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_alphabetically));
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .check(matches(withText("Vegetable")));
    }

    private void mimicAddOptionRequest(){
        bundle.putSerializable("add Option",option3);
    }

    @Test
    public void testAddOptionRequest(){
        mimicAddOptionRequest();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(2)
                .check(matches(withText(option3)));
    }
    private void mimicEditRequest(){
        bundle.putSerializable("edit option",option3);
        bundle.putInt("edit option",1);
    }
    @Test
    public void testEditRecipeRequest(){
        mimicEditRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(1)
                .check(matches(withText(option3)));
    }
    public void mimicDeleteRecipeRequest() {
        bundle.putBoolean("deletedFlag", true);
        bundle.putInt("edit option",0);
        bundle.putSerializable("edit option",option3);
    }
    @Test
    public void testDeleteRecipeRequest(){
        mimicDeleteRecipeRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .check(matches(withText(option3)));
    }

    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }
    @Test
    public void clickOnRecipe(){
        mimicAddOptionRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(2)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                perform(click());
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("SelectedOption"),option3);
        assertEquals(returnValue.getInt("SelectedOptionIndex"),2);
        assertEquals(returnValue.getInt("fromWhichFragment"),R.layout.fragment_recipe_collection);
    }

}
