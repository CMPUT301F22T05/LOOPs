package com.example.loops.ingredientFragments;

import static java.lang.String.valueOf;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.R;
import com.example.loops.ingredientFragments.forms.EditIngredientFormFragment;
import com.example.loops.models.Ingredient;

/**
 * This fragment displays details of an ingredient and provides
 * edit & delete options for a specific ingredient.
 */
public class IngredientFragment extends Fragment {

    // selected ingredient from ingredient collection
    private Ingredient ingredient;
    private int ingInd;

    // all text views
    private TextView descriptionText;
    private TextView bestBeforeDateText;
    private TextView locationText;
    private TextView amountText;
    private TextView unitText;
    private TextView categoryText;

    // all buttons
    private Button backButton;
    private Button editButton;
    private Button deleteButton;

    /**
     * Initialize all text views based on the selected ingredient.
     */
    public void initializeViewWithIngredient() {
        descriptionText.setText(ingredient.getDescription());
        bestBeforeDateText.setText(ingredient.getBestBeforeDateString());
        locationText.setText(ingredient.getStoreLocation());
        amountText.setText(Double.toString(ingredient.getAmount()));
        unitText.setText(ingredient.getUnit());
        categoryText.setText(ingredient.getCategory());

        // pending ingredient have no best before date, location, amount, & unit
        if (ingredient.getPending()) {
            bestBeforeDateText.setText("N/A");
            locationText.setText("N/A");
            amountText.setText("N/A");
            unitText.setText("N/A");
        }
    }

    /**
     * Set up the back button's action.
     * Send the edited ingredient back to the ingredient collection for update.
     */
    public void setBackButtonOnClick() {
        backButton.setOnClickListener(view -> {
            IngredientFragmentDirections.ActionIngredientFragmentToIngredientCollectionFragment
                    backCollectionAction = IngredientFragmentDirections
                    .actionIngredientFragmentToIngredientCollectionFragment();
            backCollectionAction.setEditedIngredient(ingredient);
            backCollectionAction.setEditedIngredientIndex(ingInd);
            Navigation.findNavController(view).navigate((NavDirections) backCollectionAction);
        });
    }

    /**
     * Set up the edit button's action.
     * Send the current ingredient to edit ingredient form fragment for user to edit.
     */
    public void setEditButtonOnClick() {
        editButton.setOnClickListener(view -> {
            NavDirections editIngredientAction = (NavDirections) IngredientFragmentDirections
                    .actionIngredientFragmentToEditIngredientFormFragment(ingredient);
            Navigation.findNavController(view).navigate(editIngredientAction);
        });
    }

    /**
     * Set up the delete button's action
     * Invoke a popup window for user to confirm the deletion to avoid accidentally delete.
     */
    public void setDeleteButtonOnClick(View parentView) {
        LayoutInflater inflater = getLayoutInflater();
        View deletePopupView = inflater.inflate(R.layout.popup_ingredient_delete, null);

        PopupWindow deletePopupWindow = new PopupWindow(
                deletePopupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // set up two buttons, one for cancel, one for confirm
        // set up the popup message
        Button popupDeleteNoButton = deletePopupView.findViewById(R.id.delete_popup_no_button);
        Button popupDeleteYesButton = deletePopupView.findViewById(R.id.delete_popup_yes_button);
        TextView popupDeleteText = deletePopupView.findViewById(R.id.delete_popup_message);

        popupDeleteNoButton.setOnClickListener(view -> {
            deletePopupWindow.dismiss();
        });
        popupDeleteYesButton.setOnClickListener(view -> {
            deletePopupWindow.dismiss();
            IngredientFragmentDirections.ActionIngredientFragmentToIngredientCollectionFragment
                    deleteCollectionAction = IngredientFragmentDirections
                    .actionIngredientFragmentToIngredientCollectionFragment();
            deleteCollectionAction.setEditedIngredient(ingredient);
            deleteCollectionAction.setEditedIngredientIndex(ingInd);
            deleteCollectionAction.setDeleteFlag(true);
            Navigation.findNavController(parentView).navigate((NavDirections) deleteCollectionAction);
        });
        popupDeleteText.setText(String.format("Delete %s?", ingredient.getDescription()));

        // delete button is only for opening popup window
        deleteButton.setOnClickListener(view -> {
            deletePopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * binds the view's components to the class's attributes, set up event listeners, and initializes
     * the view with ingredient's attribute
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // init all elements
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        descriptionText = view.findViewById(R.id.ingredient_description);
        bestBeforeDateText = view.findViewById(R.id.ingredient_best_before_date);
        locationText = view.findViewById(R.id.ingredient_location);
        amountText = view.findViewById(R.id.ingredient_amount);
        unitText = view.findViewById(R.id.ingredient_unit);
        categoryText = view.findViewById(R.id.ingredient_category);

        backButton = view.findViewById(R.id.ingredient_back_button);
        editButton = view.findViewById(R.id.ingredient_edit_button);
        deleteButton = view.findViewById(R.id.ingredient_delete_button);

        // get the ingredient and load info
        ingredient = IngredientFragmentArgs.fromBundle(getArguments()).getSelectedIngredient();
        ingInd = IngredientFragmentArgs.fromBundle(getArguments()).getSelectedIngredientIndex();

        // initialize all button activities
        setBackButtonOnClick();
        setEditButtonOnClick();
        setDeleteButtonOnClick(view);

        initializeViewWithIngredient();
        return view;
    }

    /**
     * Sets the behavior when objects are returned from other fragments to this fragment
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnEditIngredientBehaviour();
    }

    /**
     * Changes the ingredient displayed accordingly to what it was edited to
     * and display button to confirm changes
     */
    private void setOnEditIngredientBehaviour() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( EditIngredientFormFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object submittedIngredient) {
                ingredient = (Ingredient) submittedIngredient;
                initializeViewWithIngredient();

                savedStateHandle.remove( EditIngredientFormFragment.RESULT_KEY );
            }
        });
    }
}
