package com.example.loops.recipeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.R;
import com.example.loops.Recipe;

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
        collectionTitle.setText(R.string.recipeCollection);
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
            recipeCollection.addRecipe(submittedRecipe);
        }
        // TODO: handle editted recipes
//        Recipe editedRecipe = argsBundle.getEditedRecipe();
//        if (editedIngredient != null) {
//            if (argsBundle.getDeleteFlag() == false) { //update ingredient
//                ingredientCollection.updateIngredient(argsBundle.getEditedIngredientIndex(), editedIngredient);
//            }
//            else {
//                ingredientCollection.deleteIngredient(argsBundle.getEditedIngredientIndex());
//            }
//        }
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
                        selectedRecipe, position
                );
        Navigation.findNavController(getView()).navigate(viewRecipeDetailsAction);
    }
}
