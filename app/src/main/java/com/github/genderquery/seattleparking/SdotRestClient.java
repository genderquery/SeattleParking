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

package com.github.genderquery.seattleparking;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import com.github.genderquery.seattleparking.arcgis.ArcGisRestClient;
import com.github.genderquery.seattleparking.arcgis.model.SpatialReference;
import com.github.genderquery.seattleparking.model.ParkingCategory;
import java.util.EnumMap;
import okhttp3.OkHttpClient;

class SdotRestClient extends ArcGisRestClient {

  public static final SpatialReference WGS_1984_WEB_MERCATOR_AUXILIARY_SPHERE =
      new SpatialReference(3857);
  public static final int LAYER_GARAGES_AND_LOTS = 4;
  public static final int LAYER_STREET_PARKING_SIGNS = 5;
  public static final int LAYER_TEMPORARY_NO_PARKING = 6;
  public static final int LAYER_STREET_PARKING_BY_CATEGORY = 7;
  public static final int LAYER_PEAK_HOUR_NO_PARKING = 8;

  private static final String CATALOG_URL = "http://gisrevprxy.seattle.gov/" +
      "arcgis/rest/services/SDOT_EXT/SDOT_Parking/";
  private static SdotRestClient client;

  public EnumMap<ParkingCategory, Symbol> parkingCategorySymbols;

  {
    DashPathEffect dashPathEffect = new DashPathEffect(new float[]{10, 10}, 0);
    parkingCategorySymbols = new EnumMap<>(ParkingCategory.class);
    parkingCategorySymbols.put(ParkingCategory.CARPOOL_PARKING, new Symbol(Color.BLACK));
    parkingCategorySymbols.put(ParkingCategory.NO_PARKING_ALLOWED, new Symbol(Color.RED));
    parkingCategorySymbols.put(ParkingCategory.PAID_PARKING, new Symbol(Color.BLUE));
    parkingCategorySymbols.put(ParkingCategory.RESTRICTED_PARKING_ZONE,
        new Symbol(Color.RED, dashPathEffect));
    parkingCategorySymbols.put(ParkingCategory.TIME_LIMITED_PARKING,
        new Symbol(Color.GREEN, dashPathEffect));
    parkingCategorySymbols.put(ParkingCategory.UNRESTRICTED_PARKING, new Symbol(Color.GREEN));
  }

  private SdotRestClient(String url, OkHttpClient httpClient) {
    super(url, httpClient);
  }

  public static SdotRestClient getClient() {
    if (client == null) {
      OkHttpClient httpClient = new OkHttpClient.Builder()
          .build();
      client = new SdotRestClient(CATALOG_URL, httpClient);
    }
    return client;
  }
}
