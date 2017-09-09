package com.github.genderquery.arcgis.rest.model;

import java.util.List;

public class QueryResponseBody {
    public GeometryType geometryType;
    public SpatialReference spatialReference;
    public List<Feature> features;
}
