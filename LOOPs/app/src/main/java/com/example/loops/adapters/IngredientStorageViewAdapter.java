package com.example.loops.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.R;
import com.example.loops.models.Ingredient;

import java.util.ArrayList;

/**
 * This is the adapter class for ingredient storage collection listview.
 */
public class IngredientStorageViewAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> dataList;

    /**
     * Adapter constructor.
     * @param context
     * @param dataList
     */
    public IngredientStorageViewAdapter(Context context, ArrayList<Ingredient> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_storage_row, parent, false);
        }

        TextView description = convertView.findViewById(R.id.ingredient_description_in_storage);
        TextView BestBeforeDate = convertView.findViewById(R.id.ingredient_bbd_in_storage);
        TextView location = convertView.findViewById(R.id.ingredient_location_in_storage);
        TextView category = convertView.findViewById(R.id.ingredient_category_in_storage);

        Ingredient currentIngredient = dataList.get(position);
        description.setText(currentIngredient.getDescription());
        BestBeforeDate.setText("Best before:  " + currentIngredient.getBestBeforeDateString());
        location.setText(currentIngredient.getStoreLocation());
        category.setText(currentIngredient.getCategory());
        if (currentIngredient.getPending()) {
            convertView.setBackgroundColor(Color.RED);
            BestBeforeDate.setText("N/A");
            location.setText("N/A");
        }

        return convertView;
    }
}
