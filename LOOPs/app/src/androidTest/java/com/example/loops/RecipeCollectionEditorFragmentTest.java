package com.example.loops;


import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionEditorFragment;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeCollectionEditorFragmentTest {
    private TestNavHostController navController;
    private FragmentScenario<RecipeCollectionEditorFragment> fragmentScenario;
    private Bundle bundle;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
}
