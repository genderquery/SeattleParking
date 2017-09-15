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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointTest {

  private static final double NO_DELTA = 0.0;

  /**
   * The API specifies that empty values should be NaN.
   */
  @Test
  public void testDefaultConstructor() throws Exception {
    Point point = new Point();
    assertEquals(Double.NaN, point.getX(), NO_DELTA);
    assertEquals(Double.NaN, point.getY(), NO_DELTA);
  }

  @Test
  public void testXYConstructor() throws Exception {
    double x = 1;
    double y = 2;
    Point point = new Point(x, y);
    assertEquals(x, point.getX(), NO_DELTA);
    assertEquals(y, point.getY(), NO_DELTA);
  }

  @Test
  public void equals() throws Exception {
    Point p1 = new Point(1, 2);
    Point p2 = new Point(1, 2);
    Point p3 = new Point(1, 3);
    Point p4 = new Point(2, 1);
    Geometry g = new Point(1, 2);

    assertTrue(p1.equals(p2));
    assertTrue(p2.equals(p1));

    assertFalse(p1.equals(p3));
    assertFalse(p1.equals(p4));

    assertFalse(p3.equals(p1));
    assertFalse(p4.equals(p1));

    assertTrue(p1.equals(g));
    assertTrue(g.equals(p1));
  }
}