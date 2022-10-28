package com.example.loops;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * An ingredient form. Holds the UI of the form and on submit, saves the result as FragmentResult
 * with the key INGREDIENT_RESULT
 */
public abstract class IngredientFormFragment extends Fragment {
    private static final String INPUT_DATE_FORMAT = "MM/dd/yyyy";
    protected EditText descriptionInput;
    protected EditText bestBeforeDateInput;
    protected Spinner locationInput;
    protected EditText amountInput;
    protected Spinner unitInput;
    protected Spinner categoryInput;
    protected Button submitButton;

    public IngredientFormFragment() {}

    /**
     * Creates view of the ingredient form and initialize its widgets
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View formView = inflater.inflate(R.layout.fragment_ingredient_form, container, false);
        initializeWidgets(formView);
        return formView;
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
        // FIXME: Abstract below code into private Ingredient getInputtedIngredient();
        String description = descriptionInput.getText().toString();
        LocalDate bestBeforeDate;
        // FIXME: ugly code here, I will fix when I implement getInputtedIngredient()
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT);
            bestBeforeDate = LocalDate.parse( bestBeforeDateInput.getText().toString() );
        }
        catch (DateTimeParseException e) {
            bestBeforeDate = null;
        }
        String location = locationInput.getSelectedItem().toString();
        int amount;
        try {
            amount = Integer.parseInt( amountInput.getText().toString() );
        }
        catch (NumberFormatException e) {
            // FIXME: need a null value for amount
            amount = -1;
        }
        // FIXME: need non-selected value for spinners
        String unit = unitInput.getSelectedItem().toString();
        String category = categoryInput.getSelectedItem().toString();

        // FIXME: Abstract below into validateFields()
        ArrayList<String> errorMessages = new ArrayList<>();

        // FIXME: replace by checkIngredient() instead later.
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription(description);
        validator.checkBestBeforeDate(bestBeforeDate);
        validator.checkLocation(location);
        validator.checkAmount(amount);
        validator.checkUnit(unit);
        validator.checkCategory(category);

        for (int errorStringID : validator.getErrorStringIds()) {
            errorMessages.add( getString(errorStringID) );
        }
        if (errorMessages.size() > 0) {
            displayErrorMessages(errorMessages);
            return;
        }

        // sendResult(description);
   }

    /**
     * Abstract method. Implement to handle how submitted ingredient is sent to other activities
     * @param submittedIngredient
     */
   abstract void sendResult(Ingredient submittedIngredient);

    /**
     * Displays error messages to the user by opening up a popup
     * @param errorMessages string of error messages to display to user
     */
    private void displayErrorMessages(ArrayList<String> errorMessages) {
        // FIXME: Maybe this should also be a custom widget? Make it reusable?
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog errorMessageDisplay = builder
                .setMessage( String.join("\n", errorMessages) )
                .create();
        errorMessageDisplay.show();
    }

    /**
     * Set constraints on input widgets
     */
    private void setConstraintsOnInputs() {
        bindDatePickerDialogToDateInput(bestBeforeDateInput);
    }

    /**
     * Binds the date picker dialog to the date input by opening the date picker on date input click
     * After choosing the date, the date is set in the date input.
     * @param dateInput
     */
    private void bindDatePickerDialogToDateInput(EditText dateInput) {
        /*
        Android_coder, Datepicker: How to popup datepicker when click on edittext
        https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext,
        2022-09-24, Creative Commons Attribution-ShareAlike license
         */
        // FIXME: This would be great as a custom widget...
        DatePickerDialog.OnDateSetListener onDateSetCallback = (view, year, month, day) -> {
            Calendar pickedDate = Calendar.getInstance();
            pickedDate.set(Calendar.YEAR, year);
            pickedDate.set(Calendar.MONTH,month);
            pickedDate.set(Calendar.DAY_OF_MONTH,day);

            SimpleDateFormat dateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.US);
            dateInput.setText(dateFormat.format(pickedDate.getTime()));
        };
        dateInput.setOnClickListener( (clickedView) -> {
            Calendar today = Calendar.getInstance();

            new DatePickerDialog(getActivity(), onDateSetCallback,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
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
        descriptionInput = formView.findViewById(R.id.ingredientFormDescriptionInput);
        bestBeforeDateInput = formView.findViewById(R.id.ingredientFormBestBeforeDateInput);
        locationInput = formView.findViewById(R.id.ingredientFormLocationInput);
        amountInput = formView.findViewById(R.id.ingredientFormAmountInput);
        unitInput = formView.findViewById(R.id.ingredientFormUnitInput);
        categoryInput = formView.findViewById(R.id.ingredientFormCategoryInput);
        submitButton = formView.findViewById(R.id.ingredientFormSubmitButton);
    }
}