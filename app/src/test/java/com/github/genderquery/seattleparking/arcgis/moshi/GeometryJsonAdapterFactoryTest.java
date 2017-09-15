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

import static com.github.genderquery.seattleparking.arcgis.geometry.Matchers.pathCollectionCloseTo;
import static com.github.genderquery.seattleparking.projection.Constants.COORDINATE_DELTA;
import static org.junit.Assert.assertThat;

import com.github.genderquery.seattleparking.arcgis.geometry.Path;
import com.github.genderquery.seattleparking.arcgis.geometry.Point;
import com.github.genderquery.seattleparking.arcgis.geometry.Polyline;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.InputStream;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import org.junit.Before;
import org.junit.Test;

public class GeometryJsonAdapterFactoryTest {

  private Moshi moshi;

  @Before
  public void setUp() throws Exception {
    moshi = new Moshi.Builder()
        .add(new GeometryJsonAdapterFactory())
        .build();
  }

  @Test
  public void polyline2D() throws Exception {
    JsonAdapter<Polyline> adapter = moshi.adapter(Polyline.class);
    BufferedSource testData = getTestData("/geometry/polyline_2d.json");
    Polyline actual;
    try {
      actual = adapter.fromJson(testData);
    } finally {
      testData.close();
    }
    Polyline expected = new Polyline();
    Path path0 = new Path();
    path0.add(new Point(-97.06138, 32.837));
    path0.add(new Point(-97.06133, 32.836));
    path0.add(new Point(-97.06124, 32.834));
    path0.add(new Point(-97.06127, 32.832));
    expected.add(path0);
    Path path1 = new Path();
    path1.add(new Point(-97.06326, 32.759));
    path1.add(new Point(-97.06298, 32.755));
    expected.add(path1);
    assertThat(actual, pathCollectionCloseTo(expected, COORDINATE_DELTA));
  }

  BufferedSource getTestData(String path) {
    InputStream inputStream = getClass().getResourceAsStream(path);
    Source source = Okio.source(inputStream);
    return Okio.buffer(source);
  }
}