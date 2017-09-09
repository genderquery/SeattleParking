package com.github.genderquery.arcgis.rest.service;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.ExportLayers;
import com.github.genderquery.arcgis.rest.model.ImageFormat;
import com.github.genderquery.arcgis.rest.model.MapServiceInfo;
import com.github.genderquery.arcgis.rest.model.QueryResponseBody;
import com.github.genderquery.arcgis.rest.model.Size;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("MapServer/{layer}/query?f=json&geometryType=esriGeometryEnvelope")
    Call<QueryResponseBody> queryGeometry(
            @Path("layer") int layer,
            // TODO @Query("geometry") Geometry geometry
            @Query("geometry") Envelope geometry,
            // TODO @Query("geometryType") GeometryType geometryType
            @Query("outFields") String fields
    );
}
