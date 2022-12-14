package com.example.loops.recipeFragments.forms;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.loops.ingredientFragments.forms.IngredientFormFragment;
import com.example.loops.models.Ingredient;

/**
 * A ingredient form for editing recipe's ingredient. Supply the ingredient to edit through action args
 */
public class EditRecipeIngredientFormFragment extends RecipeIngredientFormFragment {
    public static final String RESULT_KEY = "EDIT_RECIPE_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    private Ingredient editedIngredient;

    /**
     * Initializes the form with ingredient's attributes and sets button text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        initializeFormWithIngredientAttributes();
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("save");
    }

    /**
     * Returns the default category to show in the category spinner by default
     * @return the default category option
     */
    @Override
    protected String getDefaultCategory() {
        return editedIngredient.getCategory();
    }

    /**
     * Returns the default location to show in the location spinner by default
     * @return the default location option
     */
    @Override
    protected String getDefaultLocation() {
        return editedIngredient.getStoreLocation();
    }

    /**
     * Initializes the form's input with the attributes of the ingredient to edit
     */
    public void initializeFormWithIngredientAttributes() {
        editedIngredient = EditRecipeIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredient();

        descriptionInput.setText(editedIngredient.getDescription());
        amountInput.setText(Double.toString(editedIngredient.getAmount()));
        unitInput.setSelection(getSpinnerIndexByValue(editedIngredient.getUnit(), unitInput));
        categoryInput.setSelection(getSpinnerIndexByValue(editedIngredient.getCategory(), categoryInput));
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
