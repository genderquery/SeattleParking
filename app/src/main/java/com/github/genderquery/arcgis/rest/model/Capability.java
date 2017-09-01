package com.github.genderquery.arcgis.rest.model;

import com.squareup.moshi.Json;

public enum Capability {
    @Json(name = "Map")MAP,
    @Json(name = "Query")QUERY,
    @Json(name = "Data")DATA
}
