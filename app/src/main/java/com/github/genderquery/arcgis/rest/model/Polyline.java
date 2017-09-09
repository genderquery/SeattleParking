package com.github.genderquery.arcgis.rest.model;

import java.util.List;

public class Polyline extends Geometry {

    private final List<List<Point>> paths;

    public Polyline(List<List<Point>> paths) {
        super(GeometryType.POLYLINE);
        this.paths = paths;
    }

    public List<List<Point>> getLines() {
        return paths;
    }
}
