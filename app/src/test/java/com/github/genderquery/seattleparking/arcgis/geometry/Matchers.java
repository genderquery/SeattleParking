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

import android.support.annotation.NonNull;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {

  public static PointCloseTo pointCloseTo(@NonNull Point expected, double delta) {
    return new PointCloseTo(expected, delta);
  }

  public static EnvelopeCloseTo envelopeCloseTo(@NonNull Envelope expected, double delta) {
    return new EnvelopeCloseTo(expected, delta);
  }

  public static PathCloseTo pathCloseTo(@NonNull Path expected, double delta) {
    return new PathCloseTo(expected, delta);
  }

  public static PathCollectionCloseTo pathCollectionCloseTo(@NonNull PathCollection expected,
      double delta) {
    return new PathCollectionCloseTo(expected, delta);
  }

  private static class PointCloseTo extends GeometryCloseTo<Point> {

    PointCloseTo(@NonNull Point expected, double delta) {
      super(expected, delta);
    }

    @Override
    protected boolean matchesSafely(Point item) {
      if (doubleIsDifferent(expected.getX(), item.getX(), delta)) {
        return false;
      }
      if (doubleIsDifferent(expected.getY(), item.getY(), delta)) {
        return false;
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description
          .appendValue(expected)
          .appendText(" ±")
          .appendValue(delta);
    }

    @Override
    protected void describeMismatchSafely(Point item, Description mismatchDescription) {
      mismatchDescription
          .appendText("was ")
          .appendValue(item);
    }
  }

  private static class EnvelopeCloseTo extends GeometryCloseTo<Envelope> {

    EnvelopeCloseTo(@NonNull Envelope expected, double delta) {
      super(expected, delta);
    }

    @Override
    protected boolean matchesSafely(Envelope item) {
      if (doubleIsDifferent(expected.getXMin(), item.getXMin(), delta)) {
        return false;
      }
      if (doubleIsDifferent(expected.getYMin(), item.getYMin(), delta)) {
        return false;
      }
      if (doubleIsDifferent(expected.getXMax(), item.getXMax(), delta)) {
        return false;
      }
      if (doubleIsDifferent(expected.getYMax(), item.getYMax(), delta)) {
        return false;
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description
          .appendValue(expected)
          .appendText(" ±")
          .appendValue(delta);
    }

    @Override
    protected void describeMismatchSafely(Envelope item, Description mismatchDescription) {
      mismatchDescription
          .appendText("was ")
          .appendValue(expected);
    }
  }

  private static abstract class GeometryCloseTo<T extends Geometry> extends TypeSafeMatcher<T> {

    final T expected;
    final double delta;

    GeometryCloseTo(@NonNull T expected, double delta) {
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
  }

  private static class PathCloseTo extends TypeSafeMatcher<Path> {

    private final Path expected;
    private final double delta;

    PathCloseTo(Path expected, double delta) {
      this.expected = expected;
      this.delta = delta;
    }

    @Override
    protected boolean matchesSafely(Path item) {
      for (int i = 0, length = item.size(); i < length; i++) {
        if (!new PointCloseTo(expected.get(i), delta).matchesSafely(item.get(i))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description
          .appendValueList("[", ",", "]", expected)
          .appendText(" ±")
          .appendValue(delta);
    }

    @Override
    protected void describeMismatchSafely(Path item, Description mismatchDescription) {
      mismatchDescription
          .appendText("was ")
          .appendValueList("[", ",", "]", item);
    }
  }

  private static class PathCollectionCloseTo extends TypeSafeMatcher<PathCollection> {

    private final PathCollection expected;
    private final double delta;

    PathCollectionCloseTo(PathCollection expected, double delta) {
      this.expected = expected;
      this.delta = delta;
    }

    @Override
    protected boolean matchesSafely(PathCollection item) {
      for (int i = 0, length = item.size(); i < length; i++) {
        if (!new PathCloseTo(expected.get(i), delta).matchesSafely(item.get(i))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description
          .appendValueList("[", ",", "]", expected)
          .appendText(" ±")
          .appendValue(delta);
    }

    @Override
    protected void describeMismatchSafely(PathCollection item, Description mismatchDescription) {
      mismatchDescription
          .appendText("was ")
          .appendValueList("[", ",", "]", item);
    }
  }
}
