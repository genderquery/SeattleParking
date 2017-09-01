package com.github.genderquery.arcgis.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class BitmapConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, Bitmap> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = Types.getRawType(type);
        if (Bitmap.class.isAssignableFrom(rawType)) {
            return new BitmapConverter();
        }
        return null;
    }

    private class BitmapConverter implements Converter<ResponseBody, Bitmap> {

        @Override
        public Bitmap convert(@NonNull ResponseBody value) throws IOException {
            return BitmapFactory.decodeStream(value.byteStream());
        }
    }
}
