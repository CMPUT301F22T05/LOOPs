package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.loops.adapters.RecipeCollectionViewAdapter;
import com.example.loops.adapters.ShoppingListViewAdapter;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.MealPlan;
import com.example.loops.sortOption.IngredientSortOption;
import com.example.loops.sortOption.RecipeSortOption;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealPlanFragment extends Fragment {
    private MealPlan mealPlan;
    private int index;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealPlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealPlanFragment newInstance(String param1, String param2) {
        MealPlanFragment fragment = new MealPlanFragment();
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
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        index = MealPlanFragmentArgs.fromBundle(getArguments()).getMealPlanIndex();
        mealPlan = MealPlanFragmentArgs.fromBundle(getArguments()).getMealPlan();
        ((MainActivity)getActivity()).setActionBarTitle(mealPlan.getName());
        // display ingredients
        IngredientCollection ingredientCollection = mealPlan.getIngredients();
        ingredientCollection.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
        ShoppingListViewAdapter ingredientsViewAdapter =
                new ShoppingListViewAdapter(getActivity(), ingredientCollection.getIngredients());

        ListView ingredientListView = view.findViewById(R.id.ingredients_listView);
        ingredientListView.setAdapter(ingredientsViewAdapter);
        // display recipes
        RecipeCollection recipeCollection = mealPlan.getRecipes();
        recipeCollection.sort(RecipeSortOption.BY_TITLE_ASCENDING);
        RecipeCollectionViewAdapter recipesViewAdapter =
                new RecipeCollectionViewAdapter(getActivity(), recipeCollection.getAllRecipes());

        ListView recipeListView = view.findViewById(R.id.recipes_listView);
        recipeListView.setAdapter(recipesViewAdapter);

        // config save button
        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealPlanFragmentDirections.ActionSaveMealPlan saveMealPlanAction
                        = MealPlanFragmentDirections.actionSaveMealPlan();
                saveMealPlanAction.setMealPlanIndex(index);
                saveMealPlanAction.setUpdatedMealPlan(mealPlan);
                Navigation.findNavController(view).navigate(saveMealPlanAction);
            }
        });
        return view;
    }
}