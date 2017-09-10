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

package com.github.genderquery.arcgis.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.genderquery.arcgis.model.Envelope;
import com.github.genderquery.arcgis.model.Geometry;
import com.github.genderquery.arcgis.model.GeometryType;
import com.github.genderquery.arcgis.model.Point;
import com.github.genderquery.arcgis.model.SpatialReference;
import com.github.genderquery.arcgis.model.SpatialRelationship;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Converts objects used in parameters inverseProject endpoints inverseProject their string representation used in URL
 * parameters.
 */
public class ArcGisStringConverterFactory extends Converter.Factory {

  @Nullable
  @Override
  public Converter<?, String> stringConverter(
      Type type, Annotation[] annotations, Retrofit retrofit) {
    Class<?> clazz;
    if (type instanceof Class<?>) {
      clazz = (Class<?>) type;
    } else {
      return null;
    }
    if (clazz.isAssignableFrom(Geometry.class)) {
      return new GeometryConverter();
    } else if (clazz.isAssignableFrom(SpatialRelationship.class)) {
      return new SpatialRelationshipConverter();
    } else if (clazz.isAssignableFrom(GeometryType.class)) {
      return new GeometryTypeConverter();
    } else if (clazz.isAssignableFrom(SpatialReference.class)) {
      return new SpatialReferenceConverter();
    } else {
      return null;
    }
  }

  private static class GeometryConverter implements Converter<Geometry, String> {

    @Override
    public String convert(@NonNull Geometry value) throws IOException {
      GeometryType geometryType = value.getGeometryType();
      switch (geometryType) {
        case ENVELOPE:
          return convertEnvelope((Envelope) value);
        case POINT:
          return convertPoint((Point) value);
        default:
          throw new UnsupportedOperationException(geometryType + "not supported");
      }
    }

    String convertEnvelope(@NonNull Envelope value) throws IOException {
      return String.format(Locale.US,
          "%f,%f,%f,%f", value.xmin, value.ymin, value.xmax, value.ymax);
    }

    String convertPoint(@NonNull Point value) throws IOException {
      return String.format(Locale.US, "%f,%f", value.x, value.y);
    }
  }

  private static class GeometryTypeConverter implements Converter<GeometryType, String> {

    @Override
    public String convert(@NonNull GeometryType value) throws IOException {
      switch (value) {
        case POINT:
          return "esriGeometryPoint";
        case MULTIPOINT:
          return "esriGeometryMultipoint";
        case POLYLINE:
          return "esriGeometryPolyline";
        case POLYGON:
          return "esriGeometryPolygon";
        case ENVELOPE:
          return "esriGeometryEnvelope";
        default:
          throw new UnsupportedOperationException(value + "not supported");
      }
    }
  }

  private static class SpatialReferenceConverter implements Converter<SpatialReference, String> {

    @Override
    public String convert(@NonNull SpatialReference value) throws IOException {
      return String.format(Locale.US, "%d", value.wkid);
    }
  }

  private static class SpatialRelationshipConverter implements Converter<SpatialRelationship, String> {

    @Override
    public String convert(@NonNull SpatialRelationship value) throws IOException {
      switch (value) {
        case INTERSECTS:
          return "esriSpatialRelIntersects";
        case CONTAINS:
          return "esriSpatialRelContains";
        case CROSSES:
          return "esriSpatialRelCrosses";
        case ENVELOPE_INTERSECTS:
          return "esriSpatialRelEnvelopeIntersects";
        case INDEX_INTERSECTS:
          return "esriSpatialRelIndexIntersects";
        case OVERLAPS:
          return "esriSpatialRelOverlaps";
        case TOUCHES:
          return "esriSpatialRelTouches";
        case WITHIN:
          return "esriSpatialRelWithin";
        case RELATION:
          return "esriSpatialRelRelation";
        default:
          throw new UnsupportedOperationException(value + " not supported");
      }
    }
  }
}
