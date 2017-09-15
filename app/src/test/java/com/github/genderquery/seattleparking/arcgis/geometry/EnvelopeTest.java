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

package com.github.genderquery.seattleparking.arcgis.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnvelopeTest {

  private static final double NO_DELTA = 0.0;

  /**
   * The API specifies that empty values should be NaN.
   */
  @Test
  public void testDefaultConstructor() throws Exception {
    Envelope envelope = new Envelope();
    assertEquals(Double.NaN, envelope.getXMin(), NO_DELTA);
    assertEquals(Double.NaN, envelope.getYMin(), NO_DELTA);
    assertEquals(Double.NaN, envelope.getXMax(), NO_DELTA);
    assertEquals(Double.NaN, envelope.getYMax(), NO_DELTA);
  }

  @Test
  public void testConstructor() throws Exception {
    double xmin = 1;
    double ymin = 2;
    double xmax = 3;
    double ymax = 4;
    Envelope envelope = new Envelope(xmin, ymin, xmax, ymax);
    assertEquals(xmin, envelope.getXMin(), NO_DELTA);
    assertEquals(ymin, envelope.getYMin(), NO_DELTA);
    assertEquals(xmax, envelope.getXMax(), NO_DELTA);
    assertEquals(ymax, envelope.getYMax(), NO_DELTA);
  }
}
