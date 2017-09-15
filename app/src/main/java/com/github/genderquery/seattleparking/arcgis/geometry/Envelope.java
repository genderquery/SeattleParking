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

public final class Envelope extends Geometry {

  private final double xmin;
  private final double xmax;
  private final double ymin;
  private final double ymax;

  public Envelope() {
    this(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
  }

  public Envelope(double xmin, double ymin, double xmax, double ymax) {
    super();
    this.xmin = xmin;
    this.ymin = ymin;
    this.xmax = xmax;
    this.ymax = ymax;
  }

  public double getXMin() {
    return xmin;
  }

  public double getXMax() {
    return xmax;
  }

  public double getYMin() {
    return ymin;
  }

  public double getYMax() {
    return ymax;
  }

  @Override
  public String toString() {
    return "Envelope{ " +
        "xmin=" + xmin + ", " +
        "ymin=" + ymin + ", " +
        "xmax=" + xmax + ", " +
        "ymax=" + ymax +
        "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Envelope)) {
      return false;
    }
    Envelope that = (Envelope) o;
    if (Double.compare(that.xmin, xmin) != 0) {
      return false;
    }
    if (Double.compare(that.ymin, ymin) != 0) {
      return false;
    }
    if (Double.compare(that.xmax, xmax) != 0) {
      return false;
    }
    if (Double.compare(that.ymax, ymax) != 0) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(xmin);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(ymin);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(xmax);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(ymax);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
