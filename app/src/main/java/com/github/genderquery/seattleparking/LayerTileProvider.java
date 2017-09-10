package com.github.genderquery.seattleparking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import com.github.genderquery.seattleparking.arcgis.model.Envelope;
import com.github.genderquery.seattleparking.arcgis.model.Feature;
import com.github.genderquery.seattleparking.arcgis.model.GeometryType;
import com.github.genderquery.seattleparking.arcgis.model.Point;
import com.github.genderquery.seattleparking.arcgis.model.Polyline;
import com.github.genderquery.seattleparking.arcgis.model.QueryResponseBody;
import com.github.genderquery.seattleparking.arcgis.model.SpatialRelationship;
import com.github.genderquery.seattleparking.arcgis.service.MapService;
import com.github.genderquery.seattleparking.model.ParkingCategory;
import com.github.genderquery.seattleparking.util.CoordinateProjector;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import retrofit2.HttpException;
import retrofit2.Response;

public class LayerTileProvider implements TileProvider {

  private static final String TAG = "LayerTileProvider";

  // TODO make these runtime configurable
  private static final float TILE_SIZE = 256;
  private static final int ZOOM_MIN = 16;

  private final CoordinateProjector coordinateProjector;
  private final DisplayMetrics displayMetrics;
  private final int layer;
  private final int tileWidth;
  private final int tileHeight;
  private final Paint linePaint;
  private final SdotRestClient sdotClient;
  private final MapService mapService;
  private final Moshi moshi;
  private final JsonAdapter<ParkingCategory> parkingCategoryJsonAdapter;

  // TODO too many dependencies? at least get them out of the constructor
  public LayerTileProvider(@NonNull Context context,
      @NonNull CoordinateProjector coordinateProjector, int layer) {
    this.coordinateProjector = coordinateProjector;
    displayMetrics = context.getResources().getDisplayMetrics();
    this.layer = layer;
    tileWidth = (int) (TILE_SIZE * displayMetrics.density);
    tileHeight = (int) (TILE_SIZE * displayMetrics.density);

    linePaint = new Paint();
    linePaint.setAntiAlias(true);
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setStrokeCap(Paint.Cap.BUTT);
    linePaint.setColor(Color.GRAY);

    sdotClient = SdotRestClient.getClient();
    mapService = sdotClient.getService(MapService.class);
    moshi = sdotClient.getMoshi();
    parkingCategoryJsonAdapter = moshi.adapter(ParkingCategory.class);
  }

  @Override
  public Tile getTile(int x, int y, int zoom) {
    if (zoom < ZOOM_MIN) {
      return NO_TILE;
    }

    LatLngBounds latLngBounds = latLngBoundsFromTileCoordinate(x, y, zoom);
    Envelope envelope = coordinateProjector.project(latLngBounds);
    // TODO better encapsulation
    QueryResponseBody body;
    try {
      // TODO can't hardcode the fields
      Response<QueryResponseBody> response =
          mapService
              .queryGeometry(layer, envelope, GeometryType.ENVELOPE, SpatialRelationship.INTERSECTS,
                  "PARKING_CATEGORY")
              .execute();
      if (!response.isSuccessful()) {
        throw new HttpException(response);
      }
      body = response.body();
      if (body == null) {
        throw new IOException("unexpected empty body");
      }
    } catch (HttpException e) {
      Log.e(TAG, Log.getStackTraceString(e));
      return NO_TILE;
    } catch (SocketTimeoutException e) {
      Log.e(TAG, Log.getStackTraceString(e));
      return null; // try again
    } catch (IOException e) {
      Log.e(TAG, Log.getStackTraceString(e));
      return NO_TILE;
    }
    Bitmap bitmap = Bitmap.createBitmap(tileWidth, tileHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);

    linePaint.setStrokeWidth(zoom / 5f * displayMetrics.density);

    for (Feature feature : body.features) {
      Map<String, String> attributes = feature.attributes;
      ParkingCategory parkingCategory =
          parkingCategoryJsonAdapter.fromJsonValue(attributes.get("PARKING_CATEGORY"));
      Symbol symbol = sdotClient.parkingCategorySymbols.get(parkingCategory);
      linePaint.setColor(symbol.color);
      linePaint.setPathEffect(symbol.pathEffect);
      linePaint.setAlpha(0xff);
      Polyline geometry = (Polyline) feature.geometry;
      List<List<Point>> lines = geometry.getLines();
      for (List<Point> line : lines) {
        Path path = new Path();
        for (int i = 0; i < line.size(); i++) {
          Point point = line.get(i);
          LatLng latLng = coordinateProjector.inverseProject(new Point(point.x, point.y));
          PointF pointF = pointForLatLng(latLng.latitude, latLng.longitude, zoom);
          // translation must be done here because translating the entire canvas
          // produces odd results
          pointF.offset(-x * tileWidth, -y * tileWidth);
          if (i == 0) {
            path.moveTo(pointF.x, pointF.y);
          } else {
            path.lineTo(pointF.x, pointF.y);
          }
        }
        // drawPath is faster than drawLine or drawLines
        canvas.drawPath(path, linePaint);
      }
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    byte[] bytes = byteArrayOutputStream.toByteArray();

    return new Tile(tileWidth, tileHeight, bytes);
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

  /**
   * Returns world pixel coordinates from a given latitude, longitude, and zoom level
   * https://en.wikipedia.org/wiki/Web_Mercator#Formulas
   */
  private PointF pointForLatLng(double lat, double lng, int zoom) {
    double lng_rad = Math.toRadians(lng);
    double lat_rad = Math.toRadians(lat);
    double n = (tileWidth / 2.0 / Math.PI) * Math.pow(2.0, zoom);
    double x = n * (lng_rad + Math.PI);
    double y = n * (Math.PI - Math.log(Math.tan(Math.PI / 4.0 + lat_rad / 2.0)));
    return new PointF((float) x, (float) y);
  }
}
