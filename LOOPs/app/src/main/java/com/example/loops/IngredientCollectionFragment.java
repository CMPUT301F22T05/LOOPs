package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class IngredientCollectionFragment extends GenericCollectionLayout {
    private IngredientCollection allIngredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_collection, container, false);
        bindComponents(view);
        collectionTitle.setText(R.string.ingredientCollection);

        readPassingData();
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

        IngredientStorageViewAdapter collectionViewAdapter = new IngredientStorageViewAdapter(getActivity(), allIngredients.getIngredients());
        collectionView.setAdapter(collectionViewAdapter);
    }

    private void setListeners() {
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals(getString(R.string.empty_sort_option))) {
                    return;
                }
                else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_description))) {
                    allIngredients.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
                }
                else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_best_before_date))) {
                    allIngredients.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
                }
                else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Location))) {
                    allIngredients.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
                }
                else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Category))) {
                    allIngredients.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
                }
                ((IngredientStorageViewAdapter)collectionView.getAdapter()).notifyDataSetChanged();
                //collectionViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //useless?
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addIngredientFromCollection);
            }
        });

        collectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient selectedIngredient = allIngredients.getIngredients().get(position);
                NavDirections viewIngredientDetailsAction =
                        IngredientCollectionFragmentDirections.actionViewIngredientDetails(
                                selectedIngredient, position, R.layout.fragment_ingredient_collection);
                Navigation.findNavController(view).navigate(viewIngredientDetailsAction);
            }
        });
    }

    private void readPassingData() {
        allIngredients = ((MainActivity)getActivity()).allIngredients;
        Ingredient submittedIngredient = IngredientCollectionFragmentArgs.fromBundle(getArguments()).getAddedIngredient();
        if (submittedIngredient != null) {
            allIngredients.addIngredient(submittedIngredient);
            getArguments().clear();
        }
    }
}