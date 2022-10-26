package com.example.loops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditIngredientFragment extends IngredientFormFragment {
    public static final String INGREDIENT_RESULT_KEY = "EDIT_INGREDIENT_FORM_RESULT_KEY";

    public EditIngredientFragment() { }

    /**
     * Set up event listeners
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Edit");
    }

    public void initializeFormWithIngredientAttributes() {
        return;
    }

    void sendResult(String desc) {
//        This could be useful.
//        https://developer.android.com/guide/fragments/communicate#fragment-result
        Bundle ingredientBundle = new Bundle();
//        FIXME: can't submit ingredient right now... Hardcoded to description for now...
        ingredientBundle.putString(INGREDIENT_RESULT_KEY, desc);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.setFragmentResult(INGREDIENT_RESULT_KEY, ingredientBundle);
        closeFragment();
    }

    private void closeFragment() {
        getActivity().getFragmentManager().popBackStack();
    }
}