package com.example.loops.shoppingListFragment;

import static androidx.core.view.ViewCompat.setBackgroundTintList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.database.Database;
import com.example.loops.factory.IngredientCollectionFactory;
import com.example.loops.ingredientFragments.IngredientCollectionFragment;
import com.example.loops.models.Ingredient;

public class ShoppingListFragment extends IngredientCollectionFragment {

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        //collectionTitle.setText(R.string.shopping_list);
        return fragmentView;
    }

//    @Override
//    protected void onClickAddButton(View clickedView) {
//
//    }

    @Override
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        Ingredient selectedIngredient = ingredientCollection.getIngredients().get(position);

        LayoutInflater inflater = getLayoutInflater();
        View pickupPopupView = inflater.inflate(R.layout.popup_ingredient_delete, null);

        PopupWindow pickupPopupWindow = new PopupWindow(
                pickupPopupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
        Button pickupNoButton = pickupPopupView.findViewById(R.id.delete_popup_no_button);
        Button pickupYesButton = pickupPopupView.findViewById(R.id.delete_popup_yes_button);
        TextView pickupConfirmText = pickupPopupView.findViewById(R.id.delete_popup_message);
        TextView popupTitle = pickupPopupView.findViewById(R.id.popup_window_title);
        CardView popupCard = pickupPopupView.findViewById(R.id.popup_cardview);
        //LinearLayout popupWindow = pickupPopupView.findViewById(R.id.popup_window_frame);

        popupTitle.setText("Confirm");
        //popupTitle.setBackgroundColor(Color.parseColor("#2c8f04"));
        //popupWindow.setBackgroundColor(Color.parseColor("#b6f59d"));
        popupTitle.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(),R.color.color_states));
        popupCard.setCardBackgroundColor(Color.parseColor("#b6f59d"));
        pickupNoButton.setBackgroundColor(Color.parseColor("#FF6A6667"));
        pickupYesButton.setBackgroundColor(Color.parseColor("#2196F3"));
        pickupConfirmText.setText(String.format("%s has been picked up?", ingredientCollection.getIngredients().get(position).getDescription()));

        pickupNoButton.setOnClickListener(view1 -> {
            pickupPopupWindow.dismiss();
        });
        pickupYesButton.setOnClickListener(view1 -> {
            pickupPopupWindow.dismiss();
            Ingredient pendingIng = new Ingredient(ingredientCollection.getIngredients().get(position));
            pendingIng.setPending(true);
            Database.getInstance().addDocument(pendingIng);
            ((MainActivity)getActivity()).getIngredientStorage().addIngredient(pendingIng);
            ingredientCollection.deleteIngredient(position);
            collectionViewAdapter.notifyDataSetChanged();
        });

        pickupPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void parseArguments() {
        return;
    }

    @Override
    protected IngredientCollectionFactory.CollectionType getCollectionType() {
        return IngredientCollectionFactory.CollectionType.FROM_SHOPPING_LIST_FOR_EDIT;
    }
}