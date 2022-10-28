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
}
