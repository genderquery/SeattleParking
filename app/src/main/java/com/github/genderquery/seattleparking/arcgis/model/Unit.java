/*
 * MIT License
 *
 * Copyright (c) 2017 Avery
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.genderquery.seattleparking.arcgis.model;

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
