package com.example.loops;

import android.os.Bundle;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class IngredientCollectionEditorFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<IngredientCollectionEditorFragment> fragmentScenario;

    @Before
    public void setUp() {
        navController = new TestNavHostController( ApplicationProvider.getApplicationContext() );
        Bundle bundle = new Bundle();
        bundle.putSerializable("collectionType", IngredientCollectionFragment.CollectionType.FOR_TEST_INGREDIENT_COLLECTION_EDITOR_FRAGMENT);
        fragmentScenario = FragmentScenario.launchInContainer(IngredientCollectionEditorFragment.class, bundle);

        fragmentScenario.onFragment(fragment -> {
            //setCurrentDestination
            navController.setGraph(R.navigation.nav_graph);
            Navigation.setViewNavController(fragment.requireView(), navController);
        });

        //IdlingPolicies.setMasterPolicyTimeout(5000, TimeUnit.HOURS);
        /*fragmentScenario.onFragment(new FragmentScenario.FragmentAction<IngredientCollectionEditorFragment>() {
            @Override
            public void perform(@NonNull IngredientCollectionEditorFragment ingredientCollectionEditorFragment) {

            }
        })*/
    }

    private void chooseSortOption(String option) {
        onView(withId(R.id.sort_option_spinner)).perform(click());
        onData(allOf(is(instanceOf(CharSequence.class)), is(option))).perform(click());
        //IdlingPolicies.setIdlingResourceTimeout(5000, TimeUnit.HOURS);

    }

    @Test
    public void testSortByDescription() {
        chooseSortOption(ApplicationProvider.getApplicationContext().getString(R.string.sort_by_description));
//        onData(allOf(instanceOf(Ingredient.class))).atPosition(0).check(matches(withText(containsString("AAA"))));
//        onData(is(instanceOf(IngredientStorageViewAdapter.class))).atPosition(0).check(matches(withText(containsString("AAA"))));
        onData(is(instanceOf(Ingredient.class)))
                .inAdapterView(withId(R.id.generic_collection_view))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.ingredient_description_in_storage), withText(containsString("AAA"))))));
    }
}
