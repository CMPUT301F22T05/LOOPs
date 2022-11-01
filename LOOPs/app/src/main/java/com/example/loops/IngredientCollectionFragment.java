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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientCollectionFragment extends GenericCollectionLayout {
    private IngredientCollection allIngredients;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientCollectionFragment newInstance(String param1, String param2) {
        IngredientCollectionFragment fragment = new IngredientCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_collection, container, false);
        bindComponents(view);
        collectionTitle.setText(R.string.ingredientCollection);
        ArrayAdapter<CharSequence> sortOptionSpinnerAdapter =
                ArrayAdapter.createFromResource(getActivity(),
                    R.array.ingredient_collection_sort_option, android.R.layout.simple_spinner_item);
        sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);

        //region hard code
        /*IngredientCollection ingredientCollection = new IngredientCollection();
        Ingredient ingredient;
        try{
            ingredient = new Ingredient("XXXXX", "2022-10-27", "fridge", 1, "UU", "CC");
            ingredientCollection.addIngredient(ingredient);
            ingredientCollection.addIngredient(ingredient);
        } catch (Exception e) {

        }
        collectionView.setAdapter(new IngredientStorageViewAdapter(getActivity(), ingredientCollection.getIngredients()));*/
        //endregion
        allIngredients = ((MainActivity)getActivity()).allIngredients;
        Ingredient submittedIngredient = IngredientCollectionFragmentArgs.fromBundle(getArguments()).getAddedIngredient();
        if (submittedIngredient != null) {
            allIngredients.addIngredient(submittedIngredient);
            getArguments().clear();
        }
        IngredientStorageViewAdapter collectionViewAdapter = new IngredientStorageViewAdapter(getActivity(), allIngredients.getIngredients());
        collectionView.setAdapter(collectionViewAdapter);

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
                collectionViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //useless?
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.addIngredientFromCollection);
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
        return view;
    }
}