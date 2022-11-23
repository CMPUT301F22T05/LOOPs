package com.example.loops;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.loops.adapters.RecipeCollectionViewAdapter;
import com.example.loops.adapters.ShoppingListViewAdapter;
import com.example.loops.ingredientFragments.IngredientCollectionSelectionFragment;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;
import com.example.loops.recipeFragments.RecipeCollectionSelectionFragment;
import com.example.loops.recipeFragments.forms.AddRecipeFormFragmentDirections;
import com.example.loops.recipeFragments.forms.AddRecipeIngredientFormFragment;
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

    private ShoppingListViewAdapter ingredientsViewAdapter;
    private RecipeCollectionViewAdapter recipesViewAdapter;

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
        ingredientsViewAdapter =
                new ShoppingListViewAdapter(getActivity(), ingredientCollection.getIngredients());

        ListView ingredientListView = view.findViewById(R.id.ingredients_listView);
        ingredientListView.setAdapter(ingredientsViewAdapter);
        // display recipes
        BaseRecipeCollection recipeCollection = mealPlan.getRecipes();
        recipeCollection.sort(RecipeSortOption.BY_TITLE_ASCENDING);
        recipesViewAdapter =
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
        // config add button
        Button addButton = view.findViewById(R.id.add_button);
        chooseAddWhatToMealPlan(addButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        //testRecipeCollectionSelectionFragment();
        handleNewAddedRecipes();
        handleNewAddedIngredients();
    }

    private void chooseAddWhatToMealPlan(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] ingredientSelectionOptions = new CharSequence[]{
                        "Recipes",
                        "Ingredients",
                        "Cancel"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog ingredientSelectionPrompt = builder
                        .setTitle( "What do you want to add to the mael plan?" )
                        .setItems(ingredientSelectionOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // navigate to add ingredient form
                                if (i == 0) {
                                    MealPlanFragmentDirections.ActionChooseRecipesForMealPlan addRecipeAction
                                            = MealPlanFragmentDirections.actionChooseRecipesForMealPlan();
                                    addRecipeAction.setRecipesToFilter(mealPlan.getRecipes());
                                    Navigation.findNavController(getView()).navigate(addRecipeAction);
                                }
                                else if (i == 1) {
                                    MealPlanFragmentDirections.ActionChooseIngredientsForMealPlan addIngredientAction
                                            = MealPlanFragmentDirections.actionChooseIngredientsForMealPlan();
                                    addIngredientAction.setIngredientsToFilter(new IngredientCollection()); //FIXME: NOT sure if need filter
                                    Navigation.findNavController(getView()).navigate(addIngredientAction);
                                }
                                else if (i == 2) {
                                    return;
                                }
                                else {
                                    throw new Error("Invalid selection");
                                }
                            }
                        })
                        .create();
                ingredientSelectionPrompt.show();
            }
        });
    }

    /**
     * Add new selected recipes to the meal plan
     */
    private void handleNewAddedRecipes() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView())
                .getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( RecipeCollectionSelectionFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
                    @Override
                    public void onChanged(@Nullable final Object selectedRecipes) {
                        BaseRecipeCollection s = (BaseRecipeCollection) selectedRecipes;
                        BaseRecipeCollection mealPlanRecipes = mealPlan.getRecipes();
                        for (Recipe recipe : s.getAllRecipes()) {
                            mealPlanRecipes.addRecipe(recipe);
                            Log.d("RECIPE_DEBUG", "SELECTED RECIPE " + recipe.getTitle() + " " + recipe.getNumServing());
                            for (Ingredient ingredient : recipe.getIngredients().getIngredients()) {
                                Log.d("RECIPE_DEBUG", "INGREDIENT " + ingredient.getDescription() + " " + ingredient.getAmount());
                            }
                        }
                        recipesViewAdapter.notifyDataSetChanged();
                        savedStateHandle.remove( RecipeCollectionSelectionFragment.RESULT_KEY );
                    }
                });
    }

    /**
     * add new selected ingredients to the meal plan
     */
    private void handleNewAddedIngredients() {
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView())
                .getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( IngredientCollectionSelectionFragment.RESULT_KEY )
                .observe(getViewLifecycleOwner(), new Observer<Object>() {
                    @Override
                    public void onChanged(@Nullable final Object selectedIngredients) {
                        IngredientCollection s = (IngredientCollection) selectedIngredients;
                        IngredientCollection mealPlanIngredients = mealPlan.getIngredients();
                        for (Ingredient ing : s.getIngredients()) {
                            mealPlanIngredients.addIngredient(ing);
                        }
                        ingredientsViewAdapter.notifyDataSetChanged();
                        savedStateHandle.remove( IngredientCollectionSelectionFragment.RESULT_KEY );
                    }
                });
    }

    // For Testing. I will push this so that the one implementing this class has a reference to work on
    private void testRecipeCollectionSelectionFragment() {
        Button addButton = getView().findViewById(R.id.add_button);
        BaseRecipeCollection recipeToFilter = new RecipeCollection();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to recipe selection fragment
//                MealPlanFragmentDirections.ActionMealPlanFragmentToRecipeCollectionSelectionFragment addRecipeAction
//                        = MealPlanFragmentDirections.actionMealPlanFragmentToRecipeCollectionSelectionFragment();
//                addRecipeAction.setRecipesToFilter(recipeToFilter);
//                Navigation.findNavController(getView()).navigate(addRecipeAction);
            }
        });

        // Handle response from recipe collection selection fragment
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
        savedStateHandle.getLiveData( RecipeCollectionSelectionFragment.RESULT_KEY )
        .observe(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(@Nullable final Object selectedRecipes) {
                BaseRecipeCollection s = (BaseRecipeCollection) selectedRecipes;
                for (Recipe recipe : s.getAllRecipes()) {
                    Log.d("RECIPE_DEBUG", "SELECTED RECIPE " + recipe.getTitle() + " " + recipe.getNumServing());
                    for (Ingredient ingredient : recipe.getIngredients().getIngredients()) {
                        Log.d("RECIPE_DEBUG", "INGREDIENT " + ingredient.getDescription() + " " + ingredient.getAmount());
                    }
                }
                savedStateHandle.remove( RecipeCollectionSelectionFragment.RESULT_KEY );
            }
        });
    }
}