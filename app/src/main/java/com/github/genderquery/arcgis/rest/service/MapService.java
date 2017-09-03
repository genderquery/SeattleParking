package com.github.genderquery.arcgis.rest.service;

import android.graphics.Bitmap;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.ExportLayers;
import com.github.genderquery.arcgis.rest.model.ImageFormat;
import com.github.genderquery.arcgis.rest.model.MapServiceInfo;
import com.github.genderquery.arcgis.rest.model.Size;
import com.github.genderquery.arcgis.rest.model.SpatialReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {

    @GET("MapServer?f=json")
    Call<MapServiceInfo> serviceInfo();

    @GET("MapServer/export?f=image")
    Call<ResponseBody> export(
            @Query("bbox") Envelope bounds,
            @Query("size") Size size,
            @Query("dpi") int dpi,
            @Query("format") ImageFormat imageFormat,
            @Query("layers") ExportLayers layers,
            @Query("transparent") boolean transparent
    );
}
