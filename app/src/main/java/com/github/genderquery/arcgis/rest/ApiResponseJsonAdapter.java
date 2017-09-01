package com.github.genderquery.arcgis.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.genderquery.arcgis.rest.model.ApiResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

class ApiResponseJsonAdapter<T extends ApiResponse> extends JsonAdapter<T> {

    private static final String TAG = "ApiResponseJsonAdapter";

    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {

        @Nullable
        @Override
        public JsonAdapter<?> create(@NonNull Type type,
                                     @NonNull Set<? extends Annotation> annotations,
                                     @NonNull Moshi moshi) {
            Class<?> rawType = Types.getRawType(type);
            if (ApiResponse.class.isAssignableFrom(rawType)) {
                JsonAdapter<?> adapter = moshi.nextAdapter(this, type, annotations);

            }
            return null;
        }
    };

    @Nullable
    @Override
    public T fromJson(@NonNull JsonReader reader) throws IOException {
        return null;
    }

    @Override
    public void toJson(@NonNull JsonWriter writer, T value) throws IOException {

    }
}
