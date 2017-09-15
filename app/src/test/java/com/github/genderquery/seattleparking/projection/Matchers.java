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
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.osgeo.proj4j.ProjCoordinate;

public class Matchers {

  public static CoordinateCloseTo coordinateCloseTo(@NonNull ProjCoordinate expected,
      double delta) {
    return new CoordinateCloseTo(expected, delta);
  }

  private static class CoordinateCloseTo extends TypeSafeMatcher<ProjCoordinate> {

    final ProjCoordinate expected;
    final double delta;

    CoordinateCloseTo(@NonNull ProjCoordinate expected, double delta) {
      super();
      this.expected = expected;
      this.delta = delta;
    }

    static boolean doubleIsDifferent(double d1, double d2, double delta) {
      if (Double.compare(d1, d2) == 0) {
        return false;
      }
      if ((Math.abs(d1 - d2) <= delta)) {
        return false;
      }
      return true;
    }

    @Override
    protected boolean matchesSafely(ProjCoordinate item) {
      if (doubleIsDifferent(expected.x, item.x, delta)) {
        return false;
      }
      if (doubleIsDifferent(expected.y, item.y, delta)) {
        return false;
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description
          .appendText("a coordinate at ")
          .appendValueList("(", ", ", ") ", expected.x, expected.y)
          .appendText("±")
          .appendValue(delta);
    }

    @Override
    protected void describeMismatchSafely(ProjCoordinate item, Description mismatchDescription) {
      mismatchDescription
          .appendText("was at ")
          .appendValueList("(", ", ", ")", item.x, item.y);
    }
  }
}
