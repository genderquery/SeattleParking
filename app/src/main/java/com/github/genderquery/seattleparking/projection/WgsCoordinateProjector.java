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

package com.github.genderquery.seattleparking.projection;

import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.UnknownAuthorityCodeException;
import org.osgeo.proj4j.io.Proj4FileReader;
import org.osgeo.proj4j.proj.Projection;

/**
 * Projects coordinates and geometries to and from a unit-specific Cartesian coordinate system and
 * the World Geodetic System (WGS 84) with coordinates in degrees latitude and longitude.
 */
public class WgsCoordinateProjector implements CoordinateProjector {

  private static final String TAG = "WgsCoordinateProjector";

  private final Projection projection;

  /**
   * Create a WgsCoordinateProjector using the specified authority and ID.
   */
  public WgsCoordinateProjector(@NonNull String authority, @NonNull String id) {
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
   * Projects a geographic coordinate in degrees longitude and latitude producing a coordinate in
   * the unit defined by the target coordinate system.
   *
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public ProjCoordinate project(@NonNull ProjCoordinate src) {
    ProjCoordinate dest = new ProjCoordinate();
    return projection.project(src, dest);
  }

  /**
   * Inverse-projects a coordinate in the unit defined by the coordinate system producing a
   * geographic coordinate in degrees longitude and longitude.
   *
   * @return the inverse-projected geographic coordinate in degrees longitude and latitude
   */
  public ProjCoordinate inverseProject(@NonNull ProjCoordinate src) {
    ProjCoordinate dest = new ProjCoordinate();
    return projection.inverseProject(src, dest);
  }
}
