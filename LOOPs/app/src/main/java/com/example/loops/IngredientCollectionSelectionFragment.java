package com.example.loops;

import android.os.Bundle;

import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Date;

/**
 * Ingredient collection fragment for selecting an ingredient
 */
public class IngredientCollectionSelectionFragment extends IngredientCollectionFragment {
    public static final String RESULT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    public static final String INGREDIENT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY_INGREDIENT";
    public static final int FROM_STORAGE = 0;
    public static final int FROM_TESTING = 1;   // FIXME: temporary. Just to show it works

    public IngredientCollectionSelectionFragment() {
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
        Bundle argsBundle = getArguments();
        if (argsBundle != null) {
            getIngredientCollectionToDisplay();

            Ingredient submittedIngredient = IngredientCollectionSelectionFragmentArgs.fromBundle(argsBundle).getAddedIngredient();
            if (submittedIngredient != null) {
                sendSelectedIngredientToCaller(submittedIngredient);
            }
            argsBundle.clear();
        }
    }

    void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addIngredientFormFromSelection);
    }

    void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        sendSelectedIngredientToCaller(collectionViewAdapter.getItem(position));
    }

    void sendSelectedIngredientToCaller(Ingredient selectedIngredient) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            Bundle resultBundle = new Bundle();
            resultBundle.putSerializable(INGREDIENT_KEY, selectedIngredient);
            getParentFragmentManager().setFragmentResult(RESULT_KEY, resultBundle);
        }
//        else if ( callerFragmentId == R.id.addRecipeFormFragment ) {
//            IngredientCollectionSelectionFragmentDirections.AddIngredientToAddRecipeForm toSubmitAction =
//                    IngredientCollectionSelectionFragmentDirections.
//                            addIngredientToAddRecipeForm();
//            toSubmitAction.setAddedIngredient(selectedIngredient);
//            Navigation.findNavController(getView()).navigate(toSubmitAction);
//        }
    }

//  FIXME: needs better design
    void getIngredientCollectionToDisplay() {
        Bundle argsBundle = getArguments();
        int collectionTypeId = IngredientCollectionSelectionFragmentArgs.fromBundle(argsBundle).getCollectionType();
        switch(collectionTypeId) {
            case -1:    // FIXME: hardcoded to this for now
                Log.e("TESTING", "-1 in getIngredientCollectionToDisplay in selection");
            case FROM_STORAGE:
                ingredientCollection = ((MainActivity)getActivity()).allIngredients;
                break;
            case FROM_TESTING:
                ingredientCollection = new IngredientCollection();
                ingredientCollection.addIngredient(new Ingredient(
                        "test 1",
                        new Date(0),
                        "Test Location",
                        69,
                        "Test Unit",
                        "Test Category"
                ));
                ingredientCollection.addIngredient(new Ingredient(
                        "test 2",
                        new Date(0),
                        "Test Location",
                        69,
                        "Test Unit",
                        "Test Category"
                ));
                ingredientCollection.addIngredient(new Ingredient(
                        "test 3",
                        new Date(0),
                        "Test Location",
                        69,
                        "Test Unit",
                        "Test Category"
                ));
                break;
            case -5:
                break;
            default:
                throw new RuntimeException("Invalid collection type id for ingredient selection fragment");
        }
    }

    private Integer getCallerFragmentId() {
        NavController navController = Navigation.findNavController(getView());
        NavBackStackEntry previousFragment = navController.getPreviousBackStackEntry();
        if (previousFragment == null)
            return null;
        return previousFragment.getDestination().getId();
    }
}