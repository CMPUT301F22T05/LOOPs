package com.example.loops.shoppingListFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.loops.R;
import com.example.loops.ingredientFragments.IngredientCollectionFragment;

public class ShoppingListFragment extends IngredientCollectionFragment {

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        collectionTitle.setText(R.string.shopping_list);
        return fragmentView;
    }

    @Override
    protected void onClickAddButton(View clickedView) {

    }

    @Override
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void parseArguments() {
        setIngredientCollectionToDisplay(CollectionType.FROM_SHOPPING_LIST);
    }
}