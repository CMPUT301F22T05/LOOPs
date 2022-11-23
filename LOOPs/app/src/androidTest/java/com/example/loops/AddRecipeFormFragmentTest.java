package com.example.loops;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.loops.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.recipeFragments.forms.AddRecipeFormFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

/**
 * Testing AddRecipeFormFragment in isolation
 */

@RunWith(AndroidJUnit4.class)
public class AddRecipeFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<AddRecipeFormFragment> fragmentScenario;
    private Bundle args;
    private IngredientCollection mockIngredientCollection;

    /**
     * Rule for mocking intents
     */
    @Rule
    public IntentsRule intentsRule = new IntentsRule();
    @Before
    public void setup(){
        args = new Bundle();
        mockIngredientCollection = new IngredientCollection();
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        navController.setViewModelStore(new ViewModelStore());





    }

    /**
     * Creates an AddRecipeFormFragment for testing
     */
    public void launchFragment(){
        /*
         * https://developer.android.com/guide/navigation/navigation-testing#test_navigationui_with_fragmentscenario
         * Date Accessed : 2022-11-19
         */
        fragmentScenario = FragmentScenario.launchInContainer(AddRecipeFormFragment.class,args,new FragmentFactory(){
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                AddRecipeFormFragment addRecipeFormFragment = new AddRecipeFormFragment();

                addRecipeFormFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner != null){
                            navController.setGraph(R.navigation.nav_graph);
                            navController.setCurrentDestination(R.id.addRecipeFormFragment,args);
                            Navigation.setViewNavController(addRecipeFormFragment.requireView(), navController);
                        }



                    }
                });
                return addRecipeFormFragment;
            }

        });
        /*
         * Stubs result from camera
         */

        Instrumentation.ActivityResult result = cameraResultStub();
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

    }
    /*
     * Mocks an IngredientCollection object
     */

    private void mimicIngredientCollection(){
        Ingredient salt = new Ingredient("kosher salt", "11/14/2025","Pantry",1,"tsp","Spice");
        mockIngredientCollection.addIngredient(salt);
        args.putSerializable("ingredientCollection",mockIngredientCollection);
    }

    /**
     * Gets arguments of the next fragment that a button in
     * AddRecipeFragment navigates to
     * @return
     */
    private Bundle getReturnBundle() {
        return navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments();
    }

    /**
     * Returns a photo that will be used in testing
     * @return mocked Camera result
     */
    private Instrumentation.ActivityResult cameraResultStub(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.drawable.hamburger_test_image
        ));
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK,resultData);
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
        onView(withId(id)).perform(clearText(), typeText(toType), ViewActions.closeSoftKeyboard());
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
     * Seeing if warning comes up when nothing is submitted
     */
    @Test
    public void testSubmitNothing(){
        mimicIngredientCollection();
        launchFragment();
        clickSubmit();
        onView(withText("Please fill out the form properly")).check(matches(isDisplayed()));
    }

    /**
     * Seeing if warning comes up when no title is submitted
     */
    @Test
    public void testTitleNotSubmitted(){
        launchFragment();
        setComments("This is a comment");
        setCategory("Breakfast");
        setHourDuration("1");
        setMinuteDuration("0");
        setNumServing("4");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_title))).check(matches(isDisplayed()));
    }

    /**
     * Seeing if warning comes up when no comments is submitted
     */
    @Test
    public void testNoCommentSubmitted(){
        launchFragment();
        setTitle("Test Recipe");
        setCategory("Snack");
        setHourDuration("1");
        setMinuteDuration("0");
        setNumServing("4");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_Comment))).check(matches(isDisplayed()));
    }

    /**
     * Seeing if warning comes up when no prepTime is submitted
     */
    @Test
    public void testNoDurationInput(){
        launchFragment();
        setCategory("Snack");
        setNumServing("4");
        setTitle("Test Recipe");
        setComments("This is a comment");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_duration))).check(matches(isDisplayed()));
    }

    /**
     * Seeing if warning comes up when no number of serving is submitted
     */
    @Test
    public void testNoNumServing(){
        launchFragment();
        setTitle("Test Recipe");
        setCategory("Snack");
        setHourDuration("1");
        setMinuteDuration("0");
        setComments("This is a comment");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_numServ))).check(matches(isDisplayed()));
    }

    /**
     * Test if a valid Recipe is submitted, no warnings will come out
     * and navigates to recipeCollectionEditorFragment
     */
    @Test
    public void testValidRecipe(){
        launchFragment();
        fragmentScenario.onFragment(fragment -> {
            navController.setCurrentDestination(R.id.recipeCollectionEditorFragment);
            navController.navigate(R.id.addRecipeFormFragment);
        });
        IngredientCollection testIngredients = new IngredientCollection();
        Bitmap hamburger = BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.drawable.hamburger_test_image);
        Recipe typedRecipe = new Recipe(
                "Hamburger",
                0,
                45,
                1,
                "Lunch",
                hamburger,
                testIngredients,
                "Very Delicious"

        );
        setTitle("Hamburger");
        setCategory("Lunch");
        setNumServing("1");
        setComments("Very Delicious");
        setMinuteDuration("40");
        setHourDuration("0");
        onView(withId(R.id.imageView)).perform(click());
        clickSubmit();
        sleep(1000);
        assertEquals(navController.getCurrentDestination().getId(), R.id.recipeCollectionEditorFragment);


    }

    /**
     * Test if a photo taken with the camera is displayed and inputted fields are not changed
     */
    @Test
    public void testCamera(){
        launchFragment();
        Bitmap hamburger = BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.drawable.hamburger_test_image);
        setTitle("Hamburger");
        setCategory("Lunch");
        setNumServing("1");
        setComments("Very Delicious");
        setMinuteDuration("40");
        setHourDuration("0");
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
        onView(withId(R.id.recipeFormCommentsInput)).check(matches(withText("Very Delicious")));
        onView(withId(R.id.recipeFormPrepTimeMinuteInput)).check(matches(withText("40")));
        onView(withId(R.id.recipeFormPrepTimeHourInput)).check(matches(withText("0")));
        onView(withId(R.id.recipeFormNumServingInput)).check(matches(withText("1")));
        onView(withId(R.id.recipeFormTitleInput)).check(matches(withText("Hamburger")));
        onView(withId(R.id.recipeFormCategoryInput)).check(matches(withSpinnerText("Lunch")));
    }

    /**
     * Testing the cancel option in the dialog fragment that comes up when
     * pressing the AddIngredient button
     */
    @Test
    public void testCancelOption() {
        launchFragment();
        onView(withId(R.id.recipeFormAddIngredientButton)).perform(click());
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
        mimicIngredientCollection();
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



}
