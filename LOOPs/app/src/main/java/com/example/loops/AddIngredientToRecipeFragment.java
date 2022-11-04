package com.example.loops;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.time.Duration;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIngredientToRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class AddIngredientToRecipeFragment extends RecipeFormFragment {

    protected EditText descriptionInput;
    protected EditText amountInput;
    protected EditText unitTypeInput;
    protected EditText categoryInput;
    protected Button submitButton;

    public AddIngredientToRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Implement to handle how submitted Ingredient is sent to other activities
     * @param submittedIngredient ingredient submitted by the form
     */
    abstract void sendResult(IngredientFromRecipe submittedIngredient);

    /**
     * default onCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Creates view of the ingredient form and initialize its widgets
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View formView = inflater.inflate(R.layout.fragment_add_edit_ingredient_form, container, false);
        initializeWidgets(formView);
        return formView;
    }

    /**
     * initializes widgets of the form.
     * This involves finding the layout widgets and populating Edittext
     * @param formView
     */
    private void initializeWidgets(View formView) {
        getLayoutWidgetsFrom(formView);
    }



    /**
     * Gets all the relevant widgets and sets it to the corresponding class attribute.
     * @param formView
     */
    private void getLayoutWidgetsFrom(View formView) {
        descriptionInput = formView.findViewById(R.id.input_description);
        amountInput = formView.findViewById(R.id.input_amount_ingredients);
        unitTypeInput = formView.findViewById(R.id.input_ingredients_unit);
        categoryInput = formView.findViewById(R.id.input_ingredient_category);
        submitButton = formView.findViewById(R.id.save_recipeForm_button);
    }



    /**
     * Set up event listeners
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        setButtonOnClickListeners();
    }



    /**
     * Sets all the button on click listeners in the form
     */
    private void setButtonOnClickListeners() {
        // setOnClickSubmitButton() but in here instead.

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }
    /**
     * Validates the values in the form.
     * If there are any validation errors, displays to the user error messages.
     * If there are no validation errors, submits the result to the fragment manager and closes fragment
     */
    public void submitForm() {
        IngredientFromRecipe submittedIngredient = getInputtedIngredientToRecipe();
//        if ( isValidRecipeAndNotifyErrors(submittedIngredient) ) {
            sendResult(submittedIngredient);
//        }
    }


    /**
     * Returns an Recipe object where its attributes are those from the form
     * @return Recipe object formed by the value of the fields of the form
     */
    public IngredientFromRecipe getInputtedIngredientToRecipe() {
        String description = descriptionInput.getText().toString();
        String amount = amountInput.getText().toString();
        int intAmount = Integer.parseInt(amount);
        String unitInput = unitTypeInput.getText().toString();
        String category = categoryInput.getText().toString();

        IngredientFromRecipe inputtedIngredient= new IngredientFromRecipe(
                description,
                intAmount,
                unitInput,
                category
        );
        return inputtedIngredient;
    }


//    /**
//     * Checks if Recipe is valid and also if there are any errors, prompts the message to user
//     * by displayErrorMessage
//     * @param ingredientToValidate ingredient to validate
//     * @return True if ingredient is valid. Otherwise false and notify user of errors
//     */
//    private boolean isValidRecipeAndNotifyErrors(IngredientFromRecipe ingredientToValidate) {
//        RecipeValidator validator = new RecipeValidator();
//        validator.checkRecipe(ingredientToValidate, RecipeValidator.RECIPE_TYPE.STORED);
//
//        ArrayList<String> errorMessages = new ArrayList<>();
//        for (int errorStringID : validator.getErrorStringIds()) {
//            errorMessages.add( getString(errorStringID) );
//        }
//        if (errorMessages.size() > 0) {
//            displayErrorMessages(errorMessages);
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Displays error messages to the user by opening up a popup
//     * @param errorMessages string of error messages to display to user
//     */
//    private void displayErrorMessages(ArrayList<String> errorMessages) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        AlertDialog errorMessageDisplay = builder
//                .setTitle("Please fill out the form properly")
//                .setMessage( String.join("\n", errorMessages) )
//                .create();
//        errorMessageDisplay.show();
//    }


}