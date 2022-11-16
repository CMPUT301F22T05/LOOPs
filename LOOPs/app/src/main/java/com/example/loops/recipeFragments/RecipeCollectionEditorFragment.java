package com.example.loops.recipeFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

/**
 * Recipe collection fragment for showing the recipes and also allowing manipulation
 * of the recipes in the collection
 */
public class RecipeCollectionEditorFragment extends RecipeCollectionFragment {

    public RecipeCollectionEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Sets the UI layout of the view it creates
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        //collectionTitle.setText(R.string.recipeCollection);
        return fragmentView;
    }

    /**
     * Parses the arguments specified by navigation graph actions.
     * Sets the ingredient collection from the arguments and adds ingredient sent by form to
     * its ingredient collection
     */
    void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        RecipeCollectionEditorFragmentArgs argsBundle
                = RecipeCollectionEditorFragmentArgs.fromBundle(getArguments());
        // Set the type of the recipe collection
        RecipeCollectionFragment.CollectionType collectionType = argsBundle.getCollectionType();
        setRecipeCollectionToDisplay(collectionType);

        // If any form had returned a recipe, update it to the collection
        Recipe submittedRecipe = argsBundle.getAddedRecipe();
        if (submittedRecipe != null) {
            Log.e("ohh", "this is problem");
            recipeCollection.addRecipe(submittedRecipe);
        }
        // If recipe edited, update or delete
        Recipe editedRecipe = argsBundle.getEditedRecipe();
        int editedRecipeIndex = argsBundle.getEditedRecipeIndex();
        if (editedRecipe != null) {
            Log.e("old", recipeCollection.getRecipe(editedRecipeIndex).getDocumentName());
            Log.e("new", editedRecipe.getDocumentName());
            if (argsBundle.getDeletedFlag() == false) { //update recipe
                recipeCollection.updateRecipe(editedRecipeIndex, editedRecipe);
            }
            else {
                recipeCollection.deleteRecipe(editedRecipeIndex);
            }
        }
        getArguments().clear();
    }

    /**
     * Opens the add recipe form
     * @param clickedView
     */
    void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addRecipeFromCollection);
    }

    /**
     * Opens recipe view details
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    void onClickRecipe(AdapterView<?> parent, View view, int position, long id) {
        Recipe selectedRecipe = recipeCollection.getRecipe(position);
        NavDirections viewRecipeDetailsAction =
                (NavDirections) RecipeCollectionEditorFragmentDirections.actionRecipeCollectionToRecipe(
                        selectedRecipe, position, R.layout.fragment_recipe_collection);
        Navigation.findNavController(getView()).navigate(viewRecipeDetailsAction);
    }
}
