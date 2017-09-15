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

import static com.github.genderquery.seattleparking.arcgis.geometry.Matchers.envelopeCloseTo;
import static com.github.genderquery.seattleparking.arcgis.geometry.Matchers.pointCloseTo;
import static com.github.genderquery.seattleparking.projection.Constants.COORDINATE_DELTA;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_LAT;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_LNG;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_X;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_CENTER_Y;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_NORTHEAST_LAT;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_NORTHEAST_LNG;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_NORTHEAST_X;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_NORTHEAST_Y;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_SOUTHWEST_LAT;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_SOUTHWEST_LNG;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_SOUTHWEST_X;
import static com.github.genderquery.seattleparking.projection.Constants.SEATTLE_SOUTHWEST_Y;
import static com.github.genderquery.seattleparking.projection.Constants.STATE_PLANE_WA_N_WKID;
import static org.junit.Assert.assertThat;

import com.github.genderquery.seattleparking.arcgis.geometry.Envelope;
import com.github.genderquery.seattleparking.arcgis.geometry.Multipoint;
import com.github.genderquery.seattleparking.arcgis.geometry.Point;
import org.junit.Before;
import org.junit.Test;

public class EsriWgsCoordinateProjectorTest {

  private EsriWgsCoordinateProjector coordinateProjector;
  private Point samplePointWgs;
  private Point samplePointWaN;
  private Envelope sampleEnvelopeWgs;
  private Envelope sampleEnvelopeWaN;
  private Multipoint sampleMultipointWgs;

  @Before
  public void setUp() throws Exception {
    coordinateProjector = new EsriWgsCoordinateProjector(STATE_PLANE_WA_N_WKID);
    samplePointWgs = new Point(SEATTLE_CENTER_LNG, SEATTLE_CENTER_LAT);
    samplePointWaN = new Point(SEATTLE_CENTER_X, SEATTLE_CENTER_Y);
    sampleEnvelopeWgs = new Envelope(SEATTLE_SOUTHWEST_LNG, SEATTLE_SOUTHWEST_LAT,
        SEATTLE_NORTHEAST_LNG, SEATTLE_NORTHEAST_LAT);
    sampleEnvelopeWaN = new Envelope(SEATTLE_SOUTHWEST_X, SEATTLE_SOUTHWEST_Y,
        SEATTLE_NORTHEAST_X, SEATTLE_NORTHEAST_Y);
    sampleMultipointWgs = new Multipoint();
    sampleMultipointWgs.add(new Point(SEATTLE_SOUTHWEST_LNG, SEATTLE_SOUTHWEST_LAT));
    sampleMultipointWgs.add(new Point(SEATTLE_SOUTHWEST_LNG, SEATTLE_NORTHEAST_LNG));
    sampleMultipointWgs.add(new Point(SEATTLE_NORTHEAST_LNG, SEATTLE_NORTHEAST_LNG));
    sampleMultipointWgs.add(new Point(SEATTLE_NORTHEAST_LNG, SEATTLE_NORTHEAST_LAT));
  }

  @Test
  public void projectPoint() throws Exception {
    Point actual = coordinateProjector.project(samplePointWgs);
    assertThat(actual, pointCloseTo(samplePointWaN, COORDINATE_DELTA));
  }

  @Test
  public void projectEnvelope() throws Exception {
    Envelope actual = coordinateProjector.project(sampleEnvelopeWgs);
    assertThat(actual, envelopeCloseTo(sampleEnvelopeWaN, COORDINATE_DELTA));
  }

  @Test
  public void inverseProjectPoint() throws Exception {
    Point actual = coordinateProjector.inverseProject(samplePointWaN);
    assertThat(actual, pointCloseTo(samplePointWgs, COORDINATE_DELTA));
  }

  @Test
  public void inverseProjectEnvelope() throws Exception {
    Envelope actual = coordinateProjector.inverseProject(sampleEnvelopeWaN);
    assertThat(actual, envelopeCloseTo(sampleEnvelopeWgs, COORDINATE_DELTA));
  }
}
