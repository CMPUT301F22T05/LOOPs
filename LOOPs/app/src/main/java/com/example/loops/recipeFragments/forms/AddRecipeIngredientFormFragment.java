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

public class AddRecipeIngredientFormFragment extends IngredientFormFragment {

    private IngredientCollection ingredients;

    public AddRecipeIngredientFormFragment() {}

    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
        hideUnusedInput(formView);
        getCachedRecipe();
    }

    public void sendResult(Ingredient submittedIngredient) {
        ingredients.addIngredient(submittedIngredient);
        AddRecipeIngredientFormFragmentDirections.ActionAddRecipeIngredientFormFragmentToAddRecipeFormFragment
                addRecipeIngredientAction = AddRecipeIngredientFormFragmentDirections
                .actionAddRecipeIngredientFormFragmentToAddRecipeFormFragment();
        addRecipeIngredientAction.setIngredientCollection(ingredients);
        Navigation.findNavController(getView()).navigate(addRecipeIngredientAction);
    }

    private void getCachedRecipe() {
        ingredients = AddRecipeIngredientFormFragmentArgs.fromBundle(getArguments())
                .getIngredientCollection();
    }

    private void hideUnusedInput(View formView) {
        TextView bestBeforeDateText = formView.findViewById(R.id.ingredientFormBestBeforeDateText);
        bestBeforeDateText.setVisibility(View.GONE);
        bestBeforeDateInput.setVisibility(View.GONE);
        bestBeforeDateInput.setText("2022-01-01");

        TextView locationText = formView.findViewById(R.id.ingredientFormLocationText);
        locationText.setVisibility(View.GONE);
        locationInput.setSelection(1);
        locationInput.setVisibility(View.GONE);
    }
}
