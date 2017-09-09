package com.github.genderquery.arcgis.rest.model;

public class Point extends Geometry {

    public double x;
    public double y;

    public Point(double x, double y) {
        super(GeometryType.POINT);
        this.x = x;
        this.y = y;
    }
}
