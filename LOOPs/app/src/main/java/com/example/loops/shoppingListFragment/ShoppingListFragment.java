package com.example.loops.shoppingListFragment;

import android.os.Bundle;

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
        collectionTitle.setText(R.string.shopping_list);
        return fragmentView;
    }

    @Override
    protected void onClickAddButton(View clickedView) {

    }

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
        pickupConfirmText.setText("Already picked up this ingredient?");
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
        setIngredientCollectionToDisplay(CollectionType.FROM_SHOPPING_LIST);
    }
}