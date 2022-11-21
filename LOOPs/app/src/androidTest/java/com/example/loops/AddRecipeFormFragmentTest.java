package com.example.loops;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import com.example.loops.models.Recipe;

import androidx.activity.result.ActivityResultLauncher;
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
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.recipeFragments.forms.AddRecipeFormFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddRecipeFormFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<AddRecipeFormFragment> fragmentScenario;

    @Rule
    public IntentsRule intentsRule = new IntentsRule();
    @Before
    public void setup(){
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        navController.setViewModelStore(new ViewModelStore());
        fragmentScenario = FragmentScenario.launchInContainer(AddRecipeFormFragment.class,null,new FragmentFactory(){
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                AddRecipeFormFragment addRecipeFormFragment = new AddRecipeFormFragment();

                addRecipeFormFragment.getViewLifecycleOwnerLiveData().observeForever(new Observer<LifecycleOwner>() {
                    @Override
                    public void onChanged(LifecycleOwner lifecycleOwner) {
                        navController.setGraph(R.navigation.nav_graph);
                        navController.setCurrentDestination(R.id.addRecipeFormFragment);
                        Navigation.setViewNavController(addRecipeFormFragment.requireView(), navController);
                    }
                });
                return addRecipeFormFragment;
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
    private void selectSpinnerOption(int id, String option) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(option))).perform(click());
    }
    private void setTitle(String title){
        typeToEditText(R.id.recipeFormTitleInput,title);
    }

    private void setCategory(String category) {
        selectSpinnerOption(R.id.recipeFormCategoryInput, category);
    }
    private void clickSubmit(){
        onView(withId(R.id.recipeFormSubmitButton)).perform(click());
    }
    private void setNumServing(String numServing){
        typeToEditText(R.id.recipeFormNumServingInput,numServing);
    }
    private void setComments(String comments){
        typeToEditText(R.id.recipeFormCommentsInput,comments);
    }
    private void clickAddIngredient(){
        onView(withId(R.id.recipeFormAddIngredientButton)).perform(click());
    }
    private void setHourDuration(String hour){
        typeToEditText(R.id.recipeFormPrepTimeHourInput, hour);
    }
    private void setMinuteDuration (String minute){
        typeToEditText(R.id.recipeFormPrepTimeMinuteInput,minute);
    }
    private Recipe getSubmittedRecipe(){
        Recipe submittedRecipe = (Recipe) navController.getBackStack()
                .get(navController.getBackStack().size()-1)
                .getArguments()
                .getSerializable("addedRecipe");
        return submittedRecipe;
    }

    @Test
    public void testSubmitNothing(){
        clickSubmit();
        onView(withText("Please fill out the form properly")).check(matches(isDisplayed()));
    }

    @Test
    public void testTitleNotSubmitted(){
        setComments("This is a comment");
        setCategory("Breakfast");
        setHourDuration("1");
        setMinuteDuration("0");
        setNumServing("4");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_title))).check(matches(isDisplayed()));
    }
    @Test
    public void testNoCommentSubmitted(){
        setTitle("Test Recipe");
        setCategory("Snack");
        setHourDuration("1");
        setMinuteDuration("0");
        setNumServing("4");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_Comment))).check(matches(isDisplayed()));
    }

    @Test
    public void testNoDurationInput(){
        setCategory("Snack");
        setNumServing("4");
        setTitle("Test Recipe");
        setComments("This is a comment");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_duration))).check(matches(isDisplayed()));
    }

    @Test
    public void testNoNumServing(){
        setTitle("Test Recipe");
        setCategory("Snack");
        setHourDuration("1");
        setMinuteDuration("0");
        setComments("This is a comment");
        clickSubmit();
        onView(withText(getString(R.string.recipe_no_numServ))).check(matches(isDisplayed()));
    }

    @Test
    public void testValidRecipe(){

    }
    @Test
    public void testCamera(){

    }


}
