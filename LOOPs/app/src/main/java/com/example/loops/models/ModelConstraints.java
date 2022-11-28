package com.example.loops.models;

import java.util.Map;

/**
 * All signatures in this interface must be implemented by model classes
 * to make all model class valid.
 */
public interface ModelConstraints {
    /**
     * The formatted data that can directly send to database.
     * @return the formatted map data for database storage
     */
    Map<String, Object> getMapData();

    /**
     * The document name that will be used to store the object in Firestore.
     * It should be unique
     * @return
     */
    String getDocumentName();
}
