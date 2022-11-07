package com.example.loops.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.loops.modelCollections.IngredientStorage;
import com.example.loops.models.Ingredient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is deprecated.
 *      If we add more functions like interacting with ingredients, interacting with recipes, and so on
 *      this database class will have a lot of methods. It may be a good idea to break this database
 *      class into smaller classes like IngredientQueryManager, RecipeQueryManager, UserManager, etc
 *      just to give quick examples. This class is proof of concept more than actual code.
 */
public class Database implements RemoteIngredientStorageManager{
    /**
     * Singleton pattern
     * https://refactoring.guru/design-patterns/singleton/java/example#example-2
     * Date Accessed: 2022-11-02
     */
    public static final String MAIN_USER_ID = "MainUser";
    public static final String TEST_USER_ID = "TestUser";
    public static final Object lock = new Object();
    private static volatile Database instance;
    private static volatile String currentUserId = "";
    private DocumentReference userData;
    private FirebaseFirestore db;
    private Map<String, Object> ingredientRecord;

    /**
     * Called when database query is successful
     */
    public interface onSuccessListener {
        // FIXME: instead of returning a Map, it would be better to create a DatabaseResult class
        // the DatabaseResult class can then handle many different type of results (strings, documents, ints, etc)
        void onSuccess(Map<String, ?> result);
    }

    /**
     * Called when database query has failed
     */
    public interface onFailureListener {
        /*
        Idea is when you call a database query, you supply a lambda function
            (exception) -> {
                try {
                    throw exception
                }
                catch (ExceptionType e ){
                    ... error handling ...
                }
            }

         It would be a good idea to make our own custom exceptions like NonExistentDataError,
         ConnectionError, etc to give few as examples.
         */
        void onFailure(@NonNull Exception e);
    }


    private Database(String username) {
        db = FirebaseFirestore.getInstance();
        try {
            userData = db
                    .collection("Users")
                    .document(username);
        }
        catch (Exception e){
            // TODO: error handling
            throw e;
        }
    }

    private Database() {
        db = FirebaseFirestore.getInstance();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /*
    The idea is whenever you need to access the data, just call Database.getInstance(Database.TEST_USER_ID)
     */
    public static Database getInstance(String username) {
        Database result = instance;
        if (result != null && currentUserId.equals(username)) {
            return result;
        }
        synchronized(Database.class) {
            if (instance == null || !currentUserId.equals(username)) {
                instance = new Database(username);
                currentUserId = username;
            }
            return instance;
        }
    }

    /*
    Then you can call this in a fragment like this:
    Database db = Database.getInstance(Database.TEST_USER_ID);
    db.getIngredientLocations(
            (result) -> {
                Log.e("String", result.get("ingredientLocations").toString());
            },
            (exception) -> {
                try {
                    throw exception;
                }
                catch ...

            }
    );

    ALSO it may be a good idea to look at threading in java
    Thread t = new Thread(new Runnable() {
        @Override
           public void run() {
                db.getIngredientLocations(....);
           }
       });
     t.start();
     t.join();      // wait for thread to finish
     */
    public void getIngredientLocations(onSuccessListener onSuccess, onFailureListener onFailure) {
        userData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        onSuccess.onSuccess(doc.getData());
                    }
                    else {
                        onFailure.onFailure(new Exception("User document does not exist"));
                    }
                }
                else {
                    onFailure.onFailure(new Exception("Failed to retrieve data"));
                }
            }
        });
    }

    @Override
    public void getIngredientStorage(IngredientStorage ingredientStorage) {
        //ArrayList<Ingredient> ingredientStorage = new ArrayList<>();
        //return ingredientStorage;
        Runnable myRunnable = () -> {
            System.out.println("in runnable");
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
                                            documentSnapshot.getLong("amount"), //why can this return a float?
                                            documentSnapshot.getString("unit"),
                                            documentSnapshot.getString("category")
                                    );
                                    ingredientStorage.addIngredientLocal(databaseIngredient);
                                }

                            }
                            ingredientStorage.done = true;
                            System.out.println("before unlock");
                            synchronized (instance) {
                                instance.notify();
                                System.out.println("unblock");
                            }
                        }
                    });
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
        System.out.println("create thread");
        /*while (ingredientStorage.done == false) {
            //wait until read all data
        }*/
    }

    @Override
    public void addIngredientToStorage(Ingredient ingredient) {
        String key =
                ingredient.getDescription() +
                ingredient.getBestBeforeDateString() +
                ingredient.getStoreLocation() +
                ingredient.getUnit() +
                ingredient.getCategory();
        ingredientRecord = new HashMap<>();
        ingredientRecord.put("description", ingredient.getDescription());
        ingredientRecord.put("bestBeforeDate", ingredient.getBestBeforeDateString());
        ingredientRecord.put("location", ingredient.getStoreLocation());
        ingredientRecord.put("amount", ingredient.getAmount());
        ingredientRecord.put("unit", ingredient.getUnit());
        ingredientRecord.put("category", ingredient.getCategory());
        db.collection("IngredientStorage").document(key)
                .set(ingredientRecord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void removeIngredientFromStorage(Ingredient ingredient) {
        String key =
                ingredient.getDescription() +
                        ingredient.getBestBeforeDateString() +
                        ingredient.getStoreLocation() +
                        ingredient.getUnit() +
                        ingredient.getCategory();
        ingredientRecord = new HashMap<>();
        ingredientRecord.put("description", ingredient.getDescription());
        ingredientRecord.put("bestBeforeDate", ingredient.getBestBeforeDateString());
        ingredientRecord.put("location", ingredient.getStoreLocation());
        ingredientRecord.put("amount", ingredient.getAmount());
        ingredientRecord.put("unit", ingredient.getUnit());
        ingredientRecord.put("category", ingredient.getCategory());
        db.collection("IngredientStorage").document(key).delete();
    }

    @Override
    public void updateIngredientInStorage(Ingredient oldIngredient, Ingredient newIngredient) {
        removeIngredientFromStorage(oldIngredient);
        addIngredientToStorage(newIngredient);
    }
}
