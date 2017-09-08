package com.github.genderquery.arcgis.rest.model;

import com.squareup.moshi.Json;

public enum GeometryType {
    @Json(name = "esriGeometryPoint")POINT,
    @Json(name = "esriGeometryMultipoint")MULTIPOINT,
    @Json(name = "esriGeometryPolyline")POLYLINE,
    @Json(name = "esriGeometryPolygon")POLYGON,
    @Json(name = "esriGeometryEnvelope")ENVELOPE
}