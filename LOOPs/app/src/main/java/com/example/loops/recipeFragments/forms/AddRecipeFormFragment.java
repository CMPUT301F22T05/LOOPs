package com.example.loops.recipeFragments.forms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.adapters.RecipeIngredientsAdapter;
import com.example.loops.ingredientFragments.IngredientCollectionFragment;
import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.R;
import com.example.loops.models.Recipe;

/**
 * A recipe form for adding recipes.
 */
public class AddRecipeFormFragment extends RecipeFormFragment {

    //private Recipe addedRecipe;
    // Empty constructor
    public AddRecipeFormFragment() { }

    @Override
    public void OnItemClick(int position) {
        super.OnItemClick(position);
        NavDirections actionEditRecipeIngredient = AddRecipeFormFragmentDirections
                .actionAddRecipeFormFragmentToEditRecipeIngredientFormFragment(
                        ingredientCollection.getIngredients().get(position),
                        position
                );
        Navigation.findNavController(getView()).navigate(actionEditRecipeIngredient);
    }

    /**
     * Sets the title text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
        //ingredientCollection = addedRecipe.getIngredients();
    }

    /**
     * Sends recipe to RecipeCollectionFragment through navigation graph action
     * @param submittedRecipe
     */
    protected void sendResult(Recipe submittedRecipe) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            throw new Error("No caller fragment");
        }
        else if ( callerFragmentId == R.id.recipeCollectionEditorFragment ) {
            AddRecipeFormFragmentDirections.SubmitRecipeToCollection toSubmitAction =
                    AddRecipeFormFragmentDirections.submitRecipeToCollection();
            toSubmitAction.setAddedRecipe(submittedRecipe);
            Navigation.findNavController(getView()).navigate((NavDirections) toSubmitAction);
        }
        else {
            throw new Error("Navigation action not defined");
        }
    }

    /**
     * Returns the id of the previous fragment
     * @return id of previous fragment. If no such fragment then null
     */
    private Integer getCallerFragmentId() {
        NavController navController = Navigation.findNavController(getView());
        NavBackStackEntry previousFragment = navController.getPreviousBackStackEntry();
        if (previousFragment == null)
            return null;
        return previousFragment.getDestination().getId();
    }


    /**
     * Opens dialog to select ingredients from. Either by creating a new ingredient
     * or selecting from storage
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
                            NavDirections addIngredientAction = AddRecipeFormFragmentDirections
                                    .addRecipeIngredientToAddRecipeForm();
                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        else if (i == 1) {
                            AddRecipeFormFragmentDirections.SelectIngredientForAddRecipeForm addIngredientAction
                                    = AddRecipeFormFragmentDirections.selectIngredientForAddRecipeForm();
                            addIngredientAction.setIngredientsToFilter(ingredientCollection);
                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        else {
                            throw new Error("Invalid selection");
                        }
                    }
                })
                .create();
        ingredientSelectionPrompt.show();
    }

    /**
     * Parses argument of this fragment
     */
    protected void parseArguments() {
        Bundle argsBundle = getArguments();
        if (argsBundle != null) {
            argsBundle.clear();
        }
    }
}