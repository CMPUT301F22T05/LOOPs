package com.example.loops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditIngredientFragment extends IngredientFormFragment {

    public EditIngredientFragment() { }

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
    void sendResult(Ingredient submittedIngredient) {
        return;
    }
}