package com.example.loops;

import static java.lang.String.valueOf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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
        amountText.setText(valueOf(ingredient.getAmount()));
        unitText.setText(ingredient.getUnit());
        categoryText.setText(ingredient.getCategory());
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
            Navigation.findNavController(view).navigate(backCollectionAction);
        });
    }

    /**
     * Set up the edit button's action.
     * Send the current ingredient to edit ingredient form fragment for user to edit.
     */
    public void setEditButtonOnClick() {
        editButton.setOnClickListener(view -> {
            NavDirections editIngredientAction = IngredientFragmentDirections
                    .actionIngredientFragmentToEditIngredientFormFragment(ingredient);
            Navigation.findNavController(view).navigate(editIngredientAction);
        });
    }

    public void setDeleteButtonOnClick() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        initializeViewWithIngredient();

        // initialize all button activities
        setBackButtonOnClick();
        setEditButtonOnClick();
        setDeleteButtonOnClick();

        return view;
    }
}
