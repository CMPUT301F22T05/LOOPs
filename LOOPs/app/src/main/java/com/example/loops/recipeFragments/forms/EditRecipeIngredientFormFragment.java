package com.example.loops.recipeFragments.forms;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.ingredientFragments.forms.IngredientFormFragment;
import com.example.loops.models.Ingredient;

public class EditRecipeIngredientFormFragment extends AddRecipeIngredientFormFragment {
    public static final String RESULT_KEY = "EDIT_RECIPE_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";

    private Ingredient editedIngredient;
    private int editedIngredientIndex;

    public EditRecipeIngredientFormFragment() {}

    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("save");
        initializeFormWithIngredientAttributes();
    }

    public void initializeFormWithIngredientAttributes() {
        editedIngredient = EditRecipeIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredient();
        editedIngredientIndex = EditRecipeIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredientIndex();

        descriptionInput.setText(editedIngredient.getDescription());
        amountInput.setText(Double.toString(editedIngredient.getAmount()));
        unitInput.setSelection(getSpinnerIndexByValue(editedIngredient.getUnit(), unitInput));
        categoryInput.setSelection(getSpinnerIndexByValue(editedIngredient.getCategory(), categoryInput));
    }
}
