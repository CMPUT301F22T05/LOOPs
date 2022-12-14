package com.example.loops.recipeFragments.forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.navigation.NavDirections;

import androidx.navigation.Navigation;

import com.example.loops.adapters.RecipeIngredientsAdapter;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

/**
 * A recipe form for editing recipes. Supply the recipe to edit through action args
 */
public class EditRecipeFormFragment extends RecipeFormFragment {

    private Recipe editRecipe;
    private int editRecipeInd;
    private boolean initialized = false;

    /**
     * Set up event listeners and sets button text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        initializeFormWithIngredientAttributes();
    }

    /**
     * On recipe's ingredient click, open edit ingredient form
     * @param position
     */
    @Override
    public void OnItemClick(int position) {
        super.OnItemClick(position);
        NavDirections actionEditRecipeIngredient = EditRecipeFormFragmentDirections
                .actionEditRecipeFormFragmentToEditRecipeIngredientFormFragment(
                        ingredientCollection.getIngredients().get(position)
                );
        Navigation.findNavController(getView()).navigate(actionEditRecipeIngredient);
    }

    /**
     * Parses the arguments of the fragment
     */
    void parseArguments() {
        editRecipe = EditRecipeFormFragmentArgs.fromBundle(getArguments())
                .getEditRecipe();
        editRecipeInd = EditRecipeFormFragmentArgs.fromBundle(getArguments())
                .getEditRecipeIndex();
    }

    /**
     * Gets the index for where 'value' is at in spinner
     * @param value
     * @param spinner
     */
    protected int getSpinnerIndexByValue(String value, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Populates the edit form with data of the recipe
     */
    public void initializeFormWithIngredientAttributes() {
        if ( !initialized ) {
            titleInput.setText(editRecipe.getTitle());
            prepTimeHourInput.setText(Integer.toString((int)editRecipe.getPrepTime().toHours()));
            prepTimeMinuteInput.setText(Integer.toString((int)editRecipe.getPrepTime().toMinutes() % 60));
            categoryInput.setSelection(getSpinnerIndexByValue(editRecipe.getCategory(), categoryInput));
            numServingInput.setText(Integer.toString(editRecipe.getNumServing()));
            commentsInput.setText(editRecipe.getComments());
            ingredientCollection.getIngredients().addAll(editRecipe.getIngredients().getIngredients());
            if (editRecipe.getPhoto() != null)
                imageView.setImageBitmap(editRecipe.getPhoto());

            initialized = true;
        }

    }

    /**
     * Shows the default category to recipe's original category
     * @return
     */
    @Override
    protected String getDefaultCategory() {
        return editRecipe.getCategory();
    }

    /**
     * Send the edited recipe back to previous fragment.
     * @param submittedRecipe recipe submitted by the form
     */
    protected void sendResult(Recipe submittedRecipe) {
        EditRecipeFormFragmentDirections.ActionEditRecipeFormFragmentToRecipeFragment editAction =
                EditRecipeFormFragmentDirections.actionEditRecipeFormFragmentToRecipeFragment(
                        submittedRecipe,
                        editRecipeInd,
                        0
                );
        Navigation.findNavController(getView()).navigate(editAction);
    }

    /**
     * Opens a dialog to select where to choose ingredients from and directs the user to
     * the appropriate fragment
     */
    void openSelectionForWhereToSelectIngredientsFrom() {
        CharSequence[] ingredientSelectionOptions = new CharSequence[]{
                "By New Ingredient",
                "From Ingredient Storage",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog ingredientSelectionPrompt = builder
                .setTitle( "How do you want to select an ingredient?" )
                .setItems(ingredientSelectionOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // navigate to add ingredient form
                        if (i == 0) {
                            NavDirections addIngredientAction = EditRecipeFormFragmentDirections
                                    .addIngredientToEditRecipeForm();
                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        else if (i == 1) {
                            EditRecipeFormFragmentDirections.ActionEditRecipeFormFragmentToIngredientCollectionSelectionFragment addIngredientAction
                                    = EditRecipeFormFragmentDirections.actionEditRecipeFormFragmentToIngredientCollectionSelectionFragment();
                            addIngredientAction.setIngredientsToFilter(ingredientCollection);
                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        else if (i == 2) {
                            return;
                        }
                        else {
                            throw new Error("Invalid selection");
                        }
                    }
                })
                .create();
        ingredientSelectionPrompt.show();
    }
}