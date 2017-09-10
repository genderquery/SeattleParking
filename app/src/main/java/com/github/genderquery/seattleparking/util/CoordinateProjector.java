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

package com.github.genderquery.seattleparking.util;

import android.support.annotation.NonNull;
import android.util.Log;
import com.github.genderquery.arcgis.model.Envelope;
import com.github.genderquery.arcgis.model.Point;
import com.github.genderquery.arcgis.model.SpatialReference;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.io.IOException;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.UnknownAuthorityCodeException;
import org.osgeo.proj4j.io.Proj4FileReader;
import org.osgeo.proj4j.proj.Projection;

/**
 * Projects geometries inverseProject and project coordinates specified in degrees and coordinates
 * specified in system-specific units.
 */
public class CoordinateProjector {

  private static final String TAG = "CoordinateProjector";

  private final Projection projection;

  /**
   * Creates a CoordinateProjector using ESRI as an authority and the ID of the SpatialReference.
   *
   * @param spatialReference the ID of the coordinate reference system
   */
  public CoordinateProjector(@NonNull SpatialReference spatialReference) {
    this("ESRI", String.valueOf(spatialReference.wkid));
  }

  /**
   * Create a CoordinateProjector using the specified authority and ID.
   */
  public CoordinateProjector(@NonNull String authority, @NonNull String id) {
    String name = authority + ":" + id;
    Proj4FileReader proj4FileReader = new Proj4FileReader();
    String[] params = new String[0];
    try {
      params = proj4FileReader.readParametersFromFile(authority, id);
    } catch (IOException e) {
      // the parameter files should always be present in /nad of proj4j
      Log.wtf(TAG, Log.getStackTraceString(e));
    }
    if (params == null) {
      throw new UnknownAuthorityCodeException(name);
    }
    CRSFactory crsFactory = new CRSFactory();
    CoordinateReferenceSystem coordinateReferenceSystem =
        crsFactory.createFromParameters(name, params);
    projection = coordinateReferenceSystem.getProjection();
  }

  /**
   * Projects a geographic bounds (in degrees) producing an envelope in the units of the target
   * coordinate system.
   *
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public Envelope project(@NonNull LatLngBounds latLngBounds) {
    return new Envelope(project(latLngBounds.southwest), project(latLngBounds.northeast));
  }

  /**
   * Projects a geographic point (in degrees) producing a point in the units of the target
   * coordinate system.
   *
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public Point project(@NonNull LatLng latLng) {
    ProjCoordinate coordinate = project(latLng.longitude, latLng.latitude);
    return new Point(coordinate.x, coordinate.y);
  }

  /**
   * Projects a geographic point (in degrees) producing a point in the units of the target
   * coordinate system.
   *
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public ProjCoordinate project(double longitude, double latitude) {
    ProjCoordinate inputCoordinate = new ProjCoordinate(longitude, latitude);
    ProjCoordinate outputCoordinate = new ProjCoordinate();
    return projection.project(inputCoordinate, outputCoordinate);
  }

  /**
   * Inverse-projects an envelope in the units defined by the coordinate system producing a
   * geographic bounds in degrees longitude and longitude.
   *
   * @return the inverse-projected geographic coordinate in degrees longitude and longitude
   */
  public LatLngBounds inverseProject(@NonNull Envelope envelope) {
    return new LatLngBounds(inverseProject(new Point(envelope.xmin, envelope.ymin)),
        inverseProject(new Point(envelope.xmax, envelope.ymax)));
  }

  /**
   * Inverse-projects a point in the units defined by the coordinate system producing a
   * geographic point in degrees longitude and longitude.
   *
   * @return the inverse-projected geographic coordinate in degrees longitude and longitude
   */
  public LatLng inverseProject(@NonNull Point point) {
    ProjCoordinate coordinate = inverseProject(point.x, point.y);
    return new LatLng(coordinate.y, coordinate.x);
  }

  /**
   * Inverse-projects a point in the units defined by the coordinate system producing a
   * geographic point in degrees longitude and longitude.
   *
   * @param x the x ordinate or easting
   * @param y the y ordinate or northing
   * @return the inverse-projected geographic coordinate in degrees longitude and longitude
   */
  public ProjCoordinate inverseProject(double x, double y) {
    ProjCoordinate inputCoordinate = new ProjCoordinate(x, y);
    ProjCoordinate outputCoordinate = new ProjCoordinate();
    return projection.inverseProject(inputCoordinate, outputCoordinate);
  }
}
