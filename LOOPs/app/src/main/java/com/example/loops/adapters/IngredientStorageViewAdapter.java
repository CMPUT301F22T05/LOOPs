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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the adapter class for ingredient storage collection listview.
 */
public class IngredientStorageViewAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private ArrayList<Ingredient> dataList;
    private HashMap<String, ViewState> viewState;


    /**
     * https://stackoverflow.com/questions/6921462/listview-reusing-views-when-i-dont-want-it-to
     * Date accessed: 2022-11-18
     * Author: Xavi Gil
     */
    static class ViewHolder {
        CardView cardView;
    }

    /**
     * The various state each ingredient could be in
     */
    public enum ViewState {
        Unselected,
        Selected,
        Pending
    }

    /**
     * Adapter constructor.
     * @param context
     * @param dataList
     */
    public IngredientStorageViewAdapter(Context context, ArrayList<Ingredient> dataList) {
        super(context, 0, dataList);
        this.context = context;
        this.dataList = dataList;
        this.viewState = new HashMap<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Ingredient currentIngredient = dataList.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_storage_row, parent, false);
            holder = new ViewHolder();
            holder.cardView = (CardView) convertView;
            convertView.setTag(holder);

            if (currentIngredient.getPending()) {
                viewState.put(currentIngredient.getDocumentName(), ViewState.Pending);
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView description = convertView.findViewById(R.id.ingredient_description_in_storage);
        TextView BestBeforeDate = convertView.findViewById(R.id.ingredient_bbd_in_storage);
        TextView location = convertView.findViewById(R.id.ingredient_location_in_storage);
        TextView category = convertView.findViewById(R.id.ingredient_category_in_storage);


        description.setText(currentIngredient.getDescription());
        BestBeforeDate.setText("Best before:  " + currentIngredient.getBestBeforeDateString());
        location.setText(currentIngredient.getStoreLocation());
        category.setText(currentIngredient.getCategory());
        if (currentIngredient.getPending()) {
            BestBeforeDate.setText("N/A");
            location.setText("N/A");
        }

        ViewState currentIngredientState = viewState.getOrDefault(currentIngredient.getDocumentName(), ViewState.Unselected);
        holder.cardView.setCardBackgroundColor(getBackgroundColorForState(currentIngredientState));

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
        ViewState currentState = viewState.getOrDefault(selectedIngredient.getDocumentName(), ViewState.Unselected);
        if (currentState == ViewState.Selected)
            viewState.put(selectedIngredient.getDocumentName(), ViewState.Unselected);
        else if (currentState == ViewState.Unselected)
            viewState.put(selectedIngredient.getDocumentName(), ViewState.Selected);
        notifyDataSetChanged();
    }

    /**
     * Gets the background color id for given state
     * @param state
     * @return
     */
    private int getBackgroundColorForState(ViewState state) {
        if (state == ViewState.Unselected) {
            return context.getResources().getColor(R.color.teal_200, null);
        }
        else if (state == ViewState.Selected) {
            return Color.BLUE;
        }
        else if (state == ViewState.Pending) {
            return Color.RED;
        }
        else {
            throw new Error("No background color for the given state");
        }
    }
}
