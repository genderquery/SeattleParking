package com.github.genderquery.arcgis.rest.model;

public class Envelope extends Geometry {

    public double xmin;
    public double ymin;
    public double xmax;
    public double ymax;

    public Envelope(Point min, Point max) {
        this(min.x, min.y, max.x, max.y);
    }

    public Envelope(double xmin, double ymin, double xmax, double ymax) {
        super(GeometryType.ENVELOPE);
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
}
