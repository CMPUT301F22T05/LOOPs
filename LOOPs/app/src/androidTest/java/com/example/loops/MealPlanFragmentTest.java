package com.example.loops;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.ingredientFragments.IngredientCollectionSelectionFragment;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionEditorFragment;
import com.example.loops.recipeFragments.RecipeCollectionSelectionFragment;
import com.example.loops.recipeFragments.forms.EditRecipeFormFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Intent test for MealPlanFragment
 */
@RunWith(AndroidJUnit4.class)
public class MealPlanFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<MealPlanFragment> fragmentScenario;
    private MealPlan mockMealPlan;
    private Recipe mockRecipe1;
    private Recipe mockRecipe2;
    private Ingredient mockIngredient1;
    private Ingredient mockIngredient2;
    private Bundle bundle;
    private IngredientCollection mockIngredientCollection;
    private RecipeCollection mockRecipeCollection;

    /**
     * Sets up variables that are mocked
     */
    @Before
    public void setUp(){
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        navController.setViewModelStore(new ViewModelStore());
        IngredientCollection recipeIngredients = new IngredientCollection();
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap image = BitmapFactory.decodeResource(targetContext.getResources(),R.drawable.friedchicken);
        mockRecipe1 = new Recipe(
                "fried chicken",
                0,
                45,
                4,
                "Lunch",
                image,
                recipeIngredients,
                "Fried Chicken"
                );
        image = BitmapFactory.decodeResource(targetContext.getResources(),R.drawable.hamburger_test_image);
        mockRecipe2 = new Recipe(
                "Hamburger",
                0,
                45,
                2,
                "Lunch",
                image,
                recipeIngredients,
                "God Bless America"
        );
        mockIngredient1 = new Ingredient(
                "Carrot",
                "10/24/22",
                "Fridge",
                2,
                "#",
                "snack");
        mockIngredient2 = new Ingredient(
                "Apple",
                "10/24/22",
                "Fridge",
                2,
                "#",
                "snack"

        );
        mockRecipeCollection = new RecipeCollection();
        mockRecipeCollection.addRecipe(mockRecipe1);
        mockIngredientCollection = new IngredientCollection();
        mockIngredientCollection.addIngredient(mockIngredient1);
        mockMealPlan = new MealPlan("mockMealPlan",mockIngredientCollection,mockRecipeCollection);
        bundle = new Bundle();
        bundle.putSerializable("mealPlan",mockMealPlan);
        bundle.putInt("mealPlanIndex",0);
    }

    /**
     * Launches a fragment with no triggers on observers in the fragment
     */
    public void launchFragment() {
        /*
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */

        fragmentScenario = FragmentScenario.launchInContainer(MealPlanFragment.class, bundle,R.style.AppTheme ,new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                MealPlanFragment mealPlanFragment = new MealPlanFragment();
                mealPlanFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.mealPlanFragment,bundle);
                            Navigation.setViewNavController(mealPlanFragment.requireView(), navController);
                        }



                        }
                    });
                    return mealPlanFragment;
                }

            });

    }

    /**
     * Click on the save button
     */
    private void clickSave(){
        onView(withId(R.id.save_button)).perform(click());
    }

    /**
     * Click on the add button
     */
    private void clickAdd(){
        onView(withId(R.id.add_button)).perform(click());
    }

    /**
     * Launches a fragment that has received a result that an recipe has been added
     */
    private void mimicAddRecipeRequest(){
        BaseRecipeCollection addedRecipeCollection = new BaseRecipeCollection();
        addedRecipeCollection.addRecipe(mockRecipe2);
        fragmentScenario = FragmentScenario.launchInContainer(MealPlanFragment.class, bundle,R.style.AppTheme , new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                MealPlanFragment mealPlanFragment = new MealPlanFragment();
                mealPlanFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.mealPlanFragment,bundle);
                            Navigation.setViewNavController(mealPlanFragment.requireView(), navController);
                            navController.getCurrentBackStackEntry().getSavedStateHandle()
                                    .set(RecipeCollectionSelectionFragment.RESULT_KEY,addedRecipeCollection);
                        }



                    }
                });
                return mealPlanFragment;
            }

        });
    }

    /**
     * Assertions for the size of a list view
     * @param size size of list view
     * @return boolean
     */
    public static Matcher<View> withListSize (final int size) {
        /*
         * https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso
         * Date accessed: 11/25/2022
         */
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount() == size;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }


    /**
     * Launches a fragment that has receive a result that adds an ingredient
     */
    private void mimicAddIngredientRequest(){
        IngredientCollection addedIngredientCollection = new IngredientCollection();
        addedIngredientCollection.addIngredient(mockIngredient2);
        fragmentScenario = FragmentScenario.launchInContainer(MealPlanFragment.class, bundle,R.style.AppTheme , new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                MealPlanFragment mealPlanFragment = new MealPlanFragment();
                mealPlanFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.mealPlanFragment,bundle);
                            Navigation.setViewNavController(mealPlanFragment.requireView(), navController);
                            navController.getCurrentBackStackEntry().getSavedStateHandle()
                                    .set(IngredientCollectionSelectionFragment.RESULT_KEY,addedIngredientCollection);
                        }



                    }
                });
                return mealPlanFragment;
            }

        });
    }

    /**
     * Gets arguments of the next fragment that a button in
     * MealPlanFragment navigates to
     * @return
     */
    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }
    /*
     * Check if listviews  in the ui is displaying items correctly
     */
    @Test
    public void testDisplay(){
        launchFragment();
        onView(withId(R.id.recipes_listView)).check(matches(withListSize(1)));
        onView(withId(R.id.ingredients_listView)).check(matches(withListSize(1)));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_title_in_collection)
                ,withText(containsString(mockRecipe1.getTitle()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_category_in_collection)
                        ,withText(containsString(mockRecipe1.getCategory()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_servings_in_collection)
                        ,withText(containsString(Integer.toString(mockRecipe1.getNumServing())))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getDescription()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_amount_in_shopping_list)
                        ,withText(containsString(Integer.toString(2)))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_unit_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getUnit()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getCategory()))))));

    }

    /**
     * Test clicking the save button on Fragment and check the arguments that it sends
     */
    @Test
    public void testClickingSave(){
        launchFragment();
        clickSave();
        assertEquals(navController.getCurrentDestination().getId(),R.id.mealPlanHomePageFragment);
        Bundle returnVal = getReturnBundle();
        assertEquals(returnVal.getSerializable("updatedMealPlan"),mockMealPlan);
        assertEquals(returnVal.getInt("mealPlanIndex"),0);
    }

    /**
     * Test clicking the add button if it brings up the dialog fragment
     * and then click the cancel option in the dialog fragment
     */
    @Test
    public void testCancelOption(){
        launchFragment();
        clickAdd();
        String title = "What do you want to add to the meal plan?";
        String recipesOption = "Recipes";
        String ingredientsOption = "Ingredients";
        String cancel = "Cancel";
        String[] expected = {
                title,
                recipesOption,
                ingredientsOption,
                cancel
        };
        for (String e:expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(cancel))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
    }

    /**
     * Test clicking the add button if it brings up the dialog fragment
     * and then click the recipe option in the dialog fragment to see if it navigates
     * to the right fragment
     */
    @Test
    public void testRecipeOption(){
        launchFragment();
        clickAdd();
        String title = "What do you want to add to the meal plan?";
        String recipesOption = "Recipes";
        String ingredientsOption = "Ingredients";
        String cancel = "Cancel";
        String[] expected = {
                title,
                recipesOption,
                ingredientsOption,
                cancel
        };
        for (String e:expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(recipesOption))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeCollectionSelectionFragment);
    }

    /**
     * Test clicking the add button if it brings up the dialog fragment
     * and then click the ingredient option in the dialog fragment to see if it navigates
     * to the right fragment
     */
    @Test
    public void testIngredientOption(){
        launchFragment();
        clickAdd();
        String title = "What do you want to add to the meal plan?";
        String recipesOption = "Recipes";
        String ingredientsOption = "Ingredients";
        String cancel = "Cancel";
        String[] expected = {
                title,
                recipesOption,
                ingredientsOption,
                cancel
        };
        for (String e:expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(ingredientsOption))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
        assertEquals(navController.getCurrentDestination().getId(),R.id.ingredientCollectionSelectionFragment);
    }

    /**
     * Test the MealPlanFragment if it has an ingredient added
     */
    @Test
    public void testIngredientReceived(){
        mimicAddIngredientRequest();
        onView(withId(R.id.ingredients_listView)).check(matches(withListSize(2)));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getDescription()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_amount_in_shopping_list)
                        ,withText(containsString(Integer.toString(2)))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_unit_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getUnit()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_shopping_list)
                        ,withText(containsString(mockIngredient1.getCategory()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_shopping_list)
                        ,withText(containsString(mockIngredient2.getDescription()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.ingredient_amount_in_shopping_list)
                        ,withText(containsString(Integer.toString(2)))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.ingredient_unit_in_shopping_list)
                        ,withText(containsString(mockIngredient2.getUnit()))))));
        onData(is(instanceOf(Ingredient.class))).inAdapterView(withId(R.id.ingredients_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.ingredient_category_in_shopping_list)
                        ,withText(containsString(mockIngredient2.getCategory()))))));


    }

    /**
     * Test the MealPlanFragment if it has a recipe added
     */
    @Test
    public void testRecipeReceived(){
        mimicAddRecipeRequest();
        onView(withId(R.id.recipes_listView)).check(matches(withListSize(2)));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_title_in_collection)
                        ,withText(containsString(mockRecipe1.getTitle()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_category_in_collection)
                        ,withText(containsString(mockRecipe1.getCategory()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(0).check(matches(hasDescendant(allOf(withId(R.id.recipe_servings_in_collection)
                        ,withText(containsString(Integer.toString(mockRecipe1.getNumServing())))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.recipe_title_in_collection)
                        ,withText(containsString(mockRecipe2.getTitle()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.recipe_category_in_collection)
                        ,withText(containsString(mockRecipe2.getCategory()))))));
        onData(is(instanceOf(Recipe.class))).inAdapterView(withId(R.id.recipes_listView))
                .atPosition(1).check(matches(hasDescendant(allOf(withId(R.id.recipe_servings_in_collection)
                        ,withText(containsString(Integer.toString(mockRecipe2.getNumServing())))))));

    }
}
