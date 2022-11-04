package com.example.loops;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditRecipeFragment extends RecipeFormFragment {

    private Recipe editRecipe;
    private int editRecipeInd;

    public EditRecipeFragment() { }

    /**
     * Set up event listeners and sets button text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Edit");

        initializeFormWithIngredientAttributes();
    }

    protected int getSpinnerIndexByValue(String value, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    // TODO: Implement this
    public void initializeFormWithIngredientAttributes() {
        editRecipe = EditRecipeFragmentArgs.fromBundle(getArguments())
                .getEditRecipe();
        editRecipeInd = EditRecipeFragmentArgs.fromBundle(getArguments())
                .getEditRecipeIndex();

        titleInput.setText(editRecipe.getTitle());
        prepTimeHourInput.setValue((int)editRecipe.getPrepTime().toHours());
        prepTimeMinuteInput.setValue((int)editRecipe.getPrepTime().toMinutes());
        categoryInput.setSelection(getSpinnerIndexByValue(editRecipe.getCategory(), categoryInput));
        numServingInput.setText(Integer.toString(editRecipe.getNumServing()));
        commentsInput.setText(editRecipe.getComments());

        ingredientCollection = editRecipe.getIngredients();
    }

    // TODO: Implement this
    void sendResult(Recipe submittedRecipe) {
        return;
    }
}