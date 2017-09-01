package com.github.genderquery.seattleparking;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.ExportLayers;
import com.github.genderquery.arcgis.rest.model.ImageFormat;
import com.github.genderquery.arcgis.rest.model.MapServiceInfo;
import com.github.genderquery.arcgis.rest.model.Size;
import com.github.genderquery.arcgis.rest.model.SpatialReference;
import com.github.genderquery.arcgis.rest.service.MapService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {

    private static final String TAG = "MainActivity";
    private static final float MIN_ZOOM = 11;

    private GoogleMap googleMap;
    private CoordinateProjector coordinateProjector;
    private LatLngBounds fullExtent;
    private MapService mapService;
    private LatLngBounds initialExtent;
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings uiSettings = this.googleMap.getUiSettings();
        // map doesn't draw correctly when rotated
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setCompassEnabled(false);
        this.googleMap.setMinZoomPreference(MIN_ZOOM);
        this.googleMap.setOnMapLoadedCallback(this);
        this.googleMap.setOnCameraMoveListener(this);
        this.googleMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onMapLoaded() {
        getMapServiceInfo();
    }

    private void getMapServiceInfo() {
        if (mapServiceInfo != null) {
            // a successful call was already made
            return;
        }
        mapService.serviceInfo().enqueue(new Callback<MapServiceInfo>() {
            @Override
            public void onResponse(@NonNull Call<MapServiceInfo> call,
                                   @NonNull Response<MapServiceInfo> response) {
                handleMapServiceInfoResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<MapServiceInfo> call, @NonNull Throwable t) {
                handleResponseFailure(call, t);
            }
        });
    }

    private void handleMapServiceInfoResponse(@NonNull Call<MapServiceInfo> call,
                                              @NonNull Response<MapServiceInfo> response) {
        if (!response.isSuccessful()) {
            Log.e(TAG, "Unexpected response " + response);
            //TODO do something with response.errorBody()?
            return;
        }
        MapServiceInfo body = response.body();
        if (body == null) {
            Log.e(TAG, "Empty body for response " + response);
            return;
        }
        mapServiceInfo = body;
        if (coordinateProjector == null) {
            coordinateProjector = new CoordinateProjector(mapServiceInfo.spatialReference);
        }
        fullExtent = coordinateProjector.to(mapServiceInfo.fullExtent);
        initialExtent = coordinateProjector.to(mapServiceInfo.initialExtent);
        Log.i(TAG, "fullExtent " + fullExtent.toString());
        Log.i(TAG, "initialExtent " + initialExtent.toString());
        googleMap.setLatLngBoundsForCameraTarget(fullExtent);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(initialExtent, 0));
    }

    private void handleResponseFailure(@NonNull Call call, @NonNull Throwable t) {
        // TODO add an interceptor or handler for this?
        Log.e(TAG, Log.getStackTraceString(t));
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        Log.v(TAG, cameraPosition.toString());
        updateGroundLayer();
    }

    private void updateGroundLayer() {
        if (coordinateProjector == null) {
            getMapServiceInfo();
            return;
        }
        View mapView = mapFragment.getView();
        if (mapView == null) {
            // layout hasn't occurred yet
            return;
        }
        mapProjection = googleMap.getProjection();
        VisibleRegion visibleRegion = mapProjection.getVisibleRegion();
        Envelope bounds = coordinateProjector.from(visibleRegion.latLngBounds);
        Size size = new Size(mapView.getWidth(), mapView.getHeight());
        SpatialReference spatialReference = SdotRestClient.WGS_1984_WEB_MERCATOR_AUXILIARY_SPHERE;
        ExportLayers layers = new ExportLayers("show", new int[]{
                SdotRestClient.LAYER_STREET_PARKING_BY_CATEGORY
        });
        mapService.export(bounds, size, dpi, ImageFormat.PNG8, layers, true)
                .enqueue(new Callback<Bitmap>() {
                    @Override
                    public void onResponse(@NonNull Call<Bitmap> call,
                                           @NonNull Response<Bitmap> response) {
                        handleExportResponse(call, response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Bitmap> call, @NonNull Throwable t) {
                        handleResponseFailure(call, t);
                    }
                });
    }

    private void handleExportResponse(@NonNull Call<Bitmap> call,
                                      @NonNull Response<Bitmap> response) {
        if (!response.isSuccessful()) {
            Log.e(TAG, "Unexpected response " + response);
            //TODO do something with response.errorBody()?
            return;
        }
        Bitmap body = response.body();
        if (body == null) {
            Log.e(TAG, "Empty body for response " + response);
            return;
        }
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(body);
        googleMap.clear();
        googleMap.addGroundOverlay(new GroundOverlayOptions()
                .image(bitmapDescriptor)
                .positionFromBounds(mapProjection.getVisibleRegion().latLngBounds));
    }

    @Override
    public void onCameraMove() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        // addresses a bug where the zoom goes to NaN
        if (Float.isNaN(cameraPosition.zoom)) {
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
        }
    }
}
