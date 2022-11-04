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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Main activity of the program, handle navigation of all fragments.
 * All database management parts were implemented inside the main activity.
 */
public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private IngredientCollection allIngredients = new IngredientCollection();
    private RecipeCollection allRecipes = new RecipeCollection();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public IngredientCollection getIngredientStorage() {
        return allIngredients;
    }

    public RecipeCollection getAllRecipes() {
        return allRecipes;
    }

    public void setAllRecipes(RecipeCollection recipeCollection) {
        allRecipes = recipeCollection;
    }

    public void retrieveRecipeFromDatabase() {
        db.collection("RecipeStorage").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Recipe databaseRecipe = new Recipe(
                                    documentSnapshot.getString("title"),
                                        Integer.parseInt(documentSnapshot.getString("numServing"))
                                );

                            }
                        }
                    }
                });
    }

    /**
     * Load all database ingredients into the ingredient collection.
     */
    public void retrieveIngredientFromDatabase() {
        db.collection("IngredientStorage").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Ingredient databaseIngredient = new Ingredient(
                                        documentSnapshot.getString("description"),
                                        documentSnapshot.getString("bestBeforeDate"),
                                        documentSnapshot.getString("location"),
                                        documentSnapshot.getLong("amount"),
                                        documentSnapshot.getString("unit"),
                                        documentSnapshot.getString("category")
                                );
                                allIngredients.addIngredient(databaseIngredient);
                            }
                        }
                    }
                });
    }

    /**
     * Delete an ingredient from the database storage.
     * @param ingInd index of ingredient to delete
     */
    public void deleteIngredientFromDatabase(int ingInd) {
        db.collection("IngredientStorage").document(Integer.toString(ingInd))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Ingredient deleted.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Ingredient delete failed!");
                    }
                });
    }

    /**
     * Update the ingredient collection to database.
     * @param updatedIngredient updated ingredient collection
     */
    public void updateIngredientFromDatabase(IngredientCollection updatedIngredient) {
        if (updatedIngredient == null) {
            return;
        }
        allIngredients = updatedIngredient;

        int i = 0;
        for (Ingredient ing : allIngredients.getIngredients()) {
            Log.e(TAG, ing.getDescription());
            Map<String, Object> ingredientRecord = new HashMap<>();
            ingredientRecord.put("description", ing.getDescription());
            ingredientRecord.put("bestBeforeDate", ing.getBestBeforeDateString());
            ingredientRecord.put("location", ing.getStoreLocation());
            ingredientRecord.put("amount", ing.getAmount());
            ingredientRecord.put("unit", ing.getUnit());
            ingredientRecord.put("category", ing.getCategory());

            db.collection("IngredientStorage").document(Integer.toString(i))
                    .set(ingredientRecord)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Ingredient updated.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Ingredient update failed!");
                        }
                    });
            i++;
        }
        deleteIngredientFromDatabase(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveIngredientFromDatabase();

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
                    getSupportActionBar().setTitle(R.string.mealPlans);
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
}