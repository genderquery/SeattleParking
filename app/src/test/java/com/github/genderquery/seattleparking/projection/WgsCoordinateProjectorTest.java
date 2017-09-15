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

import static com.github.genderquery.seattleparking.projection.Constants.COORDINATE_DELTA;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_LAT;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_LNG;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_X;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_Y;
import static com.github.genderquery.seattleparking.projection.Constants.STATE_PLANE_WA_N_WKID;
import static com.github.genderquery.seattleparking.projection.Matchers.coordinateCloseTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.osgeo.proj4j.ProjCoordinate;

public class WgsCoordinateProjectorTest {

  private static final ProjCoordinate SEATTLE_CENTER_WGS =
      new ProjCoordinate(SEATTLE_CENTER_LNG, SEATTLE_CENTER_LAT);
  private static final ProjCoordinate SEATTLE_CENTER_WA_N =
      new ProjCoordinate(SEATTLE_CENTER_X, SEATTLE_CENTER_Y);

  private WgsCoordinateProjector coordinateProjector;

  @Before
  public void setUp() throws Exception {
    coordinateProjector = new WgsCoordinateProjector("ESRI", String.valueOf(STATE_PLANE_WA_N_WKID));
  }

  @Test
  public void project() throws Exception {
    ProjCoordinate actual = coordinateProjector.project(SEATTLE_CENTER_WGS);
    assertThat(actual, coordinateCloseTo(SEATTLE_CENTER_WA_N, COORDINATE_DELTA));
  }

  @Test
  public void inverseProject() throws Exception {
    ProjCoordinate actual = coordinateProjector.inverseProject(SEATTLE_CENTER_WA_N);
    assertThat(actual, coordinateCloseTo(SEATTLE_CENTER_WGS, COORDINATE_DELTA));
  }
}