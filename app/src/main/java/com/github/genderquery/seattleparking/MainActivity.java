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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import com.github.genderquery.arcgis.service.MapService;
import com.github.genderquery.arcgis.model.MapServiceInfo;
import com.github.genderquery.seattleparking.util.CoordinateProjector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlayOptions;
import java.io.IOException;
import retrofit2.HttpException;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
    OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener {

  private static final String TAG = "MainActivity";
  private static final float MIN_ZOOM = 11;

  private GoogleMap googleMap;
  private CoordinateProjector coordinateProjector;
  private MapService mapService;
  private Projection mapProjection;
  private SupportMapFragment mapFragment;
  private int dpi;
  private MapServiceInfo mapServiceInfo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    mapService = SdotRestClient.getClient().getService(MapService.class);
    dpi = calculateScreenDpi();
  }

  private int calculateScreenDpi() {
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    return (int) ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0);
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    this.googleMap = googleMap;
    googleMap.setMinZoomPreference(MIN_ZOOM);
    googleMap.setOnMapLoadedCallback(this);
    googleMap.setOnCameraMoveListener(this);
    googleMap.setOnCameraIdleListener(this);
  }

  @Override
  public void onMapLoaded() {
    final Context context = this;
    // TODO clean this up
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (mapServiceInfo == null) {
          getMapServiceInfo();
        }
        CoordinateProjector coordinateProjector = getCoordinateProjector();
        final LayerTileProvider layerTileProvider =
            new LayerTileProvider(context, coordinateProjector, 7);
        final LatLngBounds fullExtent =
            coordinateProjector.inverseProject(mapServiceInfo.fullExtent);
        final LatLngBounds initialExtent =
            coordinateProjector.inverseProject(mapServiceInfo.initialExtent);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            googleMap.setLatLngBoundsForCameraTarget(fullExtent);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(initialExtent, 0));
            googleMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(layerTileProvider));
          }
        });
      }
    }).start();
  }

  @WorkerThread
  private MapServiceInfo getMapServiceInfo() {
    if (mapServiceInfo == null) {
      try {
        Response<MapServiceInfo> response = mapService.serviceInfo().execute();
        if (!response.isSuccessful()) {
          throw new HttpException(response);
        }
        mapServiceInfo = response.body();
      } catch (IOException | HttpException e) {
        Log.e(TAG, Log.getStackTraceString(e));
      }
    }
    return mapServiceInfo;
  }

  @WorkerThread
  private CoordinateProjector getCoordinateProjector() {
    if (coordinateProjector == null) {
      MapServiceInfo mapServiceInfo = getMapServiceInfo();
      coordinateProjector = new CoordinateProjector(mapServiceInfo.spatialReference);
    }
    return coordinateProjector;
  }

  @Override
  public void onCameraIdle() {
    CameraPosition cameraPosition = googleMap.getCameraPosition();
    Log.v(TAG, cameraPosition.toString());
  }

  @Override
  public void onCameraMove() {
    CameraPosition cameraPosition = googleMap.getCameraPosition();
    // addresses a bug where the zoom goes inverseProject NaN
    if (Float.isNaN(cameraPosition.zoom)) {
      googleMap.moveCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
    }
  }
}
