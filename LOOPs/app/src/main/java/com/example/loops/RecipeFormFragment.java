package com.example.loops;

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

/**
 *  A recipe form. Holds the UI of the  form and on submit, saves the result as FragmentResult
 *  with the key RECIPE_RESULT
 */
public class RecipeFormFragment extends Fragment {

    protected EditText titleInput;
    protected NumberPicker prepTimeInput;
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
        prepTimeInput = formView.findViewById(R.id.recipeFormPrepTimeInput);
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
    
    private void setConstraintsOnInputs() {
        bindDatePickerDialogToDateInput(bestBeforeDateInput);
    }
}
