package com.github.genderquery.seattleparking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.ExportLayers;
import com.github.genderquery.arcgis.rest.model.ImageFormat;
import com.github.genderquery.arcgis.rest.model.Size;
import com.github.genderquery.arcgis.rest.service.MapService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ExportMapTileProvider implements TileProvider {

    private static final String TAG = "ExportMapTileProvider";
    private final MapService mapService;
    private final CoordinateProjector coordinateProjector;

    public ExportMapTileProvider(@NonNull MapService mapService,
                                 @NonNull CoordinateProjector coordinateProjector) {
        this.mapService = mapService;
        this.coordinateProjector = coordinateProjector;
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {

        Log.d(TAG, "( " + x + ", " + y + ", " + zoom + " )");

        int width = 512;
        int height = 512;

        LatLngBounds latLngBounds = null;
        try {
            latLngBounds = latLngBoundsFromTileCoordinate(x, y, zoom);
        } catch (Throwable t) {
            Log.e(TAG, Log.getStackTraceString(t));
        }
        Log.d(TAG, latLngBounds.toString());
        Envelope envelope = coordinateProjector.from(latLngBounds);
        Response<ResponseBody> response;
        try {
            response = mapService.export(envelope, new Size(width, height), 96,
                    ImageFormat.PNG8,
                    new ExportLayers("show", new int[]{4, 5, 6, 7, 8}), true)
                    .execute();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
        if (!response.isSuccessful()) {
            Log.e(TAG, "Unexpected response " + response);
            return NO_TILE;
        }
        ResponseBody body = response.body();
        if (body == null) {
            Log.e(TAG, "Empty body for " + response);
            return NO_TILE;
        }
        byte[] bytes;
        try {
            bytes = body.bytes();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return NO_TILE;
        }
        return new Tile(width, height, bytes);
    }

    private LatLngBounds latLngBoundsFromTileCoordinate(int x, int y, int zoom) {
        return new LatLngBounds(latLngFromTileCoordinate(x, y + 1, zoom),
                latLngFromTileCoordinate(x + 1, y, zoom));
    }

    // http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Tile_numbers_to_lon..2Flat.
    private LatLng latLngFromTileCoordinate(int x, int y, int zoom) {
        double n = Math.pow(2, zoom);
        double lon_deg = x / n * 360.0 - 180.0;
        double lat_rad = Math.atan(Math.sinh(Math.PI * (1 - 2 * y / n)));
        double lat_deg = lat_rad * 180.0 / Math.PI;
        return new LatLng(lat_deg, lon_deg);
    }
}
