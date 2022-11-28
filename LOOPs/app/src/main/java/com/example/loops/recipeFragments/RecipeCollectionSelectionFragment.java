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
import com.example.loops.adapters.RecipeSelectionViewAdapter;
import com.example.loops.factory.RecipeCollectionFactory.CollectionType;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.example.loops.validators.RecipeValidator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Display a collection of recipes to be selected and this selection is sent back to the caller
 * fragment
 */
public class RecipeCollectionSelectionFragment extends RecipeCollectionFragment {
    public static final String RESULT_KEY = "RECIPE_COLLECTION_SELECTION_FRAGMENT_RESULT_KEY";
    private Button saveButton;
    private BaseRecipeCollection chosenRecipes;

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
            // Filter ingredients in filter ingredients
            recipeCollection.getAllRecipes().removeIf(
                (recipe) -> { return filterRecipes.containsSameRecipeIgnoringQuantity(recipe);
            });
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
            ((RecipeSelectionViewAdapter) collectionViewAdapter).selectItem(position, -1);
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
        View recipeQuantityPromptView = inflater.inflate(R.layout.dialog_recipe_quantity_prompt, null);
        EditText numServInput = recipeQuantityPromptView.findViewById(R.id.dialog_num_serving_input);

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
                        int numServings = getNumServingsFromInput(numServInput);
                        // Validate user input
                        RecipeValidator validator = new RecipeValidator();
                        boolean valid = validator.checkNumServ(numServings, RecipeValidator.RECIPE_TYPE.STORED);

                        if (valid) {
                            ((RecipeSelectionViewAdapter) collectionViewAdapter).selectItem(position, numServings);
                            chosenRecipes.addRecipe(selectedRecipe);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog errorMessageDisplay = builder
                .setTitle("Please fill out the form properly")
                .setMessage( String.join("\n", errorMessages) )
                .create();
        errorMessageDisplay.show();
    }

    /**
     * Given a new number of servings, scale the recipe to meet the new number of servings
     * @param recipeToScale
     * @param newNumServings
     */
    private void scaleRecipeWithNewServing(Recipe recipeToScale, int newNumServings) {
        /* FIXME: one bug I can think of right now is if ingredient is scaled, then
                if the ingredient is referenced elsewhere, then that would also be scaled */
        // FIXME: Also another consideration is adding a scale method to Recipe or changing how setNumServing works
        double scalingFactor = (double) newNumServings / recipeToScale.getNumServing();
        DecimalFormat rounder = new DecimalFormat("#.####");
        for (Ingredient ing : recipeToScale.getIngredients().getIngredients()) {
            double scaledAmount = ing.getAmount() * scalingFactor;
            double roundedAmount = Double.parseDouble( rounder.format( scaledAmount ) );
            ing.setAmount( roundedAmount );
        }
        recipeToScale.setNumServing(newNumServings);
    }

    /**
     * Sends the recipes to the caller fragment
     */
    private void sendRecipesToCallerFragment() {
        // Scale all chosen recipes
        for (Recipe recipe : chosenRecipes.getAllRecipes()) {
            int newNumServings = ((RecipeSelectionViewAdapter) collectionViewAdapter).getNumberOfServings(recipe);
            scaleRecipeWithNewServing(recipe, newNumServings);
        }
        // Send result
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                chosenRecipes
        );
        Navigation.findNavController(getView()).popBackStack();
    }

    /**
     * Parses the user input in the edit text for number of servings
     * @param numServInput edit text for number of servings
     * @return int. If invalid input, returns -1
     */
    private int getNumServingsFromInput(EditText numServInput) {
        int numServ = -1;
        try {
            numServ = Integer.parseInt( numServInput.getText().toString() );
        }
        catch (NumberFormatException e) {
            numServ = -1;
        }
        return numServ;
    }
}
