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

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.factory.RecipeCollectionFactory;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionEditorFragment;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeCollectionEditorFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<RecipeCollectionEditorFragment> fragmentScenario;
    private Bundle bundle;
    private Recipe recipe3;

    @Before
    public void setUp() {
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        bundle = new Bundle();
        bundle.putSerializable("collectionType",
                RecipeCollectionFactory
                    .CollectionType.PRESET_FOR_EDIT);

        IngredientCollection burgerIngredients = new IngredientCollection();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap image = BitmapFactory.decodeResource(targetContext.getResources(), R.drawable.hamburger_test_image);
        recipe3 = new Recipe(
                "Hamburger",
                0,
                45,
                2,
                "Lunch",
                image,
                burgerIngredients,
                "God Bless America"
        );
    }
    private void launchFragment(){
        fragmentScenario = FragmentScenario.launchInContainer(RecipeCollectionEditorFragment.class,bundle,R.style.Theme_LOOPs);
        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.recipeCollectionEditorFragment,bundle);
            Navigation.setViewNavController(fragment.requireView(), navController);

        });

    }



   private void chooseSortOption(String option){
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
   }

    @Test
    public void testSortByTitle(){
        launchFragment();
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_title));
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Grilled Cheese")));
        onView(withId(R.id.sort_order_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Pizza")));
    }
    @Test
    public void testSortByPrepTime(){
        launchFragment();
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_preptime));
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Grilled Cheese")));
        onView(withId(R.id.sort_order_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Pizza")));
    }
    @Test
    public void testSortByCategory(){
        launchFragment();
        chooseSortOption(ApplicationProvider.getApplicationContext()
                .getString(R.string.sort_by_category));
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Grilled Cheese")));
        onView(withId(R.id.sort_order_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Pizza")));
    }
    private void mimicAddRecipeRequest(){
        bundle.putSerializable("addedRecipe",recipe3);
    }
    @Test
    public void testAddRecipeRequest(){
        mimicAddRecipeRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(2)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Hamburger")));
    }
    private void mimicEditRecipeRequest(){
        bundle.putSerializable("editedRecipe",recipe3);
        bundle.putInt("editedRecipeIndex",1);
    }
    @Test
    public void testEditRecipeRequest(){
        mimicEditRecipeRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(1)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Hamburger")));
    }
    public void mimicDeleteRecipeRequest() {
        bundle.putBoolean("deletedFlag", true);
        bundle.putInt("editedRecipeIndex",0);
        bundle.putSerializable("editedRecipe",recipe3);
    }
    @Test
    public void testDeleteRecipeRequest(){
        mimicDeleteRecipeRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(0)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                check(matches(withText("Pizza")));
    }

    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }
    @Test
    public void clickOnRecipe(){
        mimicAddRecipeRequest();
        launchFragment();
        onData(anything()).inAdapterView(withId(R.id.generic_collection_view)).atPosition(2)
                .onChildView(withId(R.id.recipe_title_in_collection)).
                perform(click());
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("SelectedRecipe"),recipe3);
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),2);
        assertEquals(returnValue.getInt("fromWhichFragment"),R.layout.fragment_recipe_collection);
    }



}
