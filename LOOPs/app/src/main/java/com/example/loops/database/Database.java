package com.example.loops.database;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.IngredientStorage;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.ModelConstraints;
import com.example.loops.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The database class that contains very ease-of-use generic database access interface.
 */
public class Database {
    /**
     * Singleton pattern
     * https://refactoring.guru/design-patterns/singleton/java/example#example-2
     * Date Accessed: 2022-11-02
     */
    private static volatile Database instance;
    private FirebaseFirestore db;

    private CollectionReference mealPlanCollectionRef;

    public static final String DB_INGREDIENT = "IngredientStorage";
    public static final String DB_RECIPE = "RecipeCollection";
    public static final String DB_MEAL_PLAN = "MealPlanCollection";
    public static final String DB_SHOPPING_LIST = "ShoppingListCollection";
    private static Map<Object, String> collectionDict = new HashMap<>();


    /**
     * Connect to FireStore & initialize collection type mapping.
     */
    private void initDatabase() {
        db = FirebaseFirestore.getInstance();
        collectionDict.put(Ingredient.class, DB_INGREDIENT);
        collectionDict.put(Recipe.class, DB_RECIPE);
        collectionDict.put(MealPlan.class, DB_MEAL_PLAN);
        mealPlanCollectionRef = db.collection(DB_MEAL_PLAN);
    }

    /**
     * Constructor for offline mode.
     */
    private Database() {
        initDatabase();
    }

    /**
     * Getter for database singleton in offline mode.
     *
     * @return database singleton; can perform add, delete, update, & retrieve
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Update a database document based on its collection type & old/new document name.
     *
     * @param oldModel model before update
     * @param newModel model after update
     */
    public void updateDocument(ModelConstraints oldModel, ModelConstraints newModel) {
        Log.d("DATABASE_LOG", "DELETE DOCUMENT CALLED " + oldModel.getClass() + " " + newModel.getMapData().get("category"));
        deleteDocument(oldModel);
        addDocument(newModel);
    }

