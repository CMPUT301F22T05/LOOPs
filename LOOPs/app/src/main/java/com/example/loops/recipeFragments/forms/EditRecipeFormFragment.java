package com.example.loops.recipeFragments.forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.models.Recipe;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditRecipeFormFragment extends RecipeFormFragment {

    private Recipe editRecipe;
    private int editRecipeInd;

    public EditRecipeFormFragment() { }

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

    // Gets the index for where 'value' is at in spinner
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
        editRecipe = EditRecipeFormFragmentArgs.fromBundle(getArguments())
                .getEditRecipe();
        editRecipeInd = EditRecipeFormFragmentArgs.fromBundle(getArguments())
                .getEditRecipeIndex();

        titleInput.setText(editRecipe.getTitle());
        prepTimeHourInput.setValue((int)editRecipe.getPrepTime().toHours());
        prepTimeMinuteInput.setValue((int)editRecipe.getPrepTime().toMinutes());
        categoryInput.setSelection(getSpinnerIndexByValue(editRecipe.getCategory(), categoryInput));
        numServingInput.setText(Integer.toString(editRecipe.getNumServing()));
        commentsInput.setText(editRecipe.getComments());
        ingredientCollection = editRecipe.getIngredients();
    }

    /**
     * Send the edited recipe back to previous fragment.
     * Todo: implement
     * @param submittedRecipe recipe submitted by the form
     */
    protected void sendResult(Recipe submittedRecipe) {
        return;
    }

    void openSelectionForWhereToSelectIngredientsFrom() {
        CharSequence[] ingredientSelectionOptions = new CharSequence[]{
                "From Ingredient Storage",
                "By New Ingredient",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog ingredientSelectionPrompt = builder
                .setTitle( "How do you want to select an ingredient?" )
                .setItems(ingredientSelectionOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // navigate to ingredient storage
                        if (i == 0) {
//                            AddRecipeFormFragmentDirections.ActionAddRecipeFormFragmentToIngredientCollectionSelectionFragment addIngredientAction =
//                                    AddRecipeFormFragmentDirections.actionAddRecipeFormFragmentToIngredientCollectionSelectionFragment();
//                            addIngredientAction.setCollectionType(IngredientCollectionFragment.CollectionType.FROM_STORAGE);
//                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        // navigate to add ingredient form
                        else if (i == 1) {
//                            Navigation.findNavController(getView()).navigate(R.id.addIngredientFormFragment);
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