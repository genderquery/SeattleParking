package com.github.genderquery.arcgis.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.ExportLayers;
import com.github.genderquery.arcgis.rest.model.Size;
import com.github.genderquery.arcgis.rest.model.SpatialReference;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

import retrofit2.Converter;
import retrofit2.Retrofit;

class ArcGisStringConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<?, String> stringConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == Envelope.class) return new EnvelopeConverter();
        if (type == Size.class) return new SizeConverter();
        if (type == SpatialReference.class) return new SpatialReferenceConverter();
        if (type == ExportLayers.class) return new ExportLayersConverter();
        else return null;
    }

    private static class EnvelopeConverter implements Converter<Envelope, String> {
        @Override
        public String convert(@NonNull Envelope value) throws IOException {
            return String.format(Locale.US,
                    "%f,%f,%f,%f", value.xmin, value.ymin, value.xmax, value.ymax);
        }
    }

    private static class SizeConverter implements Converter<Size, String> {
        @Override
        public String convert(@NonNull Size value) throws IOException {
            return String.format(Locale.US, "%d,%d", value.width, value.height);
        }
    }

    private class SpatialReferenceConverter implements Converter<SpatialReference, String> {
        @Override
        public String convert(@NonNull SpatialReference value) throws IOException {
            return String.format(Locale.US, "%d", value.wkid);
        }
    }

    private class ExportLayersConverter implements Converter<ExportLayers, String> {
        @Override
        public String convert(@NonNull ExportLayers value) throws IOException {
            if (value.layers.length == 0) {
                return value.option;
            }
            StringBuilder sb = new StringBuilder(value.option);
            for (int i = 0; i < value.layers.length; i++) {
                if (i > 0) sb.append(',');
                sb.append(value.layers[i]);
            }
            return sb.toString();
        }
    }
}
