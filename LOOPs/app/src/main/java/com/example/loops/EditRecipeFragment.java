package com.example.loops;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditRecipeFragment extends RecipeFormFragment {

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
    }

    // TODO: Implement this
    public void initializeFormWithIngredientAttributes() {

        return;
    }

    // TODO: Implement this
    void sendResult(Recipe submittedRecipe) {
        return;
    }
}