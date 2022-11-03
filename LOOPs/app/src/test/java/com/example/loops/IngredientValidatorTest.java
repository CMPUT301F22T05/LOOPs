package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Test cases for IngredientValidator class
 */
public class IngredientValidatorTest {
    /**
     * Check an empty description and tests whether a single appropriate error string id
     * is in the error string buffer
     */
    @Test
    public void testNoDescriptionErrorStringIdInBuffer() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("", IngredientValidator.INGREDIENT_TYPE.STORED);
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 1, errorStringIds.size());
        assertEquals( (int) R.string.ingredient_no_description, (int) errorStringIds.get(0) );
    }

    /**
     * Check an empty description and tests whether error string id was added and then test
     * whether the error string buffer in the validator has been cleared
     */
    @Test
    public void testErrorStringIdBufferIsEmptied() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("", IngredientValidator.INGREDIENT_TYPE.STORED);
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 1, errorStringIds.size() );
        errorStringIds = validator.getErrorStringIds();
        assertEquals( 0, errorStringIds.size() );
    }

    /**
     * Checks whether duplicate error strings ids can be in the buffer
     */
    @Test
    public void testNoDuplicateIdsInErrorStringIdBuffer() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("", IngredientValidator.INGREDIENT_TYPE.STORED);
        validator.checkDescription("", IngredientValidator.INGREDIENT_TYPE.STORED);
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 1, errorStringIds.size() );
    }

    /**
     * Checks whether multiple error strings ids can be in the buffer
     */
    @Test
    public void testMultipleErrorStringIdsInBuffer() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("", IngredientValidator.INGREDIENT_TYPE.STORED);
        validator.checkLocation("", IngredientValidator.INGREDIENT_TYPE.STORED);
        validator.checkBestBeforeDate(null, IngredientValidator.INGREDIENT_TYPE.STORED);
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 3, errorStringIds.size() );
    }

    /**
     * Tests checking null ingredient for all ingredient types. It shouldn't be null
     */
    @Test
    public void testNullIngredient() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkIngredient(null, type) );
        }
    }

    /**
     * Tests checking valid ingredients for all ingredient types
     */
    @Test
    public void testValidIngredient() {
        IngredientValidator validator = new IngredientValidator();
        Ingredient ingredient = new Ingredient("Flour", new Date(), "Pantry", 69, "g", "baking");
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkIngredient(ingredient, type) );
        }
    }

    /**
     * Tests checking for empty description for all ingredient types. It shouldn't be empty
     */
    @Test
    public void testEmptyDescription() {
        IngredientValidator validator = new IngredientValidator();
        String description = "";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkDescription(description, type) );
        }
    }

    /**
     * Tests checking for null description for all ingredient types. It shouldn't be null
     */
    @Test
    public void testNullDescription() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkDescription(null, type) );
        }
    }

    /**
     * Tests checking for a valid description for all ingredient types
     */
    @Test
    public void testNormalDescription() {
        IngredientValidator validator = new IngredientValidator();
        String description = "100% fresh tuna can";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkDescription(description, type) );
        }
    }

    /**
     * Tests checking for a best before date that is on today for all ingredient types
     * Best before date on today is acceptable
     */
    @Test
    public void testBestBeforeDateOnToday() {
        IngredientValidator validator = new IngredientValidator();
        Date today = new Date();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkBestBeforeDate(today, type) );
        }
    }

    /**
     * Tests checking for a best before date that is after today for all ingredient types
     * Best before date after today are acceptable
     */
    @Test
    public void testBestBeforeDateAfterToday() {
        IngredientValidator validator = new IngredientValidator();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date afterToday = cal.getTime();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkBestBeforeDate(afterToday, type) );
        }
    }

    /**
     * Tests checking for a best before date that is before today for all ingredient types
     * Best before date before today are acceptable
     */
    @Test
    public void testBestBeforeDateBeforeToday() {
        IngredientValidator validator = new IngredientValidator();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date beforeToday = cal.getTime();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkBestBeforeDate(beforeToday, type) );
        }
    }

    /**
     * Tests for null best before date
     */
    @Test
    public void testNullBestBeforeDate() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkBestBeforeDate(null, type) );
        }
    }

    /**
     * Tests checking amount of a negative value. Negative amount is not acceptable
     */
    @Test
    public void testNegativeAmount() {
        IngredientValidator validator = new IngredientValidator();
        int amount = -1;
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkAmount(amount, type) );
        }
    }

    /**
     * Tests checking amount of zero. Zero is acceptable
     */
    @Test void testZeroAmount() {
        IngredientValidator validator = new IngredientValidator();
        int amount = 0;
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkAmount(amount, type) );
        }
    }

    /**
     * Tests checking amount of a positive value. Positive amount is acceptable.
     */
    @Test
    public void testPositiveAmount() {
        IngredientValidator validator = new IngredientValidator();
        int amount = 50;
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkAmount(amount, type) );
        }
    }

    /**
     * Tests checking for a location string that is in valid form
     */
    @Test
    public void testValidLocation() {
        IngredientValidator validator = new IngredientValidator();
        String location = "Pantry";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkLocation(location, type) );
        }
    }

    /**
     * Tests checking for no location.
     */
    @Test
    public void testNoLocation() {
        IngredientValidator validator = new IngredientValidator();
        String location = "";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkLocation(location, type) );
        }
    }

    /**
     * Tests checking for a null location
     */
    @Test
    public void testNullLocation() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkLocation(null, type) );
        }
    }

    /**
     * Tests checking for a unit that is valid form
     */
    @Test
    public void testValidUnit() {
        IngredientValidator validator = new IngredientValidator();
        String unit = "kg";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkUnit(unit, type) );
        }
    }

    /**
     * Tests checking for an empty unit.
     */
    @Test
    public void testNoUnit() {
        IngredientValidator validator = new IngredientValidator();
        String unit = "";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkUnit(unit, type) );
        }
    }

    /**
     * Tests checking for a null unit
     */
    @Test
    public void testNullUnit() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkUnit(null, type) );
        }
    }

    /**
     * Tests checking for a category that is valid form
     */
    @Test
    public void testValidCategory() {
        IngredientValidator validator = new IngredientValidator();
        String category = "Non-Perishable";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkCategory(category, type) );
        }
    }

    /**
     * Tests checking for an empty category.
     */
    @Test
    public void testNoCategory() {
        IngredientValidator validator = new IngredientValidator();
        String category = "";
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkCategory(category, type) );
        }
    }

    /**
     * Tests checking for a null category
     */
    @Test
    public void testNullCategory() {
        IngredientValidator validator = new IngredientValidator();
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertFalse( validator.checkCategory(null, type) );
        }
    }
}