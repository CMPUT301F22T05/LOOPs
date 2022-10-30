package com.example.loops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IngredientFragment extends Fragment {
    // selected ingredient from ingredient collection
    private Ingredient ingredient;

    // all text views
    private TextView descriptionText;
    private TextView bestBeforeDateText;
    private TextView locationText;
    private TextView amountText;
    private TextView unitText;
    private TextView categoryText;

    // all buttons
    private Button backButton;
    private Button editButton;
    private Button deleteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // init all elements
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        descriptionText = view.findViewById(R.id.ingredient_description);
        bestBeforeDateText = view.findViewById(R.id.ingredient_best_before_date);
        locationText = view.findViewById(R.id.ingredient_location);
        amountText = view.findViewById(R.id.ingredient_amount);
        unitText = view.findViewById(R.id.ingredient_unit);
        categoryText = view.findViewById(R.id.ingredient_category);

        backButton = view.findViewById(R.id.ingredient_edit_button);
        deleteButton = view.findViewById(R.id.ingredient_delete_button);

        //

        return view;
    }
}
