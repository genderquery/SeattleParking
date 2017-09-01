package com.github.genderquery.seattleparking;

import com.github.genderquery.arcgis.rest.ArcGisRestClient;
import com.github.genderquery.arcgis.rest.model.SpatialReference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class SdotRestClient {

    public static final SpatialReference WGS_1984_WEB_MERCATOR_AUXILIARY_SPHERE =
            new SpatialReference(3857);
    public static final int LAYER_GARAGES_AND_LOTS = 4;
    public static final int LAYER_STREET_PARKING_SIGNS = 5;
    public static final int LAYER_TEMPORARY_NO_PARKING = 6;
    public static final int LAYER_STREET_PARKING_BY_CATEGORY = 7;
    public static final int LAYER_PEAK_HOUR_NO_PARKING = 8;

    private static final String CATALOG_URL = "http://gisrevprxy.seattle.gov/" +
            "arcgis/rest/services/SDOT_EXT/SDOT_Parking/";
    private static ArcGisRestClient client;

    private SdotRestClient() {
        // prevent instantiation
    }

    public static <T> T getService(Class<T> serviceClass) {
        return getClient().getService(serviceClass);
    }

    public static ArcGisRestClient getClient() {
        if (client == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
            client = new ArcGisRestClient(CATALOG_URL, httpClient);
        }
        return client;
    }
}
