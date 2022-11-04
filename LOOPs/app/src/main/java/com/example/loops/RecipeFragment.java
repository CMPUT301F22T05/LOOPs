package com.example.loops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.time.Duration;


/**
 * A class to represent the UI and business logic that happens
 * when you checkout a specific recipe in RecipeCollectionFragment
 */
public class RecipeFragment extends Fragment implements RecyclerViewOnClickInterface{
    private Recipe selectedRecipe;
    private Button editRecipeButton;
    private RecyclerView recipeIngredients;
    private TextView prepTime;
    private TextView servingSize;
    private TextView recipeComments;
    private TextView recipeTitle;
    private ShapeableImageView recipeImage;
    private TextView recipeCategory;
    private RecyclerView.Adapter recipeIngredientsAdapter;
    private RecyclerView.LayoutManager recipeIngredientsLayoutManager;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     *  Method to inflate fragment's xml
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        return view;
    }

    /**
     * Method to set up UI interactions
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        selectedRecipe = RecipeFragmentArgs.fromBundle(getArguments()).getSelectedRecipe();
        bindComponents(view);
        putContentOnViews();
        setUpRecyclerView(view);
        setEditRecipeButtonOnClick();
        setOnSwipeDeleteIngredients();




    }

    /**
     * This binds variables to the textViews, recyclerView, shapeableImageView in fragment_recipe.xml
     * @param view
     */
    private void bindComponents(View view){
        editRecipeButton = view.findViewById(R.id.editRecipeButton);
        prepTime = view.findViewById(R.id.recipePrepTime);
        servingSize = view.findViewById(R.id.recipeServing);
        recipeComments = view.findViewById(R.id.recipeComment);
        recipeTitle = view.findViewById(R.id.recipeTitle);
        recipeImage = view.findViewById(R.id.recipeImage);
        recipeCategory = view.findViewById(R.id.recipeCategory);
        recipeIngredients = view.findViewById(R.id.recipeIngredientList);
    }

    /**
     * Puts recipe attributes that is not the ingredients on views
     */
    private void putContentOnViews(){
        String time = String.format("%d hrs %d min", selectedRecipe.getPrepTimeHour(), selectedRecipe.getPrepTimeMinute());
        prepTime.setText(time);
        recipeImage.setImageBitmap(selectedRecipe.getPhoto());
        servingSize.setText("" + selectedRecipe.getNumServing());
        recipeCategory.setText(selectedRecipe.getCategory());
        recipeTitle.setText(selectedRecipe.getTitle());
        recipeComments.setText(selectedRecipe.getComments());
    }

    /**
     * Displays the description, count, and unit of an ingredient in a recipe on
     * a recyclerview
     * @param view
     */
    private void setUpRecyclerView(View view){
        recipeIngredients.setHasFixedSize(true);
        recipeIngredientsLayoutManager = new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        recipeIngredients.setLayoutManager(recipeIngredientsLayoutManager);
        recipeIngredientsAdapter = new RecipeIngredientsAdapter (selectedRecipe.getIngredients(),view.getContext(),this);
        recipeIngredients.setAdapter(recipeIngredientsAdapter);

    }

    /**
     * this is a button that takes us to a fragment that allows for a recipe to be edited
     */
    private void setEditRecipeButtonOnClick() {
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeFragmentDirections.ActionRecipeFragmentToPlaceholder action =
                        RecipeFragmentDirections.actionRecipeFragmentToPlaceholder(selectedRecipe);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    /**
     * Creates swipe interactions on recyclerView
     */
    private void setOnSwipeDeleteIngredients() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                selectedRecipe.getIngredients().deleteIngredient(pos);
                recipeIngredientsAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recipeIngredients);
    }

    @Override
    public void OnItemClick(int position) {

    }
}