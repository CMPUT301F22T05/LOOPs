package com.example.loops;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

/**
 * A recipe form for adding recipes.
 */
public class AddRecipeFormFragment extends RecipeFormFragment {

    private Recipe addedRecipe = new Recipe();
    // Empty constructor
    public AddRecipeFormFragment() { }

    /**
     * Set up event listeners and changes button text and parse arguments
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
        ingredientCollection = addedRecipe.getIngredients();
        setUpRecyclerView(formView);
        parseArguments();
    }

    /**
     * Sends recipe to RecipeCollectionFragment through navigation graph action
     * @param submittedRecipe
     */
    void sendResult(Recipe submittedRecipe) {
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
                            AddRecipeFormFragmentDirections.ActionAddRecipeFormFragmentToIngredientCollectionSelectionFragment addIngredientAction =
                                    AddRecipeFormFragmentDirections.actionAddRecipeFormFragmentToIngredientCollectionSelectionFragment();
                            addIngredientAction.setCollectionType(IngredientCollectionFragment.CollectionType.FROM_STORAGE);
                            Navigation.findNavController(getView()).navigate(addIngredientAction);
                        }
                        // navigate to add ingredient form
                        else if (i == 1) {
                            Navigation.findNavController(getView()).navigate(R.id.addIngredientFormFragment);
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

    /**
     * Parses argument of this fragment
     */
    void parseArguments() {
        Bundle argsBundle = getArguments();
        if (argsBundle != null) {
            Ingredient submittedIngredient = AddRecipeFormFragmentArgs.fromBundle(argsBundle).getAddedIngredient();
            if (submittedIngredient != null) {
                ingredientCollection.addIngredient(submittedIngredient);
                recyclerViewAdapter.notifyDataSetChanged();
                Log.e("TEST", submittedIngredient.getDescription());
            }
            argsBundle.clear();
        }
    }
}