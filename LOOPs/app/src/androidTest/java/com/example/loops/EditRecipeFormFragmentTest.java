package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.loops.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;

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
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.forms.AddRecipeFormFragment;
import com.example.loops.recipeFragments.forms.EditRecipeFormFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Testing EditRecipeFormFragment in isolation
 */

public class EditRecipeFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<EditRecipeFormFragment> fragmentScenario;
    private Bundle args;
    private IngredientCollection mockIngredientCollection;
    private Recipe mockRecipe;
    private Integer mockRecipeIndex;

    /**
     * Rule for mocking intents
     */
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Before
    public void setup() {
        args = new Bundle();
        mockIngredientCollection = new IngredientCollection();
        navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        navController.setViewModelStore(new ViewModelStore());
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

        Integer numServing = 4;
        String title = "FriedChicken";
        String category = "Southern American";
        long hours = 0;
        long minutes = 15;
        String comment = String.join("\n",
                "1. In a large shallow dish, combine 2-2/3 cups flour, garlic salt, paprika, 2-1/2 teaspoons pepper and poultry seasoning. In another shallow dish, beat eggs and 1-1/2 cups water; add salt and the remaining 1-1/3 cups flour and 1/2 teaspoon pepper. Dip chicken in egg mixture, then place in flour mixture, a few pieces at a time. Turn to coat",
                "",
                "2. In a deep-fat fryer, heat oil to 375°. Fry chicken, several pieces at a time, until chicken is golden brown and juices run clear, 7-8 minutes on each side. Drain on paper towels.");
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap image = BitmapFactory.decodeResource(targetContext.getResources(),R.drawable.friedchicken);
        mockRecipe = new Recipe(title, hours, minutes, numServing, category, image, mockIngredientCollection,comment);
        args.putSerializable("editRecipe",mockRecipe);
        args.putInt("editRecipeIndex",0);

    }

    /**
     * Creates an EditRecipeFormFragment for testing
     */
    public void launchFragment() {
        /*
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */

        fragmentScenario = FragmentScenario.launchInContainer(EditRecipeFormFragment.class, args, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                EditRecipeFormFragment editRecipeFormFragment = new EditRecipeFormFragment();

                editRecipeFormFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.editRecipeFormFragment,args);
                            Navigation.setViewNavController(editRecipeFormFragment.requireView(), navController);
                        }



                    }
                });
                return editRecipeFormFragment;
            }

        });
        /*
         * Stubs result from camera
         */

        Instrumentation.ActivityResult result = cameraResultStub();
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

    }

    private Instrumentation.ActivityResult cameraResultStub() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.drawable.hamburger_test_image
        ));
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }
    /**
     * Method to type given string into an editText of the id given and close the keyboard
     * @param id
     * @param toType
     */
    private void typeToEditText(int id, String toType) {
        onView(withId(id)).perform(clearText(), typeText(toType), ViewActions.closeSoftKeyboard());

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
     * Method to select an item corresponding to option in the spinner that has id
     * @param id
     * @param option
     */
    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }

    /**
     * Type in the title in recipeFormTitleInput editText
     * @param title
     */
    private void setTitle(String title){
        typeToEditText(R.id.recipeFormTitleInput,title);
    }

    /**
     * Pick the category in the recipeFormCategoryInput spinner
     * @param category
     */
    private void setCategory(String category) {
        selectSpinnerOption(R.id.recipeFormCategoryInput, category);
    }

    /**
     * Click on the ADD button to add the recipe
     */
    private void clickSubmit(){
        onView(withId(R.id.recipeFormSubmitButton)).perform(click());
    }

    /**
     * Type in the serving size in recipeFormNumServingInput editText
     * @param numServing
     */
    private void setNumServing(String numServing){
        typeToEditText(R.id.recipeFormNumServingInput,numServing);
    }

    /**
     * Type in the comments in recipeFormCommentsInput editText
     * @param comments
     */
    private void setComments(String comments){
        typeToEditText(R.id.recipeFormCommentsInput,comments);
    }

    /**
     * Click on the AddIngredientButton
     */
    private void clickAddIngredient(){
        onView(withId(R.id.recipeFormAddIngredientButton)).perform(click());
    }

    /**
     * Set the hours in the recipeFormPrepTimeHourInput editText
     * @param hour
     */
    private void setHourDuration(String hour){
        typeToEditText(R.id.recipeFormPrepTimeHourInput, hour);
    }

    /**
     * Set the minutes in the recipeFormPrepTimeMinuteInput editText
     * @param minute
     */
    private void setMinuteDuration (String minute){
        typeToEditText(R.id.recipeFormPrepTimeMinuteInput,minute);
    }

    /**
     * Gets arguments of the next fragment that a button in
     * EditRecipeFragment navigates to
     * @return
     */
    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }

    /**
     * Testing when no changes has been made, the returned recipe should still be the same
     */
    @Test
    public void testNoChanges(){
        launchFragment();
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertEquals(returnRecipe.getTitle(),mockRecipe.getTitle());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Testing when title is only thing changed has been made, the recipe returned should have
     * different title
     */
    @Test
    public void testChangeTitle(){
        launchFragment();
        setTitle("Cheese");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getTitle(),mockRecipe.getTitle());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Seeing if warning comes up when no title is submitted
     */
    @Test
    public void testNoTitle(){
        launchFragment();
        setTitle("");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_title))).check(matches(isDisplayed()));;
    }

    /**
     * Testing when comments is only thing changed has been made, the recipe returned should have
     * different comments
     */
    @Test
    public void testChangeComments(){
        launchFragment();
        setComments("I Love Chicken");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getComments(),mockRecipe.getComments());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Seeing if warning comes up when no comments is submitted
     */
    @Test
    public void testNoComments(){
        launchFragment();
        setComments("");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_Comment))).check(matches(isDisplayed()));

    }

    /**
     * Testing when prepTime is only thing changed has been made, the recipe returned should have
     * different prepTime
     */
    @Test
    public void testChangeDuration(){
        launchFragment();
        setMinuteDuration("1");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getPrepTime(),mockRecipe.getPrepTime());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Seeing if warning comes up when no prepTime is submitted
     */
    @Test
    public void testNoDuration(){
        launchFragment();
        setMinuteDuration("");
        setHourDuration("");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_duration))).check(matches(isDisplayed()));
    }

    /**
     * Testing when number of serving is only thing changed has been made, the recipe returned should have
     * different number of serving
     */
    @Test
    public void testChangeNumServing(){
        launchFragment();
        setNumServing("1");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getNumServing(),mockRecipe.getNumServing());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Seeing if warning comes up when no number of serving is submitted
     */
    @Test
    public void testNoNumServing(){
        launchFragment();
        setNumServing("");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_numServ))).check(matches(isDisplayed()));
    }

    /**
     * Testing when category is only thing changed has been made, the recipe returned should have
     * different category
     */
    @Test
    public void testChangeCategory(){
        launchFragment();
        setCategory("Breakfast");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getCategory(),mockRecipe.getCategory());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Seeing if warning comes up when required fields are empty
     */
    @Test
    public void testNoRequiredFields(){
        launchFragment();
        setNumServing("");
        setMinuteDuration("");
        setHourDuration("");
        setComments("");
        setTitle("");
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        onView(withText("Please fill out the form properly")).check(matches(isDisplayed()));
    }

    /**
     * Testing when photo is only thing changed has been made, the recipe returned should have
     * different photo
     */
    @Test
    public void testChangePhoto(){
        launchFragment();
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
        onView(withId(R.id.recipeFormSubmitButton)).perform(scrollTo());
        clickSubmit();
        assertEquals(navController.getCurrentDestination().getId(),R.id.recipeFragment);
        Bundle returnValue = getReturnBundle();
        Recipe returnRecipe = (Recipe) returnValue.getSerializable("SelectedRecipe");
        assertNotEquals(returnRecipe.getPhoto(),mockRecipe.getPhoto());
        assertEquals(returnValue.getInt("SelectedRecipeIndex"),0);
    }

    /**
     * Testing the cancel option in the dialog fragment that comes up when
     * pressing the AddIngredient button
     */
    @Test
    public void testCancelOption(){
        launchFragment();
        clickAddIngredient();
        String title = "How do you want to select an ingredient?";
        String byNew = "By New Ingredient";
        String fromStorage = "From Ingredient Storage";
        String cancel = "Cancel";
        String[] expected = {
                title,
                byNew,
                fromStorage,
                cancel
        };
        for (String e: expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(cancel))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
    }

    /**
     * Testing the By New Ingredient option in the dialog fragment that comes up when
     * pressing the AddIngredient button
     */
    @Test
    public void testByNewIngredientOption(){
        launchFragment();
        clickAddIngredient();
        String title = "How do you want to select an ingredient?";
        String byNew = "By New Ingredient";
        String fromStorage = "From Ingredient Storage";
        String cancel = "Cancel";
        String[] expected = {
                title,
                byNew,
                fromStorage,
                cancel
        };
        for (String e: expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(byNew))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
        assertEquals(navController.getCurrentDestination().getId(), R.id.addRecipeIngredientFormFragment);
    }

    /**
     * Testing the From Ingredient Storage option in the dialog fragment that comes up when
     * pressing the AddIngredient button
     */
    @Test
    public void testFromIngredientStorageOption(){
        launchFragment();
        clickAddIngredient();
        String title = "How do you want to select an ingredient?";
        String byNew = "By New Ingredient";
        String fromStorage = "From Ingredient Storage";
        String cancel = "Cancel";
        String[] expected = {
                title,
                byNew,
                fromStorage,
                cancel
        };
        for (String e: expected){
            onView(withText(equalToIgnoringCase(e))).check(matches(isDisplayed()));
        }
        onData(allOf(instanceOf(String.class),is(fromStorage))).perform(click());
        onView(withText(cancel)).check(doesNotExist());
        assertEquals(navController.getCurrentDestination().getId(), R.id.ingredientCollectionSelectionFragment);

    }

    /**
     * Test if clicking an ingredient navigates to editRecipeIngredientFormFragment
     */
    @Test
    public void testEditIngredient(){
        launchFragment();
        onView(withId(R.id.recipeFormIngredientRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        assertEquals(navController.getCurrentDestination().getId(), R.id.editRecipeIngredientFormFragment);
        Bundle returnValue = getReturnBundle();
        assertEquals(returnValue.getSerializable("editedIngredient"),mockIngredientCollection.getIngredients().get(2));
    }


}