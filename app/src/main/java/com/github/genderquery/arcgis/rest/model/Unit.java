package com.github.genderquery.arcgis.rest.model;

import com.squareup.moshi.Json;

public enum Unit {
    @Json(name = "esriUnknownUnits")UNKNOWN,
    @Json(name = "esriInches")INCHES,
    @Json(name = "esriPoints")POINTS,
    @Json(name = "esriFeet")FEET,
    @Json(name = "esriYards")YARDS,
    @Json(name = "esriMiles")MILES,
    @Json(name = "esriNauticalMiles")NAUTICAL_MILES,
    @Json(name = "esriMillimeters")MILLIMETERS,
    @Json(name = "esriCentimeters")CENTIMETERS,
    @Json(name = "esriMeters")METERS,
    @Json(name = "esriKilometers")KILOMETERS,
    @Json(name = "esriDecimalDegrees")DECIMAL_DEGREES,
    @Json(name = "esriDecimeters")DECIMETERS
}
