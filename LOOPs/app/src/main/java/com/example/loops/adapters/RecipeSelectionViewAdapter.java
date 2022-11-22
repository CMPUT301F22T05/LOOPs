package com.example.loops.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.loops.R;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeSelectionViewAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private ArrayList<Recipe> dataList;
    private HashMap<String, Boolean> recipeIsSelected;
    private HashMap<String, Integer> selectedRecipeNumServings;

    /**
     * https://stackoverflow.com/questions/6921462/listview-reusing-views-when-i-dont-want-it-to
     * Date accessed: 2022-11-18
     * Author: Xavi Gil
     */
    static class ViewHolder {
        CardView cardView;
    }

    /**
     * Adapter constructor.
     * @param context
     * @param dataList
     */
    public RecipeSelectionViewAdapter(Context context, ArrayList<Recipe> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
        this.recipeIsSelected = new HashMap<>();
        this.selectedRecipeNumServings = new HashMap<>();
    }

    /**
     * Creates the view of each recipe item in the view adapter
     * @param position position in adapter
     * @param convertView view of the recipe item if already created
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Recipe currentRecipe = dataList.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_collection_row, parent, false);
            holder = new ViewHolder();
            holder.cardView = (CardView) convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView title = convertView.findViewById(R.id.recipe_title_in_collection);
        TextView prepTime = convertView.findViewById(R.id.recipe_prep_time_in_collection);
        TextView numServings = convertView.findViewById(R.id.recipe_servings_in_collection);
        TextView category = convertView.findViewById(R.id.recipe_category_in_collection);
        ImageView recipeImage = convertView.findViewById(R.id.recipe_image_in_collection);

        title.setText(currentRecipe.getTitle());
        category.setText(currentRecipe.getCategory());
        prepTime.setText(getPrepTimeString(currentRecipe.getPrepTime()));
        Bitmap photo = currentRecipe.getPhoto();
        if (photo != null) {
            recipeImage.setImageBitmap(photo);
        }

        boolean isSelected = recipeIsSelected.getOrDefault(currentRecipe.getDocumentName(), false);
        CardView cardView = holder.cardView;
        if ( isSelected ) {
            cardView.setCardBackgroundColor(Color.BLUE);
            title.setTextColor(Color.WHITE);
            prepTime.setTextColor(Color.WHITE);
            numServings.setTextColor(Color.WHITE);
            category.setTextColor(Color.WHITE);

            numServings.setText("Serves " + Integer.toString(selectedRecipeNumServings.get(currentRecipe.getDocumentName())));
        }
        else {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.teal_200, null));
            numServings.setText("");

            title.setTextColor(Color.BLACK);
            prepTime.setTextColor(Color.BLACK);
            numServings.setTextColor(Color.BLACK);
            category.setTextColor(Color.BLACK);

        }

        return convertView;
    }

    /**
     * Selects the item at the given position. Changes the background color of the associated view
     * @param position
     * @param newNumServings if being selected, saves recipe's number of servings to it
     */
    public void selectItem(int position, int newNumServings) {
        if (position >= dataList.size()) {
            Log.e("DEBUG", "Tried to select item not in the recipes data list");
            return;
        }
        Recipe selectedRecipe = dataList.get(position);
        boolean isSelected = recipeIsSelected.getOrDefault(selectedRecipe.getDocumentName(), false);
        if ( isSelected ) {
            recipeIsSelected.put(selectedRecipe.getDocumentName(), false);
        }
        else {
            recipeIsSelected.put(selectedRecipe.getDocumentName(), true);
            selectedRecipeNumServings.put(selectedRecipe.getDocumentName(), newNumServings);
        }
        notifyDataSetChanged();
    }

    /**
     * Gets the number of servings saved for the given recipe
     * @param recipe
     * @return Number of servings saved for recipe. If not saved, then -1.
     */
    public int getNumberOfServings(Recipe recipe) {
        return selectedRecipeNumServings.getOrDefault(recipe.getDocumentName(), -1);
    }

    /**
     * Converts recipe's prep time into a string of format x hours x mins
     * @param prepTime
     * @return
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
