package com.example.loops;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * Ingredient class that encapsulate the data structure & methods of each ingredient.
 */
public class Ingredient implements Serializable {
    /**
     * An ingredient includes description, best before date, location, amount, unit & category.
     */
    private String description;
    private Date bestBeforeDate;
    private String storeLocation;
    private float amount;
    private String unit;
    private String category;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Constructor that pass all ingredient elements.
     * @param description
     * @param bestBeforeDate passing in Date
     * @param storeLocation
     * @param amount
     * @param unit
     * @param category
     */
    public Ingredient(String description, Date bestBeforeDate, String storeLocation, float amount, String unit, String category) {
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
                && toCompare.getAmount() == getAmount()
                && toCompare.getCategory().equals(getCategory())
                && toCompare.getBestBeforeDateString().equals(getBestBeforeDateString())
                && toCompare.getStoreLocation().equals(getStoreLocation())
                && toCompare.getUnit().equals(getUnit());
    }

    /**
     * The following are all getters & setters.
     */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBestBeforeDate() {
        return bestBeforeDate;
    }

    public String getBestBeforeDateString() {
        return dateFormatter.format(bestBeforeDate);
    }

    public void setBestBeforeDate(Date bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public void setBestBeforeDate(String bestBeforeDate){
        try {
            Date date = dateFormatter.parse(bestBeforeDate);
            this.bestBeforeDate = date;
        } catch (ParseException e) {
            this.bestBeforeDate = new Date(0);
        }
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
