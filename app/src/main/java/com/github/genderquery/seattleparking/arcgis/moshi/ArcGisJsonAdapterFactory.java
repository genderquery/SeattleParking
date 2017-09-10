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
import android.util.Log;
import com.github.genderquery.seattleparking.arcgis.model.Geometry;
import com.github.genderquery.seattleparking.arcgis.model.Point;
import com.github.genderquery.seattleparking.arcgis.model.Polyline;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Converts JSON objects found in the API that Moshi cannot otherwise convert on its own.
 */
public class ArcGisJsonAdapterFactory implements JsonAdapter.Factory {

  @Nullable
  @Override
  public JsonAdapter<?> create(@NonNull Type type,
      @NonNull Set<? extends Annotation> annotations,
      @NonNull Moshi moshi) {
    Class<?> rawType = Types.getRawType(type);
    if (rawType.isAssignableFrom(Geometry.class)) {
      return new GeometryJsonAdapter();
    }
    return null;
  }

  private static class GeometryJsonAdapter extends JsonAdapter<Geometry> {

    private static final String TAG = "GeometryJsonAdapter";

    @Nullable
    @Override
    public Geometry fromJson(@NonNull JsonReader in) throws IOException {
      if (in.peek() == JsonReader.Token.NULL) {
        return null;
      }
      Geometry geometry = null;
      double x = 0;
      double y = 0;
      in.beginObject();
      while (in.hasNext()) {
        switch (in.nextName()) {
          // TODO support m and z
          case "x":
            x = in.nextDouble();
            break;
          case "y":
            y = in.nextDouble();
            break;
          case "paths":
            geometry = parsePaths(in);
            break;
          default:
            Log.d(TAG, "Skipping " + in.getPath());
            in.skipValue();
        }
      }
      in.endObject();
      if (geometry == null) {
        return new Point(x, y);
      }
      return geometry;
    }

    @NonNull
    private Polyline parsePaths(@NonNull JsonReader in) throws IOException {
      List<List<Point>> paths = new ArrayList<>();
      in.beginArray();
      while (in.hasNext()) {
        paths.add(parsePoints(in));
      }
      in.endArray();
      return new Polyline(paths);
    }

    @NonNull
    private List<Point> parsePoints(JsonReader in) throws IOException {
      List<Point> points = new ArrayList<>();
      in.beginArray();
      while (in.hasNext()) {
        points.add(parsePoint(in));
      }
      in.endArray();
      return points;
    }

    @NonNull
    private Point parsePoint(@NonNull JsonReader in) throws IOException {
      in.beginArray();
      // TODO support m and z
      double x = in.nextDouble();
      double y = in.nextDouble();
      in.endArray();
      return new Point(x, y);
    }

    @Override
    public void toJson(@NonNull JsonWriter out, Geometry value) throws IOException {
      // TODO?
      throw new UnsupportedOperationException();
    }
  }
}
