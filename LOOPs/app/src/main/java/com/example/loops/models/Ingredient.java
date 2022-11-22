package com.example.loops.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Model class. It is abstraction of ingredient and it implements Serializable which make it
 * transmissible between fragment
 * Ingredient class that encapsulate the data structure & methods of each ingredient.
 */
public class Ingredient implements Serializable, ModelConstraints {
    /**
     * An ingredient includes description, best before date, location, amount, unit & category.
     */
    private String description;
    private LocalDate bestBeforeDate;
    private String storeLocation;
    private double amount;
    private String unit;
    private String category;
    private boolean pending = false;
    private String dateTimeFormat = "yyyy-MM-dd";

    /**
     * Constructor that pass all ingredient elements.
     * @param description the name of ingredient
     * @param bestBeforeDate the date when out-dated; passing in Date
     * @param storeLocation the location to store; should be one of user-defined
     * @param amount the amount to store; can be decimal number
     * @param unit the unit for the amount; should be one of user-defined
     * @param category the category of ingredient; should be user-defined
     */
    public Ingredient(String description, LocalDate bestBeforeDate, String storeLocation, double amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.storeLocation = storeLocation;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Constructor that pass all ingredient elements.
     * @param description the name of ingredient
     * @param bestBeforeDate the date when out-dated; passing in String with mm/dd/yyyy format
     * @param storeLocation the location to store; should be one of user-defined
     * @param amount the amount to store; can be decimal number
     * @param unit the unit for the amount; should be one of user-defined
     * @param category the category of ingredient; should be user-defined
     */
    public Ingredient(String description, String bestBeforeDate, String storeLocation, double amount, String unit, String category) {
        this.description = description;
        setBestBeforeDate(bestBeforeDate);
        this.storeLocation = storeLocation;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Constructor for partial ingredients; only being used in recipe & shopping list
     * @param description the name of ingredient
     * @param amount the amount to store; can be decimal number
     * @param unit the unit for the amount; should be one of user-defined
     * @param category the category of ingredient; should be user-defined
     */
    public Ingredient(String description, double amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = LocalDate.MIN;
        this.storeLocation = " ";
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * copy constructor
     * @param that the ingredient want to copy
     */
    public Ingredient(Ingredient that) {
        this(that.getDescription(), that.getBestBeforeDateString(), that.getStoreLocation(),
                that.getAmount(), that.getUnit(), that.getCategory());
        this.setPending(that.pending);
    }

    /**
     * Rewrite equals method to make it comparable by element info.
     * Two ingredients must have all attributes (except amount) being identical to be considered as equal.
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
                && toCompare.getUnit().equals(getUnit())
                && toCompare.getPending() == getPending();
    }

    /**
     * The hash of ingredient as the document in FireStore database.
     * Hash is assigned by all attributes except amount.
     * @return the hash code for the ingredient
     */
    @Override
    public String getDocumentName() {
        String documentName = Integer.toString((getDescription()
                + getBestBeforeDateString()
                + getStoreLocation()
                + getUnit()
                + getCategory()).hashCode());
        if (getPending()) {
            documentName = "$~" + documentName;
        }
        return documentName;
    }

    /**
     * The mapped result for ingredient; the formatted data that can directly send to database.
     * @return the formatted map data for database storage
     */
    public Map<String, Object> getMapData() {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("description", getDescription());
        mapData.put("bestBeforeDate", getBestBeforeDateString());
        mapData.put("location", getStoreLocation());
        mapData.put("amount", Double.toString(getAmount()));
        mapData.put("unit", getUnit());
        mapData.put("category", getCategory());
        mapData.put("pending", getPending());
        return mapData;
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
        return DateTimeFormatter.ofPattern(dateTimeFormat).format(bestBeforeDate);
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
            LocalDate date = LocalDate.parse(bestBeforeDate, DateTimeFormatter.ofPattern(dateTimeFormat));
            this.bestBeforeDate = date;
        } catch (DateTimeException e) {
            this.bestBeforeDate = LocalDate.MIN;
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
    public double getAmount() {
        return amount;
    }

    /**
     * set the ingredient's amount to a new value
     * @param amount new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * add amount to the ingredient
     * @param amount how much to add
     */
    public void addAmount(double amount) {
        this.amount += amount;
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

    public boolean getPending() { return pending; }
    public void setPending(boolean pending) { this.pending = pending; }
}
