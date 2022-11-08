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

    boolean equals(Object o);
    int hashCode();
}
