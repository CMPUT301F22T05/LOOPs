package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;


public class RecipeFragment extends Fragment {

    private Recipe selectedRecipe;
    private Button editRecipeButton;
    private RecyclerView recipeIngredients;
    private TextView prepTime;
    private TextView servingSize;
    private TextView recipeComments;
    private TextView recipeTitle;
    private ShapeableImageView recipeImage;
    private TextView recipeCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        if (getArguments() != null){
            RecipeFragmentArgs args = RecipeFragmentArgs.fromBundle(getArguments());
            selectedRecipe = args.getSelectedRecipe();
        }
        bindComponents(view);
        return view;
    }
    private void bindComponents(View view){
        editRecipeButton = view.findViewById(R.id.editRecipeButton);
        prepTime = view.findViewById(R.id.recipePrepTime);
        servingSize = view.findViewById(R.id.recipeServing);
        recipeComments = view.findViewById(R.id.recipeComment);
        recipeTitle = view.findViewById(R.id.recipeTitle);
        recipeImage = view.findViewById(R.id.recipeImage);
        recipeCategory = view.findViewById(R.id.recipeCategory);

    }
}