package com.example.loops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import android.graphics.Color;
import android.graphics.Bitmap;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Test Cases for the Recipe class methods
 */
public class RecipeTest extends TestCase {
    Recipe testRecipe;
    IngredientCollection ingredients;
    Ingredient carrot;
    Bitmap bm1;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    void initialize() {
        ingredients = new IngredientCollection();
        carrot = new Ingredient(
                "Carrot",
                "10/24/22",
                "Fridge",
                10,
                "units",
                "snack");
        ingredients.addIngredient(carrot);

        bm1 = Bitmap.createBitmap(100, 200, Bitmap.Config.ARGB_8888);
        testRecipe = new Recipe(
                "Baked carrots",
                2,
                15,
                3,
                "Vegetables",
                "bm1",
                ingredients,
                "Bake in oven at 350F"
        );
    }


    @Test
    void testGetters() {
        initialize();
        assertEquals("Baked carrots", testRecipe.getTitle());

        Duration x = Duration.ofHours(2);
        Duration y = Duration.ofMinutes(15);
        x.plus(y);
        assertEquals(x, testRecipe.getPrepTime());

        assertEquals(3, testRecipe.getNumServing());

        assertEquals("Vegetables", testRecipe.getCategory());


        assertEquals("bm1", testRecipe.getPhotoBase64());

        assertEquals(ingredients, testRecipe.getIngredients());

        assertEquals("Bake in oven at 350F",testRecipe.getComments());
    }

    @Test
    void testSetters() {
        testRecipe.setTitle("apple");
        assertEquals("apple", testRecipe.getTitle());
        Duration x = Duration.ofHours(3);
        Duration y = Duration.ofMinutes(5);
        x.plus(y);
        testRecipe.setPrepTime(x);
        assertEquals(x, testRecipe.getPrepTime());

        testRecipe.setNumServing(5);
        assertEquals(5, testRecipe.getNumServing());

        testRecipe.setCategory("fruit");
        assertEquals("fruit", testRecipe.getCategory());

        Bitmap bm2 = Bitmap.createBitmap(350, 200, Bitmap.Config.RGB_565);
        testRecipe.setPhoto(bm2);
        assertEquals(bm2, testRecipe.getPhoto());

        testRecipe.setComments("good apples");
        assertEquals("good apples", testRecipe.getComments());

        ingredients = new IngredientCollection();
        Ingredient meat;
        meat = new Ingredient(
                "beef",
                "9/20/22",
                "Fridge",
                8,
                "units",
                "meat");
        ingredients.addIngredient(meat);
        testRecipe.setIngredients(ingredients);
        assertEquals(ingredients, testRecipe.getIngredients());
    }


}