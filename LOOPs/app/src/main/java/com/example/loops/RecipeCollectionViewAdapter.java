package com.example.loops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeCollectionViewAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private ArrayList<Recipe> dataList;

    public RecipeCollectionViewAdapter(Context context, ArrayList<Recipe> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_collection_row, parent, false);
        }

        TextView title = convertView.findViewById(R.id.recipe_title_in_collection);
        TextView prepTime = convertView.findViewById(R.id.recipe_prep_time_in_collection);
        TextView numServings = convertView.findViewById(R.id.recipe_servings_in_collection);
        TextView category = convertView.findViewById(R.id.recipe_category_in_collection);
        ImageView recipeImage = convertView.findViewById(R.id.recipe_image_in_collection);

        Recipe currentRecipe = dataList.get(position);
        title.setText(currentRecipe.getTitle());
        numServings.setText(currentRecipe.getNumServing());
        category.setText(currentRecipe.getCategory());

        // TODO: Implement prep time localdate conversion
        //prepTime.setText(currentRecipe.getPrepTime());

        // TODO: Implement image

        return convertView;
    }
}
