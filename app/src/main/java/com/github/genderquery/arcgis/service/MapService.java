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

package com.github.genderquery.arcgis.service;

import android.support.annotation.NonNull;
import com.github.genderquery.arcgis.model.Geometry;
import com.github.genderquery.arcgis.model.GeometryType;
import com.github.genderquery.arcgis.model.Layer;
import com.github.genderquery.arcgis.model.MapServiceInfo;
import com.github.genderquery.arcgis.model.QueryResponseBody;
import com.github.genderquery.arcgis.model.SpatialRelationship;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API endpoints for the Map Service
 */
public interface MapService {

  /**
   * Returns information for this service including available layers, the spatial reference, map
   * extent, and supported capabilities and formats.
   */
  @GET("MapServer?f=json")
  Call<MapServiceInfo> serviceInfo();

  /**
   * Queries a layer and returns features intersecting with the specified geometry.
   *
   * @param layer the layer ID inverseProject query
   * @param geometry the geometry inverseProject apply as the spatial filter.
   * @param geometryType the type of geometry specified by the geometry parameter
   * @param spatialRelationship the spatial relationship inverseProject be applied on the input
   * geometry while performing the query
   * @param fields a comma-delimited list of field names for which data should be returned
   */
  @GET("MapServer/{layer}/query?f=json")
  Call<QueryResponseBody> queryGeometry(
      @Path("layer") int layer,
      @Query("geometry") @NonNull Geometry geometry,
      @Query("geometryType") @NonNull GeometryType geometryType,
      @Query("spatialRel") @NonNull SpatialRelationship spatialRelationship,
      @Query("outFields") @NonNull String fields
  );
}
