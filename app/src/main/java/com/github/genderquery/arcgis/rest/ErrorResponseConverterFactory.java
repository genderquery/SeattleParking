package com.github.genderquery.arcgis.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.genderquery.arcgis.rest.model.ApiError;
import com.github.genderquery.arcgis.rest.model.ErrorEnvelope;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class ErrorResponseConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type != ApiError.class) {
            return null;
        }
        ParameterizedType errorEnvelopeType = Types.newParameterizedType(ErrorEnvelope.class, type);
        final Converter<ResponseBody, ErrorEnvelope> delegate =
                retrofit.nextResponseBodyConverter(this, errorEnvelopeType, annotations);
        return new Converter<ResponseBody, ApiError>() {
            @Override
            public ApiError convert(@NonNull ResponseBody value) throws IOException {
                return delegate.convert(value).error;
            }
        };
    }
}
