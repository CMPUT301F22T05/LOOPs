package com.example.loops;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *  A recipe form. Holds the UI of the  form and on submit, saves the result as FragmentResult
 *  with the key RECIPE_RESULT
 */
public abstract class RecipeFormFragment extends Fragment {

    protected EditText titleInput;
    protected NumberPicker prepTimeHourInput;
    protected NumberPicker prepTimeMinuteInput;
    protected Spinner categoryInput;
    protected EditText numServingInput;
    protected EditText commentsInput;
    protected Button submitButton;
    protected Button addIngredientButton;
    protected ListView ingredientListView;
    protected RecyclerView ingredientRecyclerView;
    protected RecipeIngredientsAdapter ingredientsAdapter;
    protected IngredientCollection ingredientCollection;

    public RecipeFormFragment() {}


    /**
     * Implement to handle how submitted Recipe is sent to other activities
     * @param submittedRecipe ingredient submitted by the form
     */
    abstract void sendResult(Recipe submittedRecipe);
    /**
     * Creates view of the ingredient form and initialize its widgets
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View formView = inflater.inflate(R.layout.fragment_recipe_form, container, false);
        initializeWidgets(formView);
        return formView;
    }

    /**
     * initializes widgets of the form.
     * This involves finding the layout widgets and populating the spinner values
     * @param formView
     */
    private void initializeWidgets(View formView) {
        getLayoutWidgetsFrom(formView);
//        populateSpinnerOptions()      FIXME: For now, spinner options are hard-coded but this will change
    }

    /**
     * Gets all the relevant widgets and sets it to the corresponding class attribute.
     * @param formView
     */
    private void getLayoutWidgetsFrom(View formView) {
        titleInput = formView.findViewById(R.id.recipeFormTitleInput);
        prepTimeHourInput = formView.findViewById(R.id.recipeFormPrepTimeHourInput);
        prepTimeMinuteInput = formView.findViewById(R.id.recipeFormPrepTimeMinuteInput);
        numServingInput = formView.findViewById(R.id.recipeFormNumServingInput);
        commentsInput = formView.findViewById(R.id.recipeFormCommentsInput);
        ingredientRecyclerView = formView.findViewById(R.id.recipeFormIngredientRecyclerView);
        categoryInput = formView.findViewById(R.id.recipeFormCategoryInput);
        submitButton = formView.findViewById(R.id.recipeFormSubmitButton);
    }

    /**
     * Set up event listeners
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        setConstraintsOnInputs(); // Feel like this needs better name
        setButtonOnClickListeners();
    }

    /**
     * Set constraints on input widgets
     */
    private void setConstraintsOnInputs() {
        bindNumberPickerOnInput(prepTimeHourInput, prepTimeMinuteInput);
    }

    /**
     * Sets the values displayed by the number picker's
     * @param prepTimeHourInput
     * @param prepTimeMinuteInput
     */
    private void bindNumberPickerOnInput(NumberPicker prepTimeHourInput,NumberPicker prepTimeMinuteInput ) {
        // TODO: Finish implementing the values displayed for number picker
        int maxHourValue = 99;
        int maxMinuteValue = 59;

        prepTimeHourInput.setMinValue(0);
        prepTimeHourInput.setMaxValue(maxHourValue);

        prepTimeMinuteInput.setMinValue(0);
        prepTimeMinuteInput.setMaxValue(maxMinuteValue);
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

        // setOnClickCancelButton();    FIXME: there is no cancel button in the UI mockup nor attributes
    }


    /**
     * Validates the values in the form.
     * If there are any validation errors, displays to the user error messages.
     * If there are no validation errors, submits the result to the fragment manager and closes fragment
     */
    public void submitForm() {
        Recipe submittedRecipe = getInputtedRecipe();
        if ( isValidRecipeAndNotifyErrors(submittedRecipe) ) {
            sendResult(submittedRecipe);
        }
    }

    /**
     * Returns an Recipe object where its attributes are those from the form
     * @return Recipe object formed by the value of the fields of the form
     */
    public Recipe getInputtedRecipe() {
        String title = titleInput.getText().toString();
        int timeHour = prepTimeHourInput.getValue();
        int timeMinute = prepTimeMinuteInput.getValue();
        String category = categoryInput.getSelectedItem().toString();
        String StringNumServ = numServingInput.getText().toString();
        int numServ = Integer.parseInt(StringNumServ);
        String comment = commentsInput.getText().toString();
        Duration duration = Duration.ofHours(timeHour).plus(Duration.ofMinutes(timeMinute));
        Recipe inputtedRecipe= new Recipe(
                title,
                duration,
                category,
                numServ,
                comment
        );
        return inputtedRecipe;
    }


    /**
     * Checks if Recipe is valid and also if there are any errors, prompts the message to user
     * by displayErrorMessage
     * @param RecipeToValidate Recipe to validate
     * @return True if ingredient is valid. Otherwise false and notify user of errors
     */
    private boolean isValidRecipeAndNotifyErrors(Recipe RecipeToValidate) {
        RecipeValidator validator = new RecipeValidator();
        validator.checkRecipe(RecipeToValidate, RecipeValidator.RECIPE_TYPE.STORED);

        ArrayList<String> errorMessages = new ArrayList<>();
        for (int errorStringID : validator.getErrorStringIds()) {
            errorMessages.add( getString(errorStringID) );
        }
        if (errorMessages.size() > 0) {
            displayErrorMessages(errorMessages);
            return false;
        }
        return true;
    }

    /**
     * Displays error messages to the user by opening up a popup
     * @param errorMessages string of error messages to display to user
     */
    private void displayErrorMessages(ArrayList<String> errorMessages) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog errorMessageDisplay = builder
                .setTitle("Please fill out the form properly")
                .setMessage( String.join("\n", errorMessages) )
                .create();
        errorMessageDisplay.show();
    }
}
