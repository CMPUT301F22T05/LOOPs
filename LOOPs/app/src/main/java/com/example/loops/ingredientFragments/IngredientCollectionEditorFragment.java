package com.example.loops.ingredientFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.models.Ingredient;
import com.example.loops.MainActivity;
import com.example.loops.R;

/**
 * Ingredient collection fragment for showing the ingredients and also allowing manipulation
 * of the ingredients in the collection
 */
public class IngredientCollectionEditorFragment extends IngredientCollectionFragment {

    public IngredientCollectionEditorFragment() {
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
        //collectionTitle.setText(R.string.ingredientCollection);
        return fragmentView;
    }

    /**
     * Parses the arguments specified by navigation graph actions.
     * Sets the ingredient collection from the arguments and adds ingredient sent by form to
     * its ingredient collection
     * Notify changes to the database.
     */
    protected void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionEditorFragmentArgs argsBundle
                = IngredientCollectionEditorFragmentArgs.fromBundle(getArguments());
        // Set the type of the ingredient collection
        CollectionType collectionType = argsBundle.getCollectionType();
        setIngredientCollectionToDisplay(collectionType);
        // If any form had returned an ingredient, update it to collection
        Ingredient submittedIngredient = argsBundle.getAddedIngredient();
        if (submittedIngredient != null) {
            ingredientCollection.addIngredient(submittedIngredient);
        }
        else{
            int ingredientIndex = argsBundle.getEditedIngredientIndex();
            if (argsBundle.getDeleteFlag() == false) { //update ingredient
                Ingredient editedIngredient = argsBundle.getEditedIngredient();
                if (editedIngredient != null) {
                    ingredientCollection.updateIngredient(ingredientIndex, editedIngredient);
                }
            }
            else { //delete ingredient
                ingredientCollection.deleteIngredient(ingredientIndex);
//                try {
//                    ((MainActivity) getActivity()).deleteIngredientFromDatabase(argsBundle.getEditedIngredientIndex());
//                } catch (Exception e) {}
            }
        }
        getArguments().clear();
//        try {
//            ((MainActivity) getActivity()).updateIngredientFromDatabase(ingredientCollection);
//        } catch (Exception e) {
//            return;
//        }
    }

    /**
     * Opens the add ingredient form
     * @param clickedView
     */
    protected void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addIngredientFromCollection);
    }

    /**
     * Opens ingredient view details
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        Ingredient selectedIngredient = ingredientCollection.getIngredients().get(position);
        NavDirections viewIngredientDetailsAction =
                (NavDirections)IngredientCollectionEditorFragmentDirections.actionViewIngredientDetails(
                        selectedIngredient, position, R.layout.fragment_ingredient_collection);
        Navigation.findNavController(view).navigate(viewIngredientDetailsAction);
    }
}
