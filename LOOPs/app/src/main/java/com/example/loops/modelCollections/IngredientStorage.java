package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.database.RemoteIngredientStorageManager;
import com.example.loops.models.Ingredient;

public class IngredientStorage extends IngredientCollection{
    private RemoteIngredientStorageManager remoteIngredientStorageManager;
    public boolean done = false;

    public IngredientStorage(RemoteIngredientStorageManager manager) {
        super();
        remoteIngredientStorageManager = manager;
        getIngredientStorageFromDataBase();
    }

    private void getIngredientStorageFromDataBase() {
        remoteIngredientStorageManager.getIngredientStorage(this);
/*        synchronized (remoteIngredientStorageManager) {
            try {
                System.out.println("block");
                remoteIngredientStorageManager.wait();
            } catch (InterruptedException e) {
                System.out.println("error");
            }
        }*/
        done = false;
    }

    public void addIngredientLocal(Ingredient ingredient) {
        super.addIngredient(ingredient);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        remoteIngredientStorageManager.addIngredientToStorage(ingredient);
        super.addIngredient(ingredient);
    }

    @Override
    public boolean deleteIngredient(int index) {
        remoteIngredientStorageManager.removeIngredientFromStorage(ingredients.get(index));
        return super.deleteIngredient(index);
    }

    @Override
    public boolean updateIngredient(int index, Ingredient ingredient) {
        remoteIngredientStorageManager.updateIngredientInStorage(ingredients.get(index), ingredient);
        return super.updateIngredient(index, ingredient);
    }
}
