package com.example.loops;

import android.os.Bundle;

import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Ingredient collection fragment for selecting an ingredient
 */
public class IngredientCollectionSelectionFragment extends IngredientCollectionFragment {
    public static final String RESULT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    public static final String INGREDIENT_KEY = "SELECT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY_INGREDIENT";

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
            Ingredient submittedIngredient = IngredientCollectionSelectionFragmentArgs.fromBundle(argsBundle).getAddedIngredient();
            if (submittedIngredient != null) {
                sendSelectedIngredientToCaller(submittedIngredient);
            }
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

    void getIngredientCollectionToDisplay() {
        ingredientCollection = ((MainActivity)getActivity()).allIngredients;
    }

    private Integer getCallerFragmentId() {
        NavController navController = Navigation.findNavController(getView());
        NavBackStackEntry previousFragment = navController.getPreviousBackStackEntry();
        if (previousFragment == null)
            return null;
        return previousFragment.getDestination().getId();

    }
}