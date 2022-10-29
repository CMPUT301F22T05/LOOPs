package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IngredientCollectionTest {
    @Test
    void testAdd() {
        IngredientCollection ingredientCollection = new IngredientCollection();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormatter.parse("2022-04-06");
        } catch (ParseException e) {

        }
        Ingredient ingredient = new Ingredient("",new Date(),"",1,"","");
        assertEquals(0, ingredientCollection.getIngredients().size());
        ingredientCollection.addIngredient(ingredient);
        assertEquals(1, ingredientCollection.getIngredients().size());
    }

    @Test
    void testSortByDescription() {
        IngredientCollection ingredientCollection = new IngredientCollection();
        Ingredient ingredient1 = new Ingredient("a", new Date(), "", 1, "", "");
        Ingredient ingredient2 = new Ingredient("b", new Date(), "", 1, "", "");
        ingredientCollection.addIngredient(ingredient1);
        ingredientCollection.addIngredient(ingredient2);
        ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_DESCENDING);
        assertEquals("b", ingredientCollection.getIngredients().get(0).getDescription());
        assertEquals("a", ingredientCollection.getIngredients().get(1).getDescription());
        ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        assertEquals("a", ingredientCollection.getIngredients().get(0).getDescription());
        assertEquals("b", ingredientCollection.getIngredients().get(1).getDescription());
    }
}
