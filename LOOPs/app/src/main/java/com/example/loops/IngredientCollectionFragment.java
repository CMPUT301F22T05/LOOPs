package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * A fragment displaying an ingredient collection
 */
public abstract class IngredientCollectionFragment extends GenericCollectionLayout {
    protected IngredientCollection ingredientCollection;
    protected IngredientStorageViewAdapter collectionViewAdapter;

    public IngredientCollectionFragment() {
        // Required empty public constructor
    }

    abstract void onClickAddButton(View clickedView);

    abstract void onClickIngredient(AdapterView<?> parent, View view, int position, long id);

    abstract void getIngredientCollectionToDisplay();

    abstract void parseActionArguments();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_collection, container, false);
        bindComponents(view);

        ingredientCollection = ((MainActivity)getActivity()).allIngredients;
        parseActionArguments();
        setAdapters();
        setListeners();

        return view;
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> sortOptionSpinnerAdapter =
                ArrayAdapter.createFromResource(getActivity(),
                        R.array.ingredient_collection_sort_option, android.R.layout.simple_spinner_item);
        sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);

        adaptIngredientCollection();
    }

    private void setListeners() {
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortIngredientCollection(parent, position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //useless?
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddButton(v);
            }
        });

        collectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickIngredient(parent, view, position, id);
            }
        });
    }

    private void sortIngredientCollection(AdapterView<?> parent, int position) {
        if (parent.getItemAtPosition(position).equals(getString(R.string.empty_sort_option))) {
            return;
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_description))) {
            ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_best_before_date))) {
            ingredientCollection.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Location))) {
            ingredientCollection.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Category))) {
            ingredientCollection.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
        }
        collectionViewAdapter.notifyDataSetChanged();
    }

    private void adaptIngredientCollection() {
        collectionViewAdapter = new IngredientStorageViewAdapter(getActivity(),
                    ingredientCollection.getIngredients());
        collectionView.setAdapter(collectionViewAdapter);
    }
}