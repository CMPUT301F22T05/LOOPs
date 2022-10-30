package com.example.loops;

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
import java.util.Calendar;
import java.util.Locale;

/**
 *  A recipe form. Holds the UI of the  form and on submit, saves the result as FragmentResult
 *  with the key RECIPE_RESULT
 */
public class RecipeFormFragment extends Fragment {

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
    protected IngredientCollection ingredientCollection;


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
        int maxHourValue = 48;
        int maxMinuteValue = 48;

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
                // TODO: Implement saving the recipe
            }
        });
        // setOnClickCancelButton();    FIXME: there is no cancel button in the UI mockup nor attributes
    }

}
