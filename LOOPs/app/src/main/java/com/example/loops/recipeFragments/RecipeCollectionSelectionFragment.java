package com.example.loops.recipeFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.Navigation;

import com.example.loops.R;
import com.example.loops.factory.RecipeCollectionFactory.CollectionType;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.models.Recipe;
import com.example.loops.validators.RecipeValidator;

import java.util.ArrayList;

public class RecipeCollectionSelectionFragment extends RecipeCollectionFragment {
    public static final String RESULT_KEY = "RECIPE_COLLECTION_SELECTION_FRAGMENT_RESULT_KEY";
    private Button saveButton;
    private BaseRecipeCollection chosenRecipes;

    public RecipeCollectionSelectionFragment() {
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
        saveButton = fragmentView.findViewById(R.id.select_recipe_button);
        chosenRecipes = new BaseRecipeCollection();
        setSaveButtonListener();
        return fragmentView;
    }

    /**
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    @Override
    protected int getUIViewId() {
        return R.layout.fragment_recipe_collection_selection;
    }

    /**
     * Returns the recipe collection type that is being displayed
     * @return
     */
    @Override
    public CollectionType getCollectionType() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        RecipeCollectionSelectionFragmentArgs argsBundle
                = RecipeCollectionSelectionFragmentArgs.fromBundle(getArguments());
        CollectionType collectionType = argsBundle.getCollectionType();
        return collectionType;
    }

    /**
     * Parses the arguments specified by navigation graph actions.
     * Sets the recipe collection from the arguments and passes recipes from forms back
     * to caller
     */
    protected void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        RecipeCollectionSelectionFragmentArgs argsBundle
                = RecipeCollectionSelectionFragmentArgs.fromBundle(getArguments());
        // Get recipes to filter
        BaseRecipeCollection filterRecipes = argsBundle.getRecipesToFilter();
        if (filterRecipes != null) {
            for (Recipe recipe : filterRecipes.getAllRecipes()) {
                int index = recipeCollection.getAllRecipes().indexOf(recipe);
                if (index != -1) {
                    recipeCollection.deleteRecipe(index);
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
                sendRecipesToCallerFragment();
            }
        });
    }

    /**
     * Selects the clicked recipe and sends it to caller fragment
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onClickRecipe(AdapterView<?> parent, View view, int position, long id) {
        Recipe selectedRecipe = collectionViewAdapter.getItem(position);

        // If already selected, unselect it
        if (chosenRecipes.getAllRecipes().contains(selectedRecipe)) {
            // TODO: implement adapter to select recipes
            // ((RecipeCollectionViewAdapter) collectionViewAdapter).selectItem(position);
            chosenRecipes.getAllRecipes().remove(selectedRecipe);
        }
        // If not, add it to selection
        else {
            openRecipeQuantityPrompt(selectedRecipe, position);
        }
    }

    /**
     * Opens the dialog to prompt for user to input quantity of selected recipe
     * It also selects the recipe in the adapter
     * @param selectedRecipe
     * @param position position of the selected ingredient in the adapter
     */
    private void openRecipeQuantityPrompt(Recipe selectedRecipe, int position) {
        /**
         * https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
         * Author : Tom Bollwitt
         * Date Accessed : 2022-11-20
         */
        // Create view for the prompt
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // TODO: Create dialog UI for num servings
        View recipeQuantityPromptView = inflater.inflate(R.layout.dialog_ingredient_quantity_prompt, null);
        EditText amountInput = recipeQuantityPromptView.findViewById(R.id.dialog_amount_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog setRecipeQuantityPrompt = builder
                .setTitle( "Select Quantity for " + selectedRecipe.getTitle() )
                .setView(recipeQuantityPromptView)
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
        setRecipeQuantityPrompt.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = setRecipeQuantityPrompt.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get user input
                        double amount = getAmountFromInput(amountInput);
                        // Validate user input
                        RecipeValidator validator = new RecipeValidator();
                        boolean valid = validator.checkNumServ((int) amount, RecipeValidator.RECIPE_TYPE.STORED);

                        if (valid) {
                            selectedRecipe.setNumServing((int) amount);
                            chosenRecipes.addRecipe(selectedRecipe);
                            // TODO: implement adapter to select recipes
                            // ((RecipeCollectionViewAdapter) collectionViewAdapter).selectItem(position);
                            setRecipeQuantityPrompt.dismiss();
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
        setRecipeQuantityPrompt.show();
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
     * Sends the recipes to the caller fragment
     */
    private void sendRecipesToCallerFragment() {
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                chosenRecipes
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
