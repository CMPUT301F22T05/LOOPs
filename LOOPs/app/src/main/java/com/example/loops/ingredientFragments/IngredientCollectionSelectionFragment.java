package com.example.loops.ingredientFragments;

import android.os.Bundle;

import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.MainActivity;
import com.example.loops.R;

/**
 * Ingredient collection fragment for selecting an ingredient
 */
public class IngredientCollectionSelectionFragment extends IngredientCollectionFragment {
    public static final String RESULT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    public static final String INGREDIENT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY_INGREDIENT";
    private IngredientCollection chosenIngredients;

    public IngredientCollectionSelectionFragment() {
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
     * Sets the ingredient collection from the arguments and passes ingredients from forms back
     * to caller
     */
    protected void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionSelectionFragmentArgs argsBundle
                = IngredientCollectionSelectionFragmentArgs.fromBundle(getArguments());
        // Set the type of the ingredient collection
        CollectionType collectionType = argsBundle.getCollectionType();
        setIngredientCollectionToDisplay(collectionType);
        // If any form had returned an ingredient, send it back to this fragment's caller
        Ingredient submittedIngredient = argsBundle.getAddedIngredient();
        if (submittedIngredient != null) {
            chosenIngredients.addIngredient(submittedIngredient);
            sendIngredientsToCallerFragment(chosenIngredients);
        }
        chosenIngredients = argsBundle.getIngredientCollection();
        getArguments().clear();
        //((MainActivity)getActivity()).updateIngredientFromDatabase(ingredientCollection);
    }

    /**
     * Opens the add ingredient form
     * @param clickedView
     */
    protected void onClickAddButton(View clickedView) {
        //Navigation.findNavController(getView()).navigate(R.id.addIngredientFormFromSelection);
    }

    /**
     * Selects the clicked ingredient and sends it to caller fragment
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        chosenIngredients.addIngredient(new Ingredient(collectionViewAdapter.getItem(position)));
        sendIngredientsToCallerFragment(chosenIngredients);
    }

    /**
     * Sends the ingredient to the caller fragment
     * @param ingredients ingredients to send
     */
    void sendIngredientsToCallerFragment(IngredientCollection ingredients) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
//            Bundle resultBundle = new Bundle();
//            resultBundle.putSerializable(INGREDIENT_KEY, selectedIngredient);
//            getParentFragmentManager().setFragmentResult(RESULT_KEY, resultBundle);
        }
        else if ( callerFragmentId == R.id.addRecipeFormFragment ) {
            IngredientCollectionSelectionFragmentDirections.ActionIngredientCollectionSelectionFragmentToAddRecipeFormFragment toSubmitAction =
                    IngredientCollectionSelectionFragmentDirections.
                            actionIngredientCollectionSelectionFragmentToAddRecipeFormFragment();
            toSubmitAction.setIngredientCollection(ingredients);
            Navigation.findNavController(getView()).navigate(toSubmitAction);
        }
//        else if ( callerFragmentId == R.id.addRecipeFormFragment ) {
//            IngredientCollectionSelectionFragmentDirections.AddIngredientToAddRecipeForm toSubmitAction =
//                    IngredientCollectionSelectionFragmentDirections.
//                            addIngredientToAddRecipeForm();
//            toSubmitAction.setAddedIngredient(selectedIngredient);
//            Navigation.findNavController(getView()).navigate(toSubmitAction);
//        }
    }

    /**
     * Gets the id of the caller fragment
     * @return id of the caller fragment
     */
    private Integer getCallerFragmentId() {
        NavController navController = Navigation.findNavController(getView());
        NavBackStackEntry previousFragment = navController.getPreviousBackStackEntry();
        if (previousFragment == null)
            return null;
        return previousFragment.getDestination().getId();
    }
}