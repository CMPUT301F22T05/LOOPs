package com.example.loops.recipeFragments.forms;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.R;
import com.example.loops.ingredientFragments.forms.IngredientFormFragment;
import com.example.loops.models.Ingredient;

/**
 * The ingredient form fragment specifically used for recipe's ingredients
 */
public abstract class RecipeIngredientFormFragment extends IngredientFormFragment  {

//    public RecipeIngredientFormFragment() {}

    /**
     * sets up the view
     * @param formView view of the ingredient form
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        hideUnusedInput(formView);
    }

    /**
     * Hides the unused inputs of ingredient fragment form
     * @param formView
     */
    private void hideUnusedInput(View formView) {
        // Maybe, this isn't the best idea? Maybe we should refactor the ingredient form fragment
        // so we can choose the UI layout we want to use?
        TextView bestBeforeDateText = formView.findViewById(R.id.ingredientFormBestBeforeDateText);
        bestBeforeDateText.setVisibility(View.GONE);
        bestBeforeDateInput.setVisibility(View.GONE);

        TextView locationText = formView.findViewById(R.id.ingredientFormLocationText);
        locationText.setVisibility(View.GONE);
        locationInput.setVisibility(View.GONE);
    }

    /**
     * Gets the inputted ingredients in the form
     * @return
     */
    @Override
    protected Ingredient getInputtedIngredient() {
        String description = descriptionInput.getText().toString();
        double amount = parseAmountFromInput();
        String unit = unitInput.getSelectedItem().toString();
        String category = categoryInput.getSelectedItem().toString();

        Ingredient inputtedIngredient = new Ingredient(
                description,
                amount,
                unit,
                category
        );
        inputtedIngredient.setPending(false);
        return inputtedIngredient;
    }
}
