package com.example.loops;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;

import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeFragment;

import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Tests for the Recipe fragment
 */
@RunWith(AndroidJUnit4.class)
public class RecipeFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<RecipeFragment> fragmentScenario;
    private Recipe mockRecipe;
    private IngredientCollection mockIngredientCollection;

    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }


    @Before
    public void setUp() throws ParseException {
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        Ingredient eggs = new Ingredient("eggs", "11/14/2022","Fridge",2,"","Breakfast");
        Ingredient water = new Ingredient("water", "11/14/2024","Fridge",1,"cups","Liquid");
        Ingredient paprika = new Ingredient("paprika", "11/14/2025","Pantry",1,"tbsp","Spice");
        Ingredient pepper = new Ingredient("black pepper", "11/14/2025","Pantry",3,"tsp","Spice");
        Ingredient poultrySeasoning = new Ingredient("poultry seasoning", "11/14/2025","Pantry",2,"tsp","Spice");
        Ingredient salt = new Ingredient("kosher salt", "11/14/2025","Pantry",1,"tsp","Spice");
        Ingredient oil = new Ingredient("canola oil", "11/14/2025","Pantry",1,"quart","Spice");
        Ingredient chicken = new Ingredient("chicken", "11/14/2022","Fridge",4,"lbs","Meat");
        mockIngredientCollection = new IngredientCollection();
        mockIngredientCollection.addIngredient(eggs);
        mockIngredientCollection.addIngredient(water);
        mockIngredientCollection.addIngredient(paprika);
        mockIngredientCollection.addIngredient(pepper);
        mockIngredientCollection.addIngredient(poultrySeasoning);
        mockIngredientCollection.addIngredient(salt);
        mockIngredientCollection.addIngredient(oil);
        mockIngredientCollection.addIngredient(chicken);
        mockRecipe = new Recipe("Fried Chicken", 4);
        mockRecipe.setIngredients(mockIngredientCollection);
        mockRecipe.setCategory("Southern American");
        String comment = String.join("\n",
                "1. In a large shallow dish, combine 2-2/3 cups flour, garlic salt, paprika, 2-1/2 teaspoons pepper and poultry seasoning. In another shallow dish, beat eggs and 1-1/2 cups water; add salt and the remaining 1-1/3 cups flour and 1/2 teaspoon pepper. Dip chicken in egg mixture, then place in flour mixture, a few pieces at a time. Turn to coat",
                "",
                "2. In a deep-fat fryer, heat oil to 375°. Fry chicken, several pieces at a time, until chicken is golden brown and juices run clear, 7-8 minutes on each side. Drain on paper towels.");
        mockRecipe.setComments(comment);
        mockRecipe.setPrepTime(Duration.ofSeconds(900));
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap image = BitmapFactory.decodeResource(targetContext.getResources(),R.drawable.fried_chicken_test_picutre);
        mockRecipe.setPhoto(image);
        Bundle bundle = new Bundle();
        bundle.putInt("fromWhichFragment", R.layout.fragment_recipe_collection);
        bundle.putSerializable("SelectedRecipe",mockRecipe);
        bundle.putInt("SelectedRecipeIndex",0);
        fragmentScenario = FragmentScenario.launchInContainer(RecipeFragment.class,bundle);
        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.recipeFragment,bundle);
            Navigation.setViewNavController(fragment.requireView(), navController);

        });



    }
    /*
    Checks if UI components are being displayed
     */
    @Test
    public void testDisplay(){

        onView(withId(R.id.editRecipeButton)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeImage)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.prepTimeString)).check(matches(isDisplayed()));
        onView(withId(R.id.recipePrepTime)).check(matches(isDisplayed()));
        onView(withId(R.id.categoryInRecipe)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.servingSizeString)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeServing)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeCommentSubhead)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeComment)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredientSubHead)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeIngredientList)).perform(scrollTo());
        onView(withId(R.id.recipeIngredientList)).check(matches(isDisplayed()));
        onView(withId(R.id.backToRecipeCollection)).check(matches(isDisplayed()));
        onView(withId(R.id.deleteRecipeButton)).check(matches(isDisplayed()));




    }
    /*
    Checks if the text on a textview matches with a recipe attribute
     */
    @Test
    public void testMatchingText(){
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String prepTime = targetContext.getResources().getString(R.string.prepTime);
        String servingSize = targetContext.getResources().getString(R.string.servingSize);
        String recipeCommentSubHead = targetContext.getResources().getString(R.string.recipeCommentSubHead);
        String ingredientSubHead = targetContext.getResources().getString(R.string.ingredientSubHead);
        String categoryInRecipe = targetContext.getResources().getString(R.string.categoryInRecipe);
        String editRecipeText = targetContext.getResources().getString(R.string.EditRecipeButtonName);
        String deleteButtonText = targetContext.getResources().getString(R.string.recipeDeleteButtonText);
        String backButtonText = targetContext.getResources().getString(R.string.recipeBackButtonText);
        String recipeServingSize = mockRecipe.getNumServing() + "";
        Duration time = mockRecipe.getPrepTime();
        long seconds = time.getSeconds();
        String durationString = String.format("%d:%02d",seconds/3600,(seconds%3600)/60);

        onView(withId(R.id.editRecipeButton)).check(matches(withText(editRecipeText)));
        onView(withId(R.id.recipeTitle)).check(matches(withText(mockRecipe.getTitle())));
        onView(withId(R.id.prepTimeString)).check(matches(withText(prepTime)));
        onView(withId(R.id.recipePrepTime)).check(matches(withText(durationString)));
        onView(withId(R.id.servingSizeString)).check(matches(withText(servingSize)));
        onView(withId(R.id.recipeServing)).check(matches(withText(recipeServingSize)));
        onView(withId(R.id.categoryInRecipe)).check(matches(withText(categoryInRecipe)));
        onView(withId(R.id.recipeCategory)).check(matches(withText(mockRecipe.getCategory())));

        onView(withId(R.id.recipeCommentSubhead)).check(matches(withText(recipeCommentSubHead)));
        onView(withId(R.id.recipeComment)).check(matches(withText(mockRecipe.getComments())));
        onView(withId(R.id.ingredientSubHead)).check(matches(withText(ingredientSubHead)));

        /*
         * This part of the code checks if each item in the recyclerview displays
         *  an ingredient's description, amount, and unit properly by scrolling to that item
         *  if it exist
         */

        ArrayList<Ingredient> testIngredients = mockIngredientCollection.getIngredients();
        Integer length = testIngredients.size();
        for (int i = 0; i<length; i++){
            Ingredient oneIngredient =  testIngredients.get(i);
            String oneIngredientDescription = oneIngredient.getDescription();
            onView(withId(R.id.recipeIngredientList)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(oneIngredientDescription))));


        }
        onView(withId(R.id.backToRecipeCollection)).check(matches(withText(backButtonText)));
        onView(withId(R.id.deleteRecipeButton)).check(matches(withText(deleteButtonText)));



    }

    /**
     * Checking if swiping an ingredient to the right deletes it
     */
    @Test
    public void testDeleteSwipe(){
        onView(withId(R.id.recipeIngredientList)).perform(scrollTo());
        ArrayList<Ingredient> testIngredients = mockIngredientCollection.getIngredients();
        Ingredient deletedIngredient = testIngredients.get(2);
        onView(withId(R.id.recipeIngredientList)).perform(RecyclerViewActions.scrollToPosition(2),swipeRight());
        assertNotEquals(deletedIngredient,testIngredients.get(2));

    }

    /**
     * Checks if the back button returns an edited/not recipe to recipeCollectionEditorFragment
     */
    @Test
    public void testBackButton(){
        onView(withId(R.id.backToRecipeCollection)).perform(scrollTo(), click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.recipeCollectionEditorFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedRecipe"),mockRecipe);
        assertEquals(returnValue.getInt("editedRecipeIndex"),0);
        assertEquals(returnValue.getBoolean("deleteFlag"),false);

    }

    /**
     * Checks if navigating to edit recipe gives proper arguments
     */
    @Test
    public void testNavigateToEditRecipe() {
        onView(withId(R.id.editRecipeButton)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(),R.id.EditRecipePlaceHolder);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedRecipe"), mockRecipe);
        assertEquals(returnValue.getInt("editedRecipe"),0);
    }

    /**
     * Checks if pop up to confirm a deletion of a recipe is displaying the right things
     *  and that pressing no does nothing to the recipe and returns to recipeFragment
     */
    @Test
    public void testDeleteButtonCancel() {
        onView(withId(R.id.deleteRecipeButton)).perform(scrollTo(), click());
        String warning = "Delete " + mockRecipe.getTitle() + " recipe";
        String[] expected = {
                "Warning",
                warning,
                "no",
                "yes"
        };
        for (String e: expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onView(withId(R.id.delete_popup_no_button)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.recipeFragment);
    }

    /**
     * Checks if pop up to confirm a deletion of a recipe is displaying the right things
     *   and that pressing yes sends a signal for a recipe to be deleted and returns to
     *   recipeCollectionEditorFragment
     */
    @Test
    public void testDeleteButtonConfirm() {
        onView(withId(R.id.deleteRecipeButton)).perform(scrollTo(), click());
        String warning = "Delete " + mockRecipe.getTitle() + " recipe";
        String[] expected = {
                "Warning",
                warning,
                "no",
                "yes"
        };
        for (String e: expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onView(withId(R.id.delete_popup_yes_button)).perform(click());
        assertEquals(navController.getCurrentDestination().getId(), R.id.recipeCollectionEditorFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedRecipe"),mockRecipe);
        assertEquals(returnValue.getInt("editedRecipeIndex"), 0);
        assertEquals(returnValue.getBoolean("deleteFlag"),true);
    }





}
