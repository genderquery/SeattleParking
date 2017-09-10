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

package com.github.genderquery.arcgis;

import android.support.annotation.NonNull;
import com.github.genderquery.arcgis.moshi.ArcGisJsonAdapterFactory;
import com.github.genderquery.arcgis.retrofit.ArcGisStringConverterFactory;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ArcGisRestClient {

  private final OkHttpClient httpClient;
  private final Moshi moshi;
  private Retrofit retrofit;
  private HashMap<Class, Object> services = new HashMap<>();

  public ArcGisRestClient(@NonNull String url) {
    this(url, new OkHttpClient.Builder().build());
  }

  public ArcGisRestClient(@NonNull String url, @NonNull OkHttpClient httpClient) {
    this.httpClient = httpClient;
    moshi = new Moshi.Builder()
        .add(new ArcGisJsonAdapterFactory())
        .build();
    retrofit = new Retrofit.Builder()
        .client(httpClient)
        .baseUrl(url)
        .addConverterFactory(new ArcGisStringConverterFactory())
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
