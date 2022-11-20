package com.example.loops.recipeFragments.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loops.ingredientFragments.IngredientCollectionSelectionFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.R;
import com.example.loops.adapters.RecipeIngredientsAdapter;
import com.example.loops.models.Ingredient;
import com.example.loops.validators.RecipeValidator;
import com.example.loops.RecyclerViewOnClickInterface;
import com.example.loops.models.Recipe;

import java.util.ArrayList;

/**
 *  A recipe form. Holds the UI of the  form and on submit, saves the result as FragmentResult
 *  with the key RECIPE_RESULT
 */
public abstract class RecipeFormFragment extends Fragment implements RecyclerViewOnClickInterface {
    protected EditText titleInput;
    protected EditText prepTimeHourInput;
    protected EditText prepTimeMinuteInput;
    protected Spinner categoryInput;
    protected EditText numServingInput;
    protected EditText commentsInput;
    protected Button submitButton;
    protected Button addIngredientButton;
    protected RecyclerView ingredientRecyclerView;
    protected IngredientCollection ingredientCollection;

    protected RecyclerView.LayoutManager layoutManager;
    protected RecipeIngredientsAdapter recyclerViewAdapter;

    protected ImageView imageView;
    //protected Button addPhotoButton;
    private ActivityResultLauncher<Intent> cameraActivityLauncher;

    // Certain views' state are not properly saved hence the addition of this attribute
    private final Bundle savedFormState = new Bundle();

    public RecipeFormFragment() {}

    public void setUpRecyclerView(View view){
        ingredientRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        ingredientRecyclerView.setLayoutManager(layoutManager);
        if (ingredientCollection == null)
            ingredientCollection = new IngredientCollection();
        recyclerViewAdapter = new RecipeIngredientsAdapter (ingredientCollection,view.getContext(),this);
        ingredientRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void OnItemClick(int position) {
        //To do overridden by subclasses
    }

    /**
     * Implement to handle how submitted Recipe is sent to other activities
     * @param submittedRecipe ingredient submitted by the form
     */
    abstract protected void sendResult(Recipe submittedRecipe);

    /**
     * Implement to handle where to add ingredients from.
     */
    // FIXME: better as a non-abstract method but not enough time!!!! >: (
    abstract void openSelectionForWhereToSelectIngredientsFrom();


    /**
     * Implement to handle how subclasses parses their arguments
     */
    abstract void parseArguments();

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
        setUpRecyclerView(formView);
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
        addIngredientButton = formView.findViewById(R.id.recipeFormAddIngredientButton);
        //addPhotoButton = formView.findViewById(R.id.add_photo_button);
        imageView = formView.findViewById(R.id.imageView);
    }

    /**
     * Set up event listeners and parses argument
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        parseArguments();
        setConstraintsOnInputs();
        setButtonOnClickListeners();
        setOnAddIngredientBehaviour();
        setOnSelectIngredientBehaviour();
        if (savedInstanceState != null)
            restoreFormState(savedInstanceState);
        else
            restoreFormState(savedFormState);
        setCameraActivityLauncher();
    }

    /**
     * Set constraints on input widgets
     */
    private void setConstraintsOnInputs() {
        //bindNumberPickerOnInput(prepTimeHourInput, prepTimeMinuteInput);
    }

