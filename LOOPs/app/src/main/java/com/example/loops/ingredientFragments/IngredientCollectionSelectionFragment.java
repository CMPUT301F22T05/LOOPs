package com.example.loops.ingredientFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.loops.adapters.IngredientSelectionViewAdapter;
import com.example.loops.adapters.IngredientStorageViewAdapter;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.factory.IngredientCollectionFactory.CollectionType;
import com.example.loops.models.Ingredient;
import com.example.loops.R;
import com.example.loops.recipeFragments.forms.AddRecipeFormFragmentDirections;
import com.example.loops.validators.IngredientValidator;

import java.util.ArrayList;

/**
 * Ingredient collection fragment for selecting an ingredient
 */
public class IngredientCollectionSelectionFragment extends IngredientCollectionFragment {
    public static final String RESULT_KEY = "INGREDIENT_COLLECTION_SELECTION_FRAGMENT_RESULT_KEY";
    private Button saveButton;
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
        saveButton = fragmentView.findViewById(R.id.select_button);
        chosenIngredients = new IngredientCollection();
        setSaveButtonListener();
        return fragmentView;
    }

    /**
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    @Override
    protected int getUIViewId() {
        return R.layout.fragment_ingredient_collection_selection;
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
     * Sets the ingredient collection from the arguments and passes ingredients from forms back
     * to caller
     */
    protected void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionSelectionFragmentArgs argsBundle
                = IngredientCollectionSelectionFragmentArgs.fromBundle(getArguments());
        // Get ingredients to filter
        IngredientCollection filterIngredients = argsBundle.getIngredientsToFilter();
        if (filterIngredients != null) {
            for (Ingredient ing : filterIngredients.getIngredients()) {
                int index = ingredientCollection.getIngredients().indexOf(ing);
                if (index != -1) {
                    ingredientCollection.deleteIngredient(index);
                }
            }
        }
        getArguments().clear();
    }

    /**
     * Defines the behavior when save button is clicked
     */
    private void setSaveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIngredientsToCallerFragment();
            }
        });
    }

    /**
     * Selects the clicked ingredient and sends it to caller fragment
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        Ingredient selectedIngredient = collectionViewAdapter.getItem(position);

        // If already selected, unselect it
        if (chosenIngredients.getIngredients().contains(selectedIngredient)) {
            ((IngredientSelectionViewAdapter) collectionViewAdapter).selectItem(position);
            chosenIngredients.getIngredients().remove(selectedIngredient);
        }
        // If not, add it to selection
        else {
            openIngredientQuantityPrompt(selectedIngredient, position);
        }
    }

    /**
     * Opens the dialog to prompt for user to input quantity of selected ingredient
     * It also selects the ingredients in the adapter
     * @param selectedIngredient
     * @param position position of the selected ingredient in the adapter
     */
    private void openIngredientQuantityPrompt(Ingredient selectedIngredient, int position) {
        /**
         * https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
         * Author : Tom Bollwitt
         * Date Accessed : 2022-11-20
         */
        // Create view for the prompt
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View ingredientQuantityPromptView = inflater.inflate(R.layout.dialog_ingredient_quantity_prompt, null);
        EditText amountInput = ingredientQuantityPromptView.findViewById(R.id.dialog_amount_input);
        Spinner unitInput = ingredientQuantityPromptView.findViewById(R.id.dialog_unit_spinner);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog setIngredientQuantityPrompt = builder
                .setTitle( "Select Quantity for " + selectedIngredient.getDescription() )
                .setView(ingredientQuantityPromptView)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;     // Do nothing on cancel
                    }
                })
                .create();
        // We implement the positive button on click listener separately to prevent the
        // dialog closing on button click automatically. We want to control when it closes.
        setIngredientQuantityPrompt.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = setIngredientQuantityPrompt.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get user input
                        double amount = getAmountFromInput(amountInput);
                        String unit = unitInput.getSelectedItem().toString();
                        // Validate user input
                        IngredientValidator validator = new IngredientValidator();
                        boolean valid = validator.checkAmount(amount, IngredientValidator.INGREDIENT_TYPE.STORED);
                        valid = validator.checkUnit(unit, IngredientValidator.INGREDIENT_TYPE.STORED) && valid;

                        if (valid) {
                            selectedIngredient.setAmount(amount);
                            selectedIngredient.setUnit(unit);
                            chosenIngredients.addIngredient(selectedIngredient);
                            ((IngredientSelectionViewAdapter) collectionViewAdapter).selectItem(position);
                            setIngredientQuantityPrompt.dismiss();
                        }
                        else {
                            ArrayList<String> errorMessages = new ArrayList<>();
                            for (int errorStringID : validator.getErrorStringIds()) {
                                errorMessages.add( getString(errorStringID) );
                            }
                            displayErrorMessages(errorMessages);
                        }
                    }
                });
            }
        });
        setIngredientQuantityPrompt.show();
    }

    /**
     * Displays error messages in a dialog
     * @param errorMessages the strings of error messages to show
     */
    private void displayErrorMessages(ArrayList<String> errorMessages) {
        // FIXME: maybe this should be a custom view since it has been reused from ingredient form
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog errorMessageDisplay = builder
                .setTitle("Please fill out the form properly")
                .setMessage( String.join("\n", errorMessages) )
                .create();
        errorMessageDisplay.show();
    }

    /**
     * Sends the ingredient to the caller fragment
     */
    private void sendIngredientsToCallerFragment() {
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                chosenIngredients
        );
        Navigation.findNavController(getView()).popBackStack();
    }

    /**
     * Parses the user input in the edit text for amount
     * @param amountInput edit text for amount
     * @return double. If invalid input, returns NaN
     */
    private double getAmountFromInput(EditText amountInput) {
        double amount = Double.NaN;
        try {
            amount = Double.parseDouble( amountInput.getText().toString() );
        }
        catch (NumberFormatException e) {
            amount = Double.NaN;
        }
        return amount;
    }
}