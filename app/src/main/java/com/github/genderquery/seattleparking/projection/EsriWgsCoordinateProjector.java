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
import com.github.genderquery.seattleparking.arcgis.geometry.Envelope;
import com.github.genderquery.seattleparking.arcgis.geometry.GeometryCollection;
import com.github.genderquery.seattleparking.arcgis.geometry.Multipoint;
import com.github.genderquery.seattleparking.arcgis.geometry.Path;
import com.github.genderquery.seattleparking.arcgis.geometry.Point;
import com.github.genderquery.seattleparking.arcgis.geometry.Polygon;
import com.github.genderquery.seattleparking.arcgis.geometry.Polyline;
import com.github.genderquery.seattleparking.arcgis.model.SpatialReference;
import org.osgeo.proj4j.ProjCoordinate;

public class EsriWgsCoordinateProjector extends WgsCoordinateProjector {

  /**
   * Creates a WgsCoordinateProjector using ESRI as an authority and the well-known ID of the
   * specified {@link SpatialReference}.
   **/
  public EsriWgsCoordinateProjector(@NonNull SpatialReference spatialReference) {
    this(spatialReference.wkid);
  }

  /**
   * Creates a WgsCoordinateProjector using ESRI as an authority and the well-known ID of the
   * coordinate system.
   *
   * @param wkid well-known ID of coordinate system
   */
  public EsriWgsCoordinateProjector(int wkid) {
    super("ESRI", String.valueOf(wkid));
  }

  /**
   * Projects a geographic coordinate in degrees longitude and latitude producing a coordinate in
   * the unit defined by the target coordinate system.
   *
   * @param x the longitude or easting in degrees
   * @param y the latitude or northing in degrees
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public Point project(double x, double y) {
    ProjCoordinate src = new ProjCoordinate(x, y);
    ProjCoordinate dest = project(src);
    return new Point(dest.x, dest.y);
  }

  /**
   * Projects a geographic coordinate in degrees longitude and latitude producing a coordinate in
   * the unit defined by the target coordinate system.
   *
   * @return the projected coordinate in the unit defined by the target coordinate system
   */
  public Point project(@NonNull Point point) {
    return project(point.getX(), point.getY());
  }

  /**
   * Projects a geographic coordinate envelope in degrees longitude and latitude producing a
   * coordinate envelope in the unit defined by the target coordinate system.
   *
   * @param xmin the west-most longitude in degrees
   * @param ymin the south-most latitude in degrees
   * @param xmax the east-most longitude in degrees
   * @param ymax the north-most latitude in degrees
   * @return the projected coordinate envelope in the unit defined by the target coordinate system
   */
  public Envelope project(double xmin, double ymin, double xmax, double ymax) {
    Point min = project(xmin, ymin);
    Point max = project(xmax, ymax);
    return new Envelope(min.getX(), min.getY(), max.getX(), max.getY());
  }

  /**
   * Projects a geographic coordinate envelope in degrees longitude and latitude producing a
   * coordinate envelope in the unit defined by the target coordinate system.
   *
   * @return the projected coordinate envelope in the unit defined by the target coordinate system
   */
  public Envelope project(@NonNull Envelope src) {
    return project(src.getXMin(), src.getYMin(), src.getXMax(), src.getYMax());
  }

  /**
   * Projects a Multipoint of geographic coordinates in degrees longitude and latitude producing a
   * Multipoint of coordinates in the unit defined by the target coordinate system.
   *
   * @return the projected Multipoint of coordinates in the unit defined by the target coordinate
   * system
   */
  public Multipoint project(@NonNull Multipoint src) {
    return (Multipoint) projectPoints(src, new Multipoint());
  }

  /**
   * Projects a Polyline of geographic coordinates in degrees longitude and latitude producing a
   * Polyline of coordinates in the unit defined by the target coordinate system.
   *
   * @return the projected Polyline of coordinates in the unit defined by the target coordinate
   * system
   */
  public Polyline project(@NonNull Polyline src) {
    return (Polyline) projectPaths(src, new Polyline());
  }

  /**
   * Projects a Polygon of geographic coordinates in degrees longitude and latitude producing a
   * Polygon of coordinates in the unit defined by the target coordinate system.
   *
   * @return the projected Polygon of coordinates in the unit defined by the target coordinate
   * system
   */
  public Polygon project(@NonNull Polygon src) {
    return (Polygon) projectPaths(src, new Polygon());
  }

  /**
   * Inverse-projects a coordinate in the unit defined by the coordinate system producing a
   * geographic coordinate in degrees longitude and longitude.
   *
   * @param x the easting in the unit defined by the coordinate system
   * @param y the northing in the unit defined by the coordinate system
   * @return the inverse-projected geographic coordinate in degrees longitude and latitude
   */
  public Point inverseProject(double x, double y) {
    ProjCoordinate src = new ProjCoordinate(x, y);
    ProjCoordinate dest = inverseProject(src);
    return new Point(dest.x, dest.y);
  }

  /**
   * Inverse-projects a coordinate in the unit defined by the coordinate system producing a
   * geographic coordinate in degrees longitude and longitude.
   *
   * @return the inverse-projected geographic coordinate in degrees longitude and latitude
   */
  public Point inverseProject(@NonNull Point point) {
    return inverseProject(point.getX(), point.getY());
  }

  /**
   * Inverse-projects a geographic coordinate envelope in the unit defined by the coordinate system
   * producing a geographic coordinate envelope in degrees longitude and longitude.
   *
   * @param xmin the west-most easting in the unit defined by the coordinate system
   * @param ymin the south-most northing in the unit defined by the coordinate system
   * @param xmax the east-most easting in the unit defined by the coordinate system
   * @param ymax the north-most northing in the unit defined by the coordinate system
   * @return the inverse-project geographic coordinate envelope degrees longitude and longitude
   */
  public Envelope inverseProject(double xmin, double ymin, double xmax, double ymax) {
    Point min = inverseProject(xmin, ymin);
    Point max = inverseProject(xmax, ymax);
    return new Envelope(min.getX(), min.getY(), max.getX(), max.getY());
  }

  /**
   * Inverse-projects a coordinate envelope in the unit defined by the coordinate system producing a
   * geographic coordinate envelope in degrees longitude and longitude.
   *
   * @return the inverse-projected geographic coordinate envelope in degrees longitude and longitude
   */
  public Envelope inverseProject(@NonNull Envelope src) {
    return inverseProject(src.getXMin(), src.getYMin(), src.getXMax(), src.getYMax());
  }

  /**
   * Inverse-projects a Multipoint of coordinates in the unit defined by the coordinate system
   * producing a Multipoint of geographic coordinates in degrees longitude and longitude.
   *
   * @return the inverse-projected Multipoint of geographic coordinates in degrees longitude and
   * latitude
   */
  public Multipoint inverseProject(@NonNull Multipoint src) {
    return (Multipoint) inverseProjectPoints(src, new Multipoint());
  }

  /**
   * Inverse-projects a Polyline of coordinates in the unit defined by the coordinate system
   * producing a Polyline of geographic coordinates in degrees longitude and longitude.
   *
   * @return the inverse-projected Polyline of geographic coordinates in degrees longitude and
   * latitude
   */
  public Polyline inverseProject(@NonNull Polyline src) {
    return (Polyline) inverseProjectPaths(src, new Polyline());
  }

  /**
   * Inverse-projects a Polygon of coordinates in the unit defined by the coordinate system
   * producing a Polygon of geographic coordinates in degrees longitude and longitude.
   *
   * @return the inverse-projected Polygon of geographic coordinates in degrees longitude and
   * latitude
   */
  public Polygon inverseProject(@NonNull Polygon src) {
    return (Polygon) inverseProjectPaths(src, new Polygon());
  }

  private GeometryCollection<Point> projectPoints(@NonNull GeometryCollection<Point> src,
      @NonNull GeometryCollection<Point> dest) {
    for (Point point : src) {
      dest.add(project(point));
    }
    return dest;
  }

  private Path projectPath(@NonNull Path src) {
    return (Path) projectPoints(src, new Path());
  }

  private GeometryCollection<Path> projectPaths(@NonNull GeometryCollection<Path> src,
      @NonNull GeometryCollection<Path> dest) {
    for (Path path : src) {
      dest.add(projectPath(path));
    }
    return dest;
  }

  private GeometryCollection<Point> inverseProjectPoints(@NonNull GeometryCollection<Point> src,
      @NonNull GeometryCollection<Point> dest) {
    for (Point point : src) {
      dest.add(inverseProject(point));
    }
    return dest;
  }

  private Path inverseProjectPath(@NonNull Path src) {
    return (Path) inverseProjectPoints(src, new Path());
  }

  private GeometryCollection<Path> inverseProjectPaths(@NonNull GeometryCollection<Path> src,
      @NonNull GeometryCollection<Path> dest) {
    for (Path path : src) {
      dest.add(inverseProjectPath(path));
    }
    return dest;
  }
}
