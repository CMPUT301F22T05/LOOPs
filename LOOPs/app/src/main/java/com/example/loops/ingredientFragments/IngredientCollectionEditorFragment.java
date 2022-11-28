package com.example.loops.ingredientFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.ingredientFragments.forms.AddIngredientFormFragment;
import com.example.loops.factory.IngredientCollectionFactory.CollectionType;
import com.example.loops.models.Ingredient;
import com.example.loops.R;

/**
 * Ingredient collection fragment for showing the ingredients and also allowing manipulation
 * of the ingredients in the collection
 */
public class IngredientCollectionEditorFragment extends IngredientCollectionFragment {
    protected Button addButton;

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
        addButton = fragmentView.findViewById(R.id.add_ingredient_button);
        return fragmentView;
    }

    /**
     * Reads in any arguments of the fragment and set up listeners for the UI
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAddButtonListener();
        setOnAddIngredientBehavior();
    }

    /**
     * Sets the listener for the add button
     */
    private void setAddButtonListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddButton(v);
            }
        });
    }

    /**
     * Opens the add ingredient form
     * @param clickedView
     */
    protected void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addIngredientFromCollection);
    }

    /**
     * Handles the behavior when an ingredient is added to the fragment through the navigation controller
     */
    private void setOnAddIngredientBehavior() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( AddIngredientFormFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object ingredient) {
                ingredientCollection.addIngredient((Ingredient) ingredient);
                savedStateHandle.remove( AddIngredientFormFragment.RESULT_KEY );
            }
        });
    }

    /**
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    @Override
    protected int getUIViewId() {
        return R.layout.fragment_ingredient_collection_editor;
    }

    /**
     * Returns the ingredient collection type that is being displayed
     * @return
     */
    @Override
    public CollectionType getCollectionType() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionSelectionFragmentArgs argsBundle
                = IngredientCollectionSelectionFragmentArgs.fromBundle(getArguments());
        CollectionType collectionType = argsBundle.getCollectionType();
        return collectionType;
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
            }
        }
        getArguments().clear();
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
                        selectedIngredient, position);
        Navigation.findNavController(view).navigate(viewIngredientDetailsAction);
    }
}
