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

class Constants {

  static final int STATE_PLANE_WA_N_WKID = 2926;

  /**
   * The maximum delta between expected and actual ordinates for which both numbers are still
   * considered equal.
   */
  static final double COORDINATE_DELTA = 0.000001;

  static final double SEATTLE_CENTER_LNG = -122.333056;
  static final double SEATTLE_CENTER_LAT = 47.609722;

  static final double SEATTLE_SOUTHWEST_LNG = -122.43492783050658;
  static final double SEATTLE_SOUTHWEST_LAT = 47.47051867180209;
  static final double SEATTLE_NORTHEAST_LNG = -122.22077900394366;
  static final double SEATTLE_NORTHEAST_LAT = 47.74848902648531;

  /**
   * The x ordinate in ftUS for the geographic center of the city of Seattle, WA in the State Plane
   * Washington North coordinate system.
   */
  static final double SEATTLE_CENTER_X = 1270504.064320;
  /**
   * The y ordinate in ftUS for the geographic center of the city of Seattle, WA in the State Plane
   * Washington North coordinate system.
   */
  static final double SEATTLE_CENTER_Y = 226012.543482;

  static final double SEATTLE_SOUTHWEST_X = 1244323.81528143;
  static final double SEATTLE_SOUTHWEST_Y = 175753.341380019;
  static final double SEATTLE_NORTHEAST_X = 1299107.04302181;
  static final double SEATTLE_NORTHEAST_Y = 276102.332567195;
}