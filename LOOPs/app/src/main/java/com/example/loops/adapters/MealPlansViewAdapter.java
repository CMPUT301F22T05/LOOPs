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
import com.example.loops.models.MealPlan;

import java.util.ArrayList;

/**
 * The array adapter for meal plans
 */
public class MealPlansViewAdapter extends ArrayAdapter<MealPlan> {
    private Context context;
    private ArrayList<MealPlan> dataList;

    /**
     * Adapter constructor.
     * @param context
     * @param dataList
     */
    public MealPlansViewAdapter (Context context, ArrayList<MealPlan> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    /**
     * Gets the individual list view item for the meal plan
     * @param position position in the parent view container
     * @param convertView the individual list view item if it's already been created
     * @param parent parent view container
     * @return view of the meal plan
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_plans_row, parent, false);
        }

        TextView mealPlanName = convertView.findViewById(R.id.meal_plan_name);
        mealPlanName.setText(dataList.get(position).getName());

        return convertView;
    }
}
