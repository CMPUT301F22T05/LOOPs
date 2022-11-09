package com.example.loops.recipeFragments.forms;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.R;
import com.example.loops.adapters.RecipeIngredientsAdapter;
import com.example.loops.validators.RecipeValidator;
import com.example.loops.RecyclerViewOnClickInterface;
import com.example.loops.models.Recipe;

import java.time.Duration;
import java.util.ArrayList;

/**
 *  A recipe form. Holds the UI of the  form and on submit, saves the result as FragmentResult
 *  with the key RECIPE_RESULT
 */
public abstract class RecipeFormFragment extends Fragment implements RecyclerViewOnClickInterface {

    protected EditText titleInput;
    protected NumberPicker prepTimeHourInput;
    protected NumberPicker prepTimeMinuteInput;
    protected Spinner categoryInput;
    protected EditText numServingInput;
    protected EditText commentsInput;
    protected Button submitButton;
    protected Button addIngredientButton;
    protected RecyclerView ingredientRecyclerView;
    protected IngredientCollection ingredientCollection;

    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView.Adapter recyclerViewAdapter;

    protected ImageView imageView;
    protected Button addPhotoButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
        addIngredientButton = formView.findViewById(R.id.recipeFormAddIngredientButton);
        addPhotoButton = formView.findViewById(R.id.add_photo_button);
        imageView = formView.findViewById(R.id.imageView);
    }

    /**
     * Set up event listeners
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        //ingredientCollection = new IngredientCollection();
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

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectionForWhereToSelectIngredientsFrom();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(openCamera, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
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
        Recipe submittedRecipe = getRecipeFromInput();
        if ( isValidRecipeAndNotifyErrors(submittedRecipe) ) {
            sendResult(submittedRecipe);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(photo);
    }

    /**
     * Returns an Recipe object where its attributes are those from the form
     * @return Recipe object formed by the value of the fields of the form
     */
    public Recipe getRecipeFromInput() {
        Recipe recipe = new Recipe(
                titleInput.getText().toString(),
                prepTimeHourInput.getValue(),
                prepTimeMinuteInput.getValue(),
                Integer.parseInt(numServingInput.getText().toString()),
                categoryInput.getSelectedItem().toString(),
                ((BitmapDrawable) imageView.getDrawable()).getBitmap(),
                ingredientCollection,
                commentsInput.getText().toString()
        );

        return recipe;
    }

    /*
    @Deprecated
    public Recipe getInputtedRecipe() {
        String title = titleInput.getText().toString();
        int timeHour = prepTimeHourInput.getValue();
        int timeMinute = prepTimeMinuteInput.getValue();
        String category = categoryInput.getSelectedItem().toString();
        String StringNumServ = numServingInput.getText().toString();
        int numServ = Integer.parseInt(StringNumServ);
        String comment = commentsInput.getText().toString();
        Duration duration = Duration.ofHours(timeHour).plus(Duration.ofMinutes(timeMinute));
        Bitmap photoG = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Recipe inputtedRecipe= new Recipe(
                title,
                duration,
                category,
                numServ,
                comment,
                photoG
        );
        return inputtedRecipe;
    }
*/

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