    /**
     * Sets the values displayed by the number picker's
     * @param prepTimeHourInput
     * @param prepTimeMinuteInput
     */
    private void bindNumberPickerOnInput(NumberPicker prepTimeHourInput, NumberPicker prepTimeMinuteInput ) {
        // TODO: Finish implementing the values displayed for number picker
        //int maxHourValue = 99;
        //int maxMinuteValue = 59;

        //prepTimeHourInput.setMinValue(0);
        //prepTimeHourInput.setMaxValue(maxHourValue);

        //prepTimeMinuteInput.setMinValue(0);
        //prepTimeMinuteInput.setMaxValue(maxMinuteValue);

        /* FIXME: there is a bug where when a user scrolls to select an option, the value displayed
            and the value of the number picker is off by one. */
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

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFragmentState();
                openSelectionForWhereToSelectIngredientsFrom();
            }
        });

        /*
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    saveFragmentState();
                    cameraActivityLauncher.launch(openCamera);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

         */

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    saveFragmentState();
                    cameraActivityLauncher.launch(openCamera);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

    }

    /**
     * Saves the state of the fragment.
     */
    private void saveFragmentState() {
//        Most of the view states are saved but there are some views that have problem saving their
//        state like NumberPicker and ImageView
        //savedFormState.putInt("PREPTIME_HOUR", prepTimeHourInput.getValue());
        //savedFormState.putInt("PREPTIME_MINUTE", prepTimeMinuteInput.getValue());
        if (imageView.getDrawable() != null)
            savedFormState.putParcelable("IMAGE", ((BitmapDrawable)imageView.getDrawable()).getBitmap());
        if (ingredientCollection != null)
            savedFormState.putSerializable("INGREDIENTS", ingredientCollection);
    }

    /**
     * Saves the state of the fragment when parent activity is destroyed
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveFragmentState();
        outState.putAll(savedFormState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Restores the state of the fragment saved by savedFormState
     * @param savedFormState
     */
    private void restoreFormState(Bundle savedFormState) {
        //if ( !savedFormState.isEmpty()) {
        //    prepTimeHourInput.setValue(savedFormState.getInt("PREPTIME_HOUR"));
        //    prepTimeMinuteInput.setValue(savedFormState.getInt("PREPTIME_MINUTE"));
        //}
        if (savedFormState.containsKey("IMAGE"))
            imageView.setImageBitmap(savedFormState.getParcelable("IMAGE"));
        if (savedFormState.containsKey("INGREDIENTS"))
            recyclerViewAdapter.setRecipeIngredients((IngredientCollection) savedFormState.getSerializable("INGREDIENTS"));
        savedFormState.clear();
    }

    /**
     * Handles the behavior when the add ingredient form submits an ingredient
     */
    private void setOnAddIngredientBehaviour() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( AddRecipeIngredientFormFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object ingredient) {
                ingredientCollection.addIngredient((Ingredient) ingredient);
                recyclerViewAdapter.notifyDataSetChanged();
                savedStateHandle.remove( AddRecipeIngredientFormFragment.RESULT_KEY );
            }
        });
    }

    /**
     * Handles the behavior when the select ingredient fragment submits ingredients
     */
    private void setOnSelectIngredientBehaviour() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( IngredientCollectionSelectionFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object selectedIngredients) {
                IngredientCollection s = (IngredientCollection) selectedIngredients;
                for (Ingredient ing : s.getIngredients()) {
                    ingredientCollection.addIngredient(ing);
                }
                recyclerViewAdapter.notifyDataSetChanged();
                savedStateHandle.remove( IngredientCollectionSelectionFragment.RESULT_KEY );
            }
        });
    }

    /**
     * Validates the values in the form.
     * If there are any validation errors, displays to the user error messages.
     * If there are no validation errors, submits the result to the fragment manager and closes fragment
     */
    public void submitForm() {
        Recipe submittedRecipe = getRecipeFromInput();
        if ( isValidRecipeAndNotifyErrors(submittedRecipe) ) {
            sendResult(submittedRecipe);
        }
    }

    /**
     * Sets up the launcher for the camera app and also the behavior on result of the camera operation
     */
    private void setCameraActivityLauncher() {
        cameraActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent resultBundle = result.getData();
                            Bitmap photo = (Bitmap) resultBundle.getExtras().get("data");
                            imageView.setImageBitmap(photo);
                        }
                    }
                }
        );
    }

    /**
     * Returns an Recipe object where its attributes are those from the form
     * @return Recipe object formed by the value of the fields of the form
     */
    public Recipe getRecipeFromInput() {
        int prepTimeHour = parseNonNegativeInteger(prepTimeHourInput.getText().toString());
        int prepTimeMin = parseNonNegativeInteger(prepTimeMinuteInput.getText().toString());
        // If one of the inputs was not given but the other was, set the empty input as zero.
        if (prepTimeHour == -1 && prepTimeMin != -1)
            prepTimeHour = 0;
        else if (prepTimeHour != -1 && prepTimeMin == -1)
            prepTimeMin = 0;

        Recipe recipe = new Recipe(
                titleInput.getText().toString(),
                prepTimeHour,
                prepTimeMin,
                parseNonNegativeInteger(numServingInput.getText().toString()),
                categoryInput.getSelectedItem().toString(),
                ((BitmapDrawable) imageView.getDrawable()).getBitmap(),
                ingredientCollection,
                commentsInput.getText().toString()
        );

        return recipe;
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

    /**
     * Parses non negative integer string to an integer
     * @param num string to parse
     * @return (int) the non negative integer. Returns -1 on invalid string
     */
    private int parseNonNegativeInteger(String num) {
        int result = -1;
        try {
            result = Integer.parseInt(num);
        }
        catch (NumberFormatException e) {
            result = -1;
        }
        return result;
    }
}