    /**
     * Delete a database document based on its collection type & document name.
     *
     * @param deleteModel model to delete
     */
    public void deleteDocument(ModelConstraints deleteModel) {
        Log.d("DATABASE_LOG", "DELETE DOCUMENT CALLED " + deleteModel.getClass() + " " + deleteModel.getDocumentName());
        db.collection(collectionDict.get(deleteModel.getClass()))
                .document(deleteModel.getDocumentName())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /**
     * Add a database document based on its collection type & document name.
     *
     * @param addModel model to add
     */
    public void addDocument(ModelConstraints addModel) {
        Log.d("DATABASE_LOG", "ADD DOCUMENT CALLED " + addModel.getClass() + " " + addModel.getMapData().get("category"));
        db.collection(collectionDict.get(addModel.getClass()))
                .document(addModel.getDocumentName())
                .set(addModel.getMapData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Retrieve all data from database to target collection based on its collection type.
     *
     * @param collectionName one of DB_INGREDIENT, DB_RECIPE, DB_MEAL_PLAN, DB_SHOPPING_LIST
     * @param collection     one of IngredientCollection, RecipeCollection, MealPlanCollection, ShoppingListCollection
     */
    public void retrieveCollection(String collectionName, Object collection) {
        Log.d("DATABASE_LOG", "RETRIEVE COLLECTION CALLED " + collectionName);
        if (!collectionName.equals(DB_MEAL_PLAN)) {
            db.collection(collectionName).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (!task.isSuccessful())
                                return;

                            if (collectionName.equals(DB_INGREDIENT)) {
                                Log.d("DATABASE_LOG", "INGREDIENT COLLECTION RETRIEVED");
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Ingredient databaseIngredient = new Ingredient(
                                            documentSnapshot.getString("description"),
                                            documentSnapshot.getString("bestBeforeDate"),
                                            documentSnapshot.getString("location"),
                                            Double.parseDouble(documentSnapshot.getString("amount")),
                                            documentSnapshot.getString("unit"),
                                            documentSnapshot.getString("category")
                                    );

                                    databaseIngredient.setPending(documentSnapshot.getBoolean("pending"));

                                    Log.d("DATABASE_LOG", "INGREDIENT GOTTEN " + databaseIngredient.getDescription());
                                    ((IngredientStorage) collection).addIngredientLocal(databaseIngredient);
                                }
                            }

                            if (collectionName.equals(DB_RECIPE)) {
                                Log.d("DATABASE_LOG", "RECIPE COLLECTION RETRIEVED");
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    List<Map<String, Object>> ingredientsList =
                                            (List<Map<String, Object>>) documentSnapshot.get("ingredients");

                                    Recipe databaseRecipe = new Recipe(
                                            documentSnapshot.getString("title"),
                                            documentSnapshot.getLong("durationHour"),
                                            documentSnapshot.getLong("durationMinute"),
                                            Math.toIntExact(documentSnapshot.getLong("numServing")),
                                            documentSnapshot.getString("category"),
                                            documentSnapshot.getString("photoBase64"),
                                            constructIngredientCollection(ingredientsList),
                                            documentSnapshot.getString("comments")
                                    );
//                                    Map<String, Object> containIngredients =
//                                            (HashMap<String, Object>) documentSnapshot.get("ingredients");
//                                    for (String ingHash : containIngredients.keySet()) {
//                                        Map<String, Object> ingInfo =
//                                                (HashMap<String, Object>) containIngredients.get(ingHash);
//                                        Ingredient containsIngredient = new Ingredient(
//                                                (String) ingInfo.get("description"),
//                                                (String) ingInfo.get("bestBeforeDate"),
//                                                (String) ingInfo.get("location"),
//                                                Double.parseDouble((String) ingInfo.get("amount")),
//                                                (String) ingInfo.get("unit"),
//                                                (String) ingInfo.get("category")
//                                        );
//                                    }

                                    Log.d("DATABASE_LOG", "RECIPE GOTTEN " + databaseRecipe.getTitle());
                                    ((RecipeCollection) collection).addRecipeLocally(databaseRecipe);
                                }
                            }

                            if (collectionName.equals(DB_MEAL_PLAN)) {

                            }

//                        if (collectionName == DB_SHOPPING_LIST) {
//
//                        }
                        }
                    });
        }
        else if (collectionName.equals(DB_MEAL_PLAN)) {
            //CollectionReference mealPlanCollectionRef = db.collection(DB_MEAL_PLAN);
            MealPlanCollection mealPlans = (MealPlanCollection) collection;
            for (String mealPlanName : mealPlans.getMealPlanNames()) {
                readMealPlan(mealPlanName, mealPlans);
            }
        }
    }

    private void readMealPlan(String name, MealPlanCollection mealPlans) {
        mealPlanCollectionRef.document(name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> ingredients =
                                (List<Map<String, Object>>) document.get("ingredients");
                        List<Map<String, Object>> recipes =
                                (List<Map<String, Object>>) document.get("recipes");
                        RecipeCollection recipeCollection = new RecipeCollection();
                        for (Map<String, Object> recipe : recipes) {
                            recipeCollection.addRecipe(new Recipe(
                                    (String) recipe.get("title"),
                                    (Long) recipe.get("durationHour"),
                                    (Long) recipe.get("durationMinute"),
                                    Math.toIntExact((Long) recipe.get("numServing")),
                                    (String) recipe.get("category"),
                                    (String) recipe.get("photoBase64"),
                                    constructIngredientCollection((List<Map<String, Object>>) recipe.get("ingredients")),
                                    (String) recipe.get("comments")));
                        }
                        Log.d("DATABASE_LOG", "MEAL PLAN " + name);
                        mealPlans.addMealPlanLocally(
                                name,
                                constructIngredientCollection(ingredients),
                                recipeCollection);
                    }
                }

            }
        });
    }

    private IngredientCollection constructIngredientCollection(List<Map<String, Object>> ingredients) {
        IngredientCollection ingredientCollection = new IngredientCollection();
        if (ingredients == null)
          return ingredientCollection;
        for (Map<String, Object> ingredient : ingredients) {
            ingredientCollection
                    .addIngredient(new Ingredient(
                            (String) ingredient.get("description"),
                            Double.parseDouble((String) ingredient.get("amount")),
                            (String) ingredient.get("unit"),
                            (String) ingredient.get("category")));
        }

        return ingredientCollection;
    }
}
