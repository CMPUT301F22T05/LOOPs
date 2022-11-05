package com.example.loops.recipeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.loops.R;
import com.example.loops.models.Recipe;
import com.example.loops.adapters.RecipeIngredientsAdapter;
import com.example.loops.RecyclerViewOnClickInterface;
import com.google.android.material.imageview.ShapeableImageView;

import java.time.Duration;


/**
 * A class to represent the UI and business logic that happens
 * when you checkout a specific recipe in RecipeCollectionFragment
 */
public class RecipeFragment extends Fragment implements RecyclerViewOnClickInterface {
    private Recipe selectedRecipe;
    private Button editRecipeButton;
    private Button backToRecipeCollection;
    private Button deleteRecipeButton;
    private TextView prepTime;
    private TextView servingSize;
    private TextView recipeComments;
    private TextView recipeTitle;
    private ShapeableImageView recipeImage;
    private TextView recipeCategory;
    private RecyclerView recipeIngredients;
    private RecyclerView.Adapter recipeIngredientsAdapter;
    private RecyclerView.LayoutManager recipeIngredientsLayoutManager;
    private Integer recipeIndex;
    private Integer fromWhichFragment;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     *  Method to inflate fragment's xml
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        return view;
    }

    /**
     * Method to set up UI interactions
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipeIndex = RecipeFragmentArgs.fromBundle(getArguments()).getSelectedRecipeIndex();
        selectedRecipe = RecipeFragmentArgs.fromBundle(getArguments()).getSelectedRecipe();
        fromWhichFragment = RecipeFragmentArgs.fromBundle(getArguments()).getFromWhichFragment();

        bindComponents(view);
        putContentOnViews();
        setUpRecyclerView(view);
        setEditRecipeButtonOnClick();
        setOnSwipeDeleteIngredients();
        setBackToRecipeCollectionOnClick();
        setDeleteRecipeButton(view);

    }

    /**
     * This binds variables to the textViews, recyclerView, shapeableImageView in fragment_recipe.xml
     * @param view
     */
    private void bindComponents(View view){
        backToRecipeCollection = view.findViewById(R.id.backToRecipeCollection);
        deleteRecipeButton = view.findViewById(R.id.deleteRecipeButton);
        editRecipeButton = view.findViewById(R.id.editRecipeButton);
        prepTime = view.findViewById(R.id.recipePrepTime);
        servingSize = view.findViewById(R.id.recipeServing);
        recipeComments = view.findViewById(R.id.recipeComment);
        recipeTitle = view.findViewById(R.id.recipeTitle);
        recipeImage = view.findViewById(R.id.recipeImage);
        recipeCategory = view.findViewById(R.id.recipeCategory);
        recipeIngredients = view.findViewById(R.id.recipeIngredientList);
    }

    /**
     * Puts recipe attributes that is not the ingredients on views
     */
    private void putContentOnViews(){
        Duration time = selectedRecipe.getPrepTime();
        long seconds = time.getSeconds();
        String duration = String.format("%d:%02d",seconds/3600,(seconds%3600)/60);
        prepTime.setText(duration);
        // FIXME: hard-coded image
        //selectedRecipe.setPhoto(BitmapFactory.decodeResource(getResources(), R.drawable.fried_chicken_test_picutre));

        recipeImage.setImageBitmap(selectedRecipe.getPhoto());
        servingSize.setText("" + selectedRecipe.getNumServing());
        recipeCategory.setText(selectedRecipe.getCategory());
        recipeTitle.setText(selectedRecipe.getTitle());
        recipeComments.setText(selectedRecipe.getComments());

    }

    /**
     * Displays the description, count, and unit of an ingredient in a recipe on
     * a recyclerview
     * @param view
     */
    private void setUpRecyclerView(View view){
        recipeIngredients.setHasFixedSize(true);
        recipeIngredientsLayoutManager = new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        };
        recipeIngredients.setLayoutManager(recipeIngredientsLayoutManager);
        recipeIngredientsAdapter = new RecipeIngredientsAdapter(selectedRecipe.getIngredients(),view.getContext(),this);
        recipeIngredients.setAdapter(recipeIngredientsAdapter);

    }

    /**
     * this is a button that takes us to a fragment that allows for a recipe to be edited
     */
    private void setEditRecipeButtonOnClick() {
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeFragmentDirections.ActionRecipeFragmentToEditRecipeFormFragment action =
                        RecipeFragmentDirections.actionRecipeFragmentToEditRecipeFormFragment(selectedRecipe, recipeIndex);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    /**
     * Sets the delete button to invoke a popup window for use to confirm deletetion to be avoid
     * accidentally delete.
     * @param parentView
     */
    private void setDeleteRecipeButton(View parentView) {
        LayoutInflater inflater = getLayoutInflater();
        View deletePopupView = inflater.inflate(R.layout.popup_ingredient_delete, null);
        PopupWindow deletePopupWindow = new PopupWindow(
                deletePopupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
        /*
         * Binds buttons and textviews.
         */
        Button popupDeleteNoButton = deletePopupView.findViewById(R.id.delete_popup_no_button);
        Button popupDeleteYesButton = deletePopupView.findViewById(R.id.delete_popup_yes_button);
        TextView popupDeleteText = deletePopupView.findViewById(R.id.delete_popup_message);
        /*
         * No button dismiss the popup window
         */
        popupDeleteNoButton.setOnClickListener(view -> {
            deletePopupWindow.dismiss();

        });
        popupDeleteYesButton.setOnClickListener(view -> {
            deletePopupWindow.dismiss();
            RecipeFragmentDirections.ActionRecipeFragmentToRecipeCollectionEditorFragment action =
                    RecipeFragmentDirections.actionRecipeFragmentToRecipeCollectionEditorFragment();
            action.setEditedRecipe(selectedRecipe);
            action.setEditedRecipeIndex(recipeIndex);
            action.setDeletedFlag(true);
            Navigation.findNavController(parentView).navigate(action);
        });
        /*
         * Set the text based on the recipe's title
         */
        popupDeleteText.setText(String.format("Delete  %s recipe",selectedRecipe.getTitle()));
        deleteRecipeButton.setOnClickListener(view -> {
            deletePopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        });
    }

    /**
     * Sets the onClick listener to take us back to the fragment where a list of recipes are being shown.
     */
    private void setBackToRecipeCollectionOnClick() {
        backToRecipeCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeFragmentDirections.ActionRecipeFragmentToRecipeCollectionEditorFragment action =
                        RecipeFragmentDirections.actionRecipeFragmentToRecipeCollectionEditorFragment();
                action.setEditedRecipe(selectedRecipe);
                action.setEditedRecipeIndex(recipeIndex);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    /**
     * Creates swipe interactions on recyclerView
     */
    private void setOnSwipeDeleteIngredients() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                selectedRecipe.getIngredients().deleteIngredient(pos);
                recipeIngredientsAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recipeIngredients);
    }

    /**
     * does nothing in this case
     * @param position
     */
    @Override
    public void OnItemClick(int position) {

    }
}