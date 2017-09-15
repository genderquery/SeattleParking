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

package com.github.genderquery.seattleparking.arcgis.moshi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.genderquery.seattleparking.arcgis.geometry.Envelope;
import com.github.genderquery.seattleparking.arcgis.geometry.Geometry;
import com.github.genderquery.seattleparking.arcgis.geometry.Multipoint;
import com.github.genderquery.seattleparking.arcgis.geometry.Path;
import com.github.genderquery.seattleparking.arcgis.geometry.PathCollection;
import com.github.genderquery.seattleparking.arcgis.geometry.Point;
import com.github.genderquery.seattleparking.arcgis.geometry.Polygon;
import com.github.genderquery.seattleparking.arcgis.geometry.Polyline;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class GeometryJsonAdapterFactory implements JsonAdapter.Factory {

  @Nullable
  @Override
  public JsonAdapter<?> create(@NonNull Type type,
      @NonNull Set<? extends Annotation> annotations,
      @NonNull Moshi moshi) {
    Class<?> rawType = Types.getRawType(type);
    if (Geometry.class.isAssignableFrom(rawType)) {
      return new GeometryJsonAdapter();
    }
    return null;
  }

  private static class GeometryJsonAdapter extends JsonAdapter<Geometry> {

    @Nullable
    @Override
    public Geometry fromJson(@NonNull JsonReader in) throws IOException {
      if (in.peek() == JsonReader.Token.NULL) {
        return null;
      }
      Geometry geometry = null;
      Double x = null;
      double y = Double.NaN;
      Double xmin = null;
      double ymin = Double.NaN;
      double xmax = Double.NaN;
      double ymax = Double.NaN;
      in.beginObject();
      while (in.hasNext()) {
        switch (in.nextName()) {
          case "x":
            x = in.nextDouble();
            break;
          case "y":
            y = in.nextDouble();
            break;
          case "xmin":
            xmin = in.nextDouble();
            break;
          case "ymin":
            ymin = in.nextDouble();
            break;
          case "xmax":
            xmax = in.nextDouble();
            break;
          case "ymax":
            ymax = in.nextDouble();
            break;
          case "points":
            geometry = parsePoints(in);
            break;
          case "paths":
            geometry = parsePaths(in, new Polyline());
            break;
          case "rings":
            geometry = parsePaths(in, new Polygon());
            break;
          default:
            in.skipValue();
        }
      }
      in.endObject();
      if (x != null) {
        return new Point(x, y);
      }
      if (xmin != null) {
        return new Envelope(xmin, ymin, xmax, ymax);
      }
      return geometry;
    }

    private Multipoint parsePoints(@NonNull JsonReader in)
        throws IOException {
      Multipoint multipoint = new Multipoint();
      in.beginArray();
      while (in.hasNext()) {
        multipoint.add(parsePointArray(in));
      }
      in.endArray();
      return multipoint;
    }

    private Point parsePointArray(@NonNull JsonReader in)
        throws IOException {
      double x;
      double y;
      in.beginArray();
      x = in.nextDouble();
      y = in.nextDouble();
      while (in.hasNext()) {
        // skip z and m values
        in.skipValue();
      }
      in.endArray();
      return new Point(x, y);
    }

    private PathCollection parsePaths(@NonNull JsonReader in, @NonNull PathCollection paths)
        throws IOException {
      in.beginArray();
      while (in.hasNext()) {
        Path path = new Path();
        in.beginArray();
        while (in.hasNext()) {
          path.add(parsePointArray(in));
        }
        in.endArray();
        paths.add(path);
      }
      in.endArray();
      return paths;
    }

    @Override
    public void toJson(@NonNull JsonWriter out, Geometry value) throws IOException {
      // TODO?
      throw new UnsupportedOperationException();
    }
  }
}
