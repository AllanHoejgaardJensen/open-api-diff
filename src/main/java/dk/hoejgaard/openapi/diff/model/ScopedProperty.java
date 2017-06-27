package dk.hoejgaard.openapi.diff.model;

import io.swagger.models.properties.Property;

/**
 * container for Scope and Property
 */
public class ScopedProperty {

    private final String el;
    private final Property property;

    /**
     * @param scope the scope for the property
     * @param property the property itself
     */
    public ScopedProperty(String scope, Property property) {
        this.el = scope;
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public String getEl() {
        return el;
    }
}
