package com.example.loops.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Model class. It is abstraction of ingredient and it implements Serializable which make it
 * transmissible between fragment
 * Ingredient class that encapsulate the data structure & methods of each ingredient.
 */
public class Ingredient implements Serializable {
    /**
     * An ingredient includes description, best before date, location, amount, unit & category.
     */
    private String description;
    private LocalDate bestBeforeDate;
    private String storeLocation;
    private float amount;
    private String unit;
    private String category;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor that pass all ingredient elements.
     * @param description
     * @param bestBeforeDate passing in Date
     * @param storeLocation
     * @param amount
     * @param unit
     * @param category
     */
    public Ingredient(String description, LocalDate bestBeforeDate, String storeLocation, float amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.storeLocation = storeLocation;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Constructor that pass all ingredient elements.
     * @param description
     * @param bestBeforeDate passing in String with mm/dd/yyyy format
     * @param storeLocation
     * @param amount
     * @param unit
     * @param category
     */
    public Ingredient(String description, String bestBeforeDate, String storeLocation, float amount, String unit, String category) {
        this.description = description;
        setBestBeforeDate(bestBeforeDate);
        this.storeLocation = storeLocation;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Rewrite equals method to make it comparable by element info.
     * @param o ingredient object to compare
     * @return whether is equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ingredient))
            return false;
        Ingredient toCompare = (Ingredient) o;
        return toCompare.getDescription().equals(getDescription())
                //&& toCompare.getAmount() == getAmount()
                && toCompare.getCategory().equals(getCategory())
                && toCompare.getBestBeforeDateString().equals(getBestBeforeDateString())
                && toCompare.getStoreLocation().equals(getStoreLocation())
                && toCompare.getUnit().equals(getUnit());
    }


    // The following are all getters & setters.


    /**
     * get the ingredient's description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the ingredient's description to a wanted value
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the ingredient's best before date as Date
     * @return Date object
     */
    public LocalDate getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * get the ingredient's best before date as string
     * @return Date string
     */
    public String getBestBeforeDateString() {
        return dateFormatter.format(bestBeforeDate);
    }

    /**
     * set the ingredient's best before date to another Date
     * @param bestBeforeDate new date
     */
    public void setBestBeforeDate(LocalDate bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * set the ingredient's best before date to another Date with formatted string
     * @param bestBeforeDate new date
     */
    public void setBestBeforeDate(String bestBeforeDate){
        try {
            LocalDate date = LocalDate.parse(bestBeforeDate, dateFormatter);
            this.bestBeforeDate = date;
        } catch (DateTimeException e) {
            this.bestBeforeDate = LocalDate.now();
        }
    }

    /**
     * get the ingredient's store location
     * @return the store location
     */
    public String getStoreLocation() {
        return storeLocation;
    }

    /**
     * set teh ingredient's store location
     * @param storeLocation new store location
     */
    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    /**
     * get the amount of the ingredient
     * @return the amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * set the ingredient's amount to a new value
     * @param amount new amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * get the ingredient's unit
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * give the ingredient another unit
     * @param unit new unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * get the ingredient's category
     * @return the category of the ingredient
     */
    public String getCategory() {
        return category;
    }

    /**
     * set the ingredient's category
     * @param category new category of this ingredient
     */
    public void setCategory(String category) {
        this.category = category;
    }

}
