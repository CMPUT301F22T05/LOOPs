package com.example.loops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IngredientStorageViewAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> dataList;

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
        BestBeforeDate.setText("Best Before " + currentIngredient.getBestBeforeDateString());
        location.setText(currentIngredient.getStoreLocation());
        category.setText(currentIngredient.getCategory());

        return convertView;
    }
}