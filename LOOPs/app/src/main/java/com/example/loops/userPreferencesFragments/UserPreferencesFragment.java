package com.example.loops.userPreferencesFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.loops.ingredientFragments.IngredientCollectionEditorFragmentDirections;
import com.example.loops.userPreferencesFragments.OptionsEditorFragment.OptionsType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.loops.R;

/**
 * A fragment as a central place to navigate to fragments related to manipulating user preferences
 */
public class UserPreferencesFragment extends Fragment {
    // FIXME: come up with a better name for the package and this class
    // I suck at naming.
    private Button gotoIngredientCategoryButton;
    private Button gotoRecipeCategoryButton;
    private Button gotoStoredLocationButton;

    public UserPreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Binds components of the view and creates the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_preferences, container, false);
        gotoIngredientCategoryButton = view.findViewById(R.id.goto_icategory_option_editor);
        gotoRecipeCategoryButton = view.findViewById(R.id.goto_rcategory_option_editor);
        gotoStoredLocationButton = view.findViewById(R.id.goto_storage_option_editor);
        return view;
    }

    /**
     * Sets up listeners
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    /**
     * Sets up the listeners in this fragment
     */
    private void setListeners() {
        gotoIngredientCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsEditor(OptionsType.INGREDIENT_CATEGORY);
            }
        });
        gotoRecipeCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsEditor(OptionsType.RECIPE_CATEGORY);
            }
        });
        gotoStoredLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsEditor(OptionsType.STORAGE_LOCATION);
            }
        });
    }

    /**
     * Navigates to the options editor fragment
     * @param optionType type of options to manipulate
     */
    private void openOptionsEditor(OptionsType optionType) {
        NavDirections openOptionsEditorAction =
                UserPreferencesFragmentDirections.actionUserPreferencesFragmentToOptionsEditorFragment(
                        optionType
                );
        Navigation.findNavController(getView()).navigate(openOptionsEditorAction);
    }
}