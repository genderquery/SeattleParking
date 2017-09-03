package com.github.genderquery.seattleparking;

import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.genderquery.arcgis.rest.model.MapServiceInfo;
import com.github.genderquery.arcgis.rest.service.MapService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
        mapService = SdotRestClient.getService(MapService.class);
        dpi = calculateScreenDpi();
    }

    private int calculateScreenDpi() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) ((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        // map doesn't draw correctly when rotated
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
        googleMap.setMinZoomPreference(MIN_ZOOM);
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onMapLoaded() {
        // TODO clean this up
        new Thread(new Runnable() {
            @Override
            public void run() {
                MapServiceInfo mapServiceInfo = getMapServiceInfo();
                CoordinateProjector coordinateProjector = getCoordinateProjector();
                final ExportMapTileProvider exportMapTileProvider =
                        new ExportMapTileProvider(mapService, coordinateProjector);
                final LatLngBounds fullExtent =
                        coordinateProjector.to(mapServiceInfo.fullExtent);
                final LatLngBounds initialExtent =
                        coordinateProjector.to(mapServiceInfo.initialExtent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        googleMap.setLatLngBoundsForCameraTarget(fullExtent);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(initialExtent, 0));
                        googleMap.addTileOverlay(new TileOverlayOptions()
                                .tileProvider(exportMapTileProvider));
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        Log.v(TAG, cameraPosition.toString());
    }

    @Override
    public void onCameraMove() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        // addresses a bug where the zoom goes to NaN
        if (Float.isNaN(cameraPosition.zoom)) {
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
        }
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
}
