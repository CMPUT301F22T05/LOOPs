package com.example.loops.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.R;
import com.example.loops.models.Recipe;

import java.time.Duration;
import java.util.ArrayList;

/**
 * Adapter for recipe collection items.
 */
public class RecipeCollectionViewAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private ArrayList<Recipe> dataList;

    /**
     * Creates an adapter for recipes
     * @param context
     * @param dataList
     */
    public RecipeCollectionViewAdapter(Context context, ArrayList<Recipe> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    /**
     * Gets the individual list view item for the recipe
     * @param position position in the parent view container
     * @param convertView the individual list view item if it's already been created
     * @param parent parent view container
     * @return view of the recipe
     */
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
        numServings.setText("Serves " + Integer.toString(currentRecipe.getNumServing()));
        category.setText(currentRecipe.getCategory());
        prepTime.setText(getPrepTimeString(currentRecipe.getPrepTime()));

        Bitmap photo = currentRecipe.getPhoto();
        if (photo != null) {
            recipeImage.setImageBitmap(photo);
        }

        return convertView;
    }

    /**
     * Converts recipe's prep time into a string of format x hours x mins
     * @param prepTime
     * @return string representation of the duration
     */
    private String getPrepTimeString(Duration prepTime) {
        long seconds = prepTime.getSeconds();
        String prepTimeString = "";
        // Add hours
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            prepTimeString += String.format("%d hour", hours) + ( (hours > 1) ? "s ": " " );
        }
        // Add minutes
        if (minutes > 0) {
            prepTimeString += String.format("%d min", minutes) + ( (minutes > 1) ? "s ": " " );
        }
        return prepTimeString.trim();
    }
}
