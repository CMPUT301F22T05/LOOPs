package com.example.loops.ingredientFragments.forms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.loops.MainActivity;
import com.example.loops.database.Database;
import com.example.loops.database.UserPreferenceAttribute;
import com.example.loops.validators.IngredientValidator;
import com.example.loops.R;
import com.example.loops.models.Ingredient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * An ingredient form. Holds the UI of the form and delegates on submit behavior to its subclasses
 */
public abstract class IngredientFormFragment extends Fragment {
    //private static final String INPUT_DATE_FORMAT = "MM/dd/yyyy";
    protected EditText descriptionInput;
    protected EditText bestBeforeDateInput;
    protected Spinner locationInput;
    protected EditText amountInput;
    protected Spinner unitInput;
    protected Spinner categoryInput;
    protected Button submitButton;

//    public IngredientFormFragment() {}

    /**
     * Implement to handle how submitted ingredient is sent to other activities
     * @param submittedIngredient ingredient submitted by the form
     */
    protected abstract void sendResult(Ingredient submittedIngredient);

    /**
     * Subclasses can override this to set the default category option displayed in the form
     * @return the default category option
     */
    protected String getDefaultCategory() {
        return "";  // empty value
    }

    /**
     * Subclasses can override this to set the default location option displayed in the form
     * @return the default location option
     */
    protected String getDefaultLocation() {
        return "";  // empty value
    }

    /**
     * Creates view of the ingredient form and initialize its widgets
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view of the ingredient form
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View formView = inflater.inflate(R.layout.fragment_ingredient_form, container, false);
        initializeWidgets(formView);
        return formView;
    }

    /**
     * Set up event listeners
     * @param formView view of the ingredient form
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
        Ingredient submittedIngredient = getInputtedIngredient();
        if ( isValidIngredientAndNotifyErrors(submittedIngredient) ) {
            sendResult(submittedIngredient);
        }
    }

    /**
     * Returns an ingredient object where its attributes are those from the form
     * @return ingredient object formed by the value of the fields of the form
     */
    protected Ingredient getInputtedIngredient() {
        String description = descriptionInput.getText().toString();
        LocalDate bestBeforeDate = parseBestBeforeDateFromInput();
        String location = locationInput.getSelectedItem().toString();
        double amount = parseAmountFromInput();
        String unit = unitInput.getSelectedItem().toString();
        String category = categoryInput.getSelectedItem().toString();

        Ingredient inputtedIngredient = new Ingredient(
                description,
                bestBeforeDate,
                location,
                amount,
                unit,
                category
        );
        inputtedIngredient.setPending(false);
        return inputtedIngredient;
    }

    /**
     * Checks if ingredient is valid and also if there are any errors, prompts the message to user
     * by displayErrorMessage
     * @param ingredientToValidate ingredient to validate
     * @return True if ingredient is valid. Otherwise false and notify user of errors
     */
    private boolean isValidIngredientAndNotifyErrors(Ingredient ingredientToValidate) {
        IngredientValidator validator = new IngredientValidator();
        validator.checkIngredient(ingredientToValidate, IngredientValidator.INGREDIENT_TYPE.STORED);

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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogStyle);

        builder
                .setTitle("Error: Missing fields")
                .setMessage(String.join("\n\n", errorMessages))
                .setPositiveButton("Okay", null)
                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                .show();
    }

    /**
     * Set constraints on input widgets
     */
    private void setConstraintsOnInputs() {
        bindDatePickerDialogToDateInput(bestBeforeDateInput);
        populateSpinnerOptions();
    }

    /**
     * Binds the date picker dialog to the date input by opening the date picker on date input click
     * After choosing the date, the date is set in the date input.
     * @param dateInput edittext to act as date input
     */
    private void bindDatePickerDialogToDateInput(EditText dateInput) {
        /*
        Android_coder, Datepicker: How to popup datepicker when click on edittext
        https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext,
        2022-09-24, Creative Commons Attribution-ShareAlike license
         */
        DatePickerDialog.OnDateSetListener onDateSetCallback = (view, year, month, day) -> {
            Calendar pickedDate = Calendar.getInstance();
            pickedDate.set(Calendar.YEAR, year);
            pickedDate.set(Calendar.MONTH,month);
            pickedDate.set(Calendar.DAY_OF_MONTH,day);

            SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.US);
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
     * @param formView view of the ingredient form
     */
    private void initializeWidgets(View formView) {
        getLayoutWidgetsFrom(formView);
    }

    /**
     * Gets all the relevant widgets and sets it to the corresponding class attribute.
     * @param formView view of the ingredient form
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

    /**
     * Populates all the spinners in the fragment with relevant options
     */
    private void populateSpinnerOptions() {
        // lazy way to not break testing
        if ( getActivity() instanceof MainActivity ) {
            Database db = Database.getInstance();

            // Create array list and adapter for ingredient category
            ArrayList<String> categories = new ArrayList<>();
            categories.add(getDefaultCategory());
            ArrayAdapter categoriesAdapter =
                    new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categories);
            categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoryInput.setAdapter(categoriesAdapter);
            // Create array list and adapter for storage location
            ArrayList<String> locations = new ArrayList<>();
            locations.add(getDefaultLocation());
            ArrayAdapter locationsAdapter =
                    new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, locations);
            locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationInput.setAdapter(locationsAdapter);
            // get data
            db.getUserPreferencesAttribute(
                UserPreferenceAttribute.IngredientCategory,
                (result) -> {
                    for (String category : result) {
                        if ( ! categories.contains(category) )
                            categories.add(category);
                    }
                    categoriesAdapter.notifyDataSetChanged();
            });
            db.getUserPreferencesAttribute(
                UserPreferenceAttribute.StorageLocation,
                (result) -> {
                    for (String location : result) {
                        if ( ! locations.contains(location) )
                            locations.add(location);
                    }
                    locationsAdapter.notifyDataSetChanged();
            });
        }
    }

    /**
     * Parses the best before date from the input
     * @return the parsed best before date
     */
    private LocalDate parseBestBeforeDateFromInput() {
        LocalDate bestBeforeDate;
        try {
            String bestBeforeDateText = bestBeforeDateInput.getText().toString();
            DateTimeFormatter dateFormatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd", Locale.CANADA);
            bestBeforeDate = LocalDate.parse(bestBeforeDateText, dateFormatter);
        }
        catch (DateTimeException e) {
            bestBeforeDate = null;
        }
        return bestBeforeDate;
    }

    /**
     * Parses the amount from the input
     * @return the parsed double amount
     */
    protected double parseAmountFromInput() {
        double amount;
        try {
            amount = Double.parseDouble( amountInput.getText().toString() );
        }
        catch (NumberFormatException e) {
            amount = Double.NaN;
        }
        return amount;
    }

    /**
     * Returns the index of the spinner item given its text value
     * @param value the string value of the spinner option
     * @param spinner the spinner to check
     * @return
     */
    protected int getSpinnerIndexByValue(String value, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }
}