package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class IngredientValidatorTest {

    /**
     * Check an empty description and tests whether a single appropriate error string id
     * is in the error string buffer
     */
    @Test
    public void testNoDescriptionErrorStringIdInBuffer() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("");
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 1, errorStringIds.size());
        assertEquals( (int) R.string.ingredient_no_description, (int) errorStringIds.get(0) );
    }

    /**
     * Check an empty description and tests whether error string id was added and then test
     * whether the error string buffer in the validator has been cleared
     */
    @Test
    public void testErrorStringIdIsEmptied() {
        IngredientValidator validator = new IngredientValidator();
        validator.checkDescription("");
        ArrayList<Integer> errorStringIds = validator.getErrorStringIds();
        assertEquals( 1, errorStringIds.size() );
        errorStringIds = validator.getErrorStringIds();
        assertEquals( 0, errorStringIds.size() );
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
     * Tests checking amount for fifty. Fifty is acceptable.
     */
    @Test
    public void testFiftyAmount() {
        IngredientValidator validator = new IngredientValidator();
        int amount = 50;
        for (IngredientValidator.INGREDIENT_TYPE type : IngredientValidator.INGREDIENT_TYPE.values()) {
            assertTrue( validator.checkAmount(amount, type) );
        }
    }

    /*
    FIXME:
    I just tested obvious things, but there are other things I want to test but can't
    because the requirements is not complete enough.
    Some stuff are:
        testDescriptionOverCharacterLimit()     what's the character limit?
        testNullBestBeforeDate()                maybe best before date is not required?
        testZeroAmount()                        maybe that's allowed?

        These ones I am waiting on how we are going to store these values and retrieve them.
        testNullLocation
        testNoLocation
        testExistingLocation
        testNullUnit
        testNoUnit
        testExistingUnit
        testNullCategory
        testNoCategory
        testExistingCategory

     Furthermore, there could be tests for specific ingredient types.
     Like as an example SHOPPING ingredient may accept null best before date but STORED ingredient doesn't
     */
}