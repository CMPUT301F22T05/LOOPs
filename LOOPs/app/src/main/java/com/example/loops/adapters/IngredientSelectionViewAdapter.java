package com.example.loops.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.loops.R;
import com.example.loops.models.Ingredient;
import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The array adapter for ingredients that are meant to be selected
 */
public class IngredientSelectionViewAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> dataList;
    private HashMap<String, Boolean> ingredientIsSelected;

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
    public IngredientSelectionViewAdapter(Context context, ArrayList<Ingredient> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
        this.ingredientIsSelected = new HashMap<>();
    }

    /**
     * Gets the individual list view item for the ingredient
     * @param position position in the parent view container
     * @param convertView the individual list view item if it's already been created
     * @param parent parent view container
     * @return view of the ingredient
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Ingredient currentIngredient = dataList.get(position);
        IngredientSelectionViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_selection_row, parent, false);
            holder = new IngredientSelectionViewAdapter.ViewHolder();
            holder.cardView = (CardView) convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (IngredientSelectionViewAdapter.ViewHolder) convertView.getTag();
        }

        TextView description = convertView.findViewById(R.id.ingredient_description_in_selection);
        TextView category = convertView.findViewById(R.id.ingredient_category_in_selection);
        TextView amount = convertView.findViewById(R.id.ingredient_amount_in_selection);
        TextView unit = convertView.findViewById(R.id.ingredient_unit_in_selection);
        TextView badge = convertView.findViewById(R.id.ingredient_badge_in_selection);

        description.setText(currentIngredient.getDescription());
        category.setText(currentIngredient.getCategory());
        badge.setText(String.valueOf(currentIngredient.getDescription().charAt(0)));

        MaterialDivider divider = convertView.findViewById(R.id.ingredient_divider_selection);
        int dividerDefColor = divider.getDividerColor();

        boolean isSelected = ingredientIsSelected.getOrDefault(currentIngredient.getDocumentName(), false);
        CardView cardView = holder.cardView;
        if ( isSelected ) {
            cardView.setCardBackgroundColor(Color.BLUE);
            description.setTextColor(Color.WHITE);
            category.setTextColor(Color.WHITE);
            amount.setTextColor(Color.WHITE);
            unit.setTextColor(Color.WHITE);
            divider.setDividerColor(Color.WHITE);
            badge.setText("\u2713");

            amount.setText(Double.toString(currentIngredient.getAmount()));
            unit.setText(currentIngredient.getUnit());
        }
        else {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.teal_200, null));
            amount.setText("");
            unit.setText("");
            badge.setText(String.valueOf(currentIngredient.getDescription().charAt(0)));
            description.setTextColor(Color.BLACK);
            category.setTextColor(Color.BLACK);
            amount.setTextColor(Color.BLACK);
            unit.setTextColor(Color.BLACK);
            divider.setDividerColor(dividerDefColor);
        }

        return convertView;
    }

    /**
     * Selects the item at the given position. Changes the background color of the associated view
     * @param position
     */
    public void selectItem(int position) {
        if (position >= dataList.size()) {
            Log.e("DEBUG", "Tried to select item not in the ingredients data list");
            return;
        }
        Ingredient selectedIngredient = dataList.get(position);
        boolean isSelected = ingredientIsSelected.getOrDefault(selectedIngredient.getDocumentName(), false);
        if ( isSelected )
            ingredientIsSelected.put(selectedIngredient.getDocumentName(), false);
        else
            ingredientIsSelected.put(selectedIngredient.getDocumentName(), true);
        notifyDataSetChanged();
    }
}
