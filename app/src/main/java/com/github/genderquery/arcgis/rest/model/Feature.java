package com.github.genderquery.arcgis.rest.model;

import java.util.Map;

public class Feature {
    // TODO add support for arbitrary JSON values?
    public Map<String,String> attributes;
    public Geometry geometry;
}
