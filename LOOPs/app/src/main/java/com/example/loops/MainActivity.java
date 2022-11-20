package com.example.loops;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.loops.database.Database;
import com.example.loops.ingredientFragments.IngredientCollectionEditorFragment;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.IngredientStorage;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Main activity of the program, handle navigation of all fragments.
 * All database management parts were implemented inside the main activity.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Main activity attributes.
     * appBarConfiguration - header display
     * allIngredients - ingredient storage from database
     * allRecipes - recipe storage from database
     */
    private AppBarConfiguration appBarConfiguration;
    private IngredientStorage allIngredients = new IngredientStorage(Database.getInstance());
    private RecipeCollection allRecipes = new RecipeCollection(Database.getInstance());
    private IngredientCollection shoppingList; //default is null
    private MealPlanCollection mealPlans = new MealPlanCollection(Database.getInstance());

    public IngredientCollection getIngredientStorage() {
        return allIngredients;
    }
    public RecipeCollection getAllRecipes() {
        return allRecipes;
    }
    public IngredientCollection getShoppingList() {
        return shoppingList;
    }
    public MealPlanCollection getMealPlans() {
        return mealPlans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationView navView = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(navView, navController);

        DrawerLayout sideMenuLayout = findViewById(R.id.side_menu_layout);
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(sideMenuLayout)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.mealPlanHomePageFragment) {
                    getSupportActionBar().setTitle(R.string.app_name);
                }
                else if (navDestination.getId() == R.id.ingredientCollectionEditorFragment) {
                    getSupportActionBar().setTitle(R.string.ingredientCollection);
                }
                else if (navDestination.getId() == R.id.recipeCollectionEditorFragment) {
                    getSupportActionBar().setTitle(R.string.recipeCollection);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}