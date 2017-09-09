package com.github.genderquery.arcgis.rest;

import com.github.genderquery.moshi.ArcGisJsonAdapterFactory;
import com.github.genderquery.moshi.SplitCollectionJsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ArcGisRestClient {

    private static final String TAG = "ArcGisRestClient";
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private Retrofit retrofit;
    private HashMap<Class, Object> services = new HashMap<>();

    public ArcGisRestClient(String url) {
        this(url, new OkHttpClient.Builder().build());
    }

    public ArcGisRestClient(String url, OkHttpClient httpClient) {
        this.httpClient = httpClient;
        moshi = new Moshi.Builder()
                .add(SplitCollectionJsonAdapter.FACTORY)
                .add(new ArcGisJsonAdapterFactory())
                .build();
        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addConverterFactory(new ArcGisStringConverterFactory())
                .addConverterFactory(new ErrorResponseConverterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public Moshi getMoshi() {
        return moshi;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        if (!services.containsKey(serviceClass)) {
            T service = retrofit.create(serviceClass);
            services.put(serviceClass, service);
            return service;
        } else {
            return (T) services.get(serviceClass);
        }
    }
}
