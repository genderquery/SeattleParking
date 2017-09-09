package com.github.genderquery.seattleparking;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.genderquery.arcgis.rest.ArcGisRestClient;
import com.github.genderquery.arcgis.rest.model.SpatialReference;

import java.util.EnumMap;

import okhttp3.OkHttpClient;

public class SdotRestClient extends ArcGisRestClient {

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
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
//                    .addInterceptor(logging)
                    .build();
            client = new SdotRestClient(CATALOG_URL, httpClient);
        }
        return client;
    }
}
