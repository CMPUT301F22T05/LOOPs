package com.example.loops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

/**
 * Ingredient collection fragment for showing the ingredients in storage
 */
public class IngredientCollectionEditorFragment extends IngredientCollectionFragment {

    public IngredientCollectionEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        collectionTitle.setText(R.string.ingredientCollection);
        return fragmentView;
    }

    void parseActionArguments() {
        IngredientCollectionEditorFragmentArgs args = IngredientCollectionEditorFragmentArgs.fromBundle(getArguments());
        //Ingredient submittedIngredient = IngredientCollectionFragmentArgs.fromBundle(getArguments()).getAddedIngredient();
        if (args.getAddedIngredient() != null) {
            ingredientCollection.addIngredient(args.getAddedIngredient());
        }
        else if (args.getEditedIngredient() != null) {
            if (args.getDeleteFlag() == false) { //update ingredient
                ingredientCollection.updateIngredient(args.getEditedIngredientIndex(), args.getEditedIngredient());
            }
            else {
                ingredientCollection.deleteIngredient(args.getEditedIngredientIndex());
            }
        }
        getArguments().clear();
    }

    void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addIngredientFromCollection);
    }

    void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        Ingredient selectedIngredient = ingredientCollection.getIngredients().get(position);
        NavDirections viewIngredientDetailsAction =
                (NavDirections)IngredientCollectionEditorFragmentDirections.actionViewIngredientDetails(
                        selectedIngredient, position, R.layout.fragment_ingredient_collection);
        Navigation.findNavController(view).navigate(viewIngredientDetailsAction);
    }

    void getIngredientCollectionToDisplay() {
        ingredientCollection = ((MainActivity)getActivity()).allIngredients;
    }
}
