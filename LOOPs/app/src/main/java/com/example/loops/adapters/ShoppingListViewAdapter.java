package com.example.loops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.R;
import com.example.loops.models.Ingredient;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingListViewAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> dataList;

    /**
     * Adapter constructor.
     * @param context
     * @param dataList
     */
    public ShoppingListViewAdapter(Context context, ArrayList<Ingredient> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_list_row, parent, false);
        }

        TextView description = convertView.findViewById(R.id.ingredient_description_in_shopping_list);
        TextView amount = convertView.findViewById(R.id.ingredient_amount_in_shopping_list);
        TextView unit = convertView.findViewById(R.id.ingredient_unit_in_shopping_list);
        TextView category = convertView.findViewById(R.id.ingredient_category_in_shopping_list);
        TextView letterBadge = convertView.findViewById(R.id.shopping_list_badge);

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        Ingredient currentIngredient = dataList.get(position);
        description.setText(currentIngredient.getDescription());
        amount.setText(df.format(currentIngredient.getAmount()));
        unit.setText(currentIngredient.getUnit());
        category.setText(currentIngredient.getCategory());
        letterBadge.setText(String.valueOf(currentIngredient.getDescription().charAt(0)));

        return convertView;
    }
}
