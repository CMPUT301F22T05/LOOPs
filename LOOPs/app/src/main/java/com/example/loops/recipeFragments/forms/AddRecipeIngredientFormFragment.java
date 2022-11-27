package com.example.loops.recipeFragments.forms;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.R;
import com.example.loops.ingredientFragments.forms.IngredientFormFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

import java.time.LocalDate;

public class AddRecipeIngredientFormFragment extends RecipeIngredientFormFragment {
    public static final String RESULT_KEY = "ADD_RECIPE_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";

//    public AddRecipeIngredientFormFragment() {}

    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
    }

    /**
     * Sends back the result through navcontroller's saved state handle with key RESULT_KEY
     * @param submittedIngredient ingredient submitted by the form
     */
    @Override
    protected void sendResult(Ingredient submittedIngredient) {
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                submittedIngredient
        );
        Navigation.findNavController(getView()).popBackStack();
    }
}
