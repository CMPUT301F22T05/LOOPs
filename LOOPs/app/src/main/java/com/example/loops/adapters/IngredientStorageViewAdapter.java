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
import androidx.core.content.ContextCompat;

import com.example.loops.R;
import com.example.loops.models.Ingredient;
import com.google.android.material.divider.MaterialDivider;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

        Ingredient currentIngredient = dataList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_storage_row, parent, false);
        }

        TextView description = convertView.findViewById(R.id.ingredient_description_in_storage);
        TextView BestBeforeDate = convertView.findViewById(R.id.ingredient_bbd_in_storage);
        TextView location = convertView.findViewById(R.id.ingredient_location_in_storage);
        TextView category = convertView.findViewById(R.id.ingredient_category_in_storage);
        TextView amount = convertView.findViewById(R.id.ingredient_amount_in_storage);
        TextView unit = convertView.findViewById(R.id.ingredient_unit_in_storage);
        TextView letterBadge = convertView.findViewById(R.id.ingredient_badge_in_selection);

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        description.setText(currentIngredient.getDescription());
        BestBeforeDate.setText(currentIngredient.getBestBeforeDateString());
        location.setText(currentIngredient.getStoreLocation());
        category.setText(currentIngredient.getCategory());
        amount.setText(df.format(currentIngredient.getAmount()));
        unit.setText(currentIngredient.getUnit());
        letterBadge.setText(String.valueOf(currentIngredient.getDescription().charAt(0)));

        MaterialDivider divider = convertView.findViewById(R.id.ingredient_divider_storage);

        if (currentIngredient.getPending()) {
            //convertView.setBackgroundColor(Color.RED);
            CardView card = convertView.findViewById(R.id.ingredient_cardview);
            card.setCardBackgroundColor(ContextCompat.getColor(this.context,R.color.red));
            BestBeforeDate.setText("N/A");
            location.setText("N/A");

            divider.setDividerColor(Color.WHITE);
            description.setTextColor(Color.WHITE);
            BestBeforeDate.setTextColor(Color.WHITE);
            location.setTextColor(Color.WHITE);
            category.setTextColor(Color.WHITE);
            amount.setTextColor(Color.WHITE);
            unit.setTextColor(Color.WHITE);
        } else {

            CardView card = convertView.findViewById(R.id.ingredient_cardview);
            card.setCardBackgroundColor(ContextCompat.getColor(this.context,R.color.teal_200));
            divider.setDividerColor(Color.parseColor("#808080"));
            description.setTextColor(Color.BLACK);
            BestBeforeDate.setTextColor(Color.BLACK);
            location.setTextColor(Color.BLACK);
            category.setTextColor(Color.BLACK);
            amount.setTextColor(Color.BLACK);
            unit.setTextColor(Color.BLACK);

        }

        return convertView;
    }
}
