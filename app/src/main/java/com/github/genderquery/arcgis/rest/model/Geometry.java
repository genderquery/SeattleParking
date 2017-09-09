package com.github.genderquery.arcgis.rest.model;

public abstract class Geometry {

    private final GeometryType geometryType;

    protected Geometry(GeometryType geometryType) {
        this.geometryType = geometryType;
    }

    public GeometryType getGeometryType() {
        return geometryType;
    }
}
