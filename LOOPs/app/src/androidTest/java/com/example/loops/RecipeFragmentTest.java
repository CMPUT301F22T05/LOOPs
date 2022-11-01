package com.example.loops;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.core.content.res.ResourcesCompat;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RecipeFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<RecipeFragment> fragmentScenario;
    private Recipe mockRecipe;
    private IngredientCollection mockIngredientCollection;

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
        bundle.putSerializable("SelectedRecipe",mockRecipe);
        bundle.putInt("SelectedRecipeIndex",0);
        fragmentScenario = FragmentScenario.launchInContainer(RecipeFragment.class,bundle);

        fragmentScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
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



    }
    /*
    Checks if the text/image is matching
    Still need to work on image matching
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
        ArrayList<Ingredient> testIngredients = mockIngredientCollection.getIngredients();
        Integer length = testIngredients.size();
        for (int i =0;i<length;i++){
            break;
        }

    }



}
