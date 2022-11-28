package com.example.loops;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
 * Display a particular meal plan. That is, a meal plan for a particular day and time
 */
public class MealPlanFragment extends Fragment {
    private MealPlan mealPlan;
    private int index;

    private ShoppingListViewAdapter ingredientsViewAdapter;
    private RecipeCollectionViewAdapter recipesViewAdapter;

    /**
     * Parses fragment's argument, set appropriate title bar,
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        index = MealPlanFragmentArgs.fromBundle(getArguments()).getMealPlanIndex();
        mealPlan = MealPlanFragmentArgs.fromBundle(getArguments()).getMealPlan();
        /*
         * The try catch is to allow the intent test for this fragment to pass the
         * casting error since the test uses fragment activity instead of activities
         */
        try{
            ((MainActivity)getActivity()).setActionBarTitle(mealPlan.getName());}
        catch (Exception e){

        }

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

    /**
     * Sets the behavior when recipes or ingredients are added to the fragment
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleNewAddedRecipes();
        handleNewAddedIngredients();
    }

    /**
     * Opens up a prompt to select what to add to the meal plan (ingredient or recipe)
     * @param view
     */
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
                        .setTitle( "What do you want to add to the meal plan?" )
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
        SavedStateHandle savedStateHandle = Navigation.findNavController(getView()).getCurrentBackStackEntry().getSavedStateHandle();
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
}