package com.github.genderquery.moshi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Converts delimited JSON strings to collection types, including {@link EnumSet}.
 * <p>
 * Use {@literal @Split} to annotate members.
 */
public abstract class SplitCollectionJsonAdapter<C extends Collection<T>, T>
        extends JsonAdapter<C> {

    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {

        @Override
        @Nullable
        public JsonAdapter<?> create(@NonNull Type type,
                                     @NonNull Set<? extends Annotation> annotations,
                                     @NonNull Moshi moshi) {
            Split annotation = (Split) getAnnotation(annotations, Split.class);
            if (annotation == null) return null;
            String delimiter = annotation.delimiter();
            Class<?> rawType = Types.getRawType(type);
            if (rawType == List.class || rawType == Collection.class) {
                return newArrayListAdapter(type, moshi, delimiter).nullSafe();
            } else if (rawType == Set.class) {
                return newLinkedHashSetAdapter(type, moshi, delimiter).nullSafe();
            } else if (rawType == EnumSet.class) {
                //noinspection unchecked
                return newEnumSetAdapter(type, moshi, delimiter).nullSafe();
            }
            return null;
        }
    };

    @Nullable
    private static Annotation getAnnotation(Set<? extends Annotation> annotations,
                                            Class<? extends Annotation> annotationClass) {
        if (annotations.isEmpty()) return null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationClass) return annotation;
        }
        return null;
    }

    private final JsonAdapter<T> elementAdapter;
    private final String delimiter;

    private SplitCollectionJsonAdapter(JsonAdapter<T> elementAdapter, String delimiter) {
        this.elementAdapter = elementAdapter;
        this.delimiter = delimiter;
    }

    private static <T> JsonAdapter<Collection<T>> newArrayListAdapter(
            @NonNull Type type, @NonNull Moshi moshi, @NonNull String delimiter) {
        Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new SplitCollectionJsonAdapter<Collection<T>, T>(elementAdapter, delimiter) {
            @Override
            Collection<T> newCollection() {
                return new ArrayList<>();
            }
        };
    }

    private static <T> JsonAdapter<Set<T>> newLinkedHashSetAdapter(
            @NonNull Type type, @NonNull Moshi moshi, @NonNull String delimiter) {
        Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new SplitCollectionJsonAdapter<Set<T>, T>(elementAdapter, delimiter) {
            @Override
            Set<T> newCollection() {
                return new LinkedHashSet<>();
            }
        };
    }

    private static <T extends Enum<T>> JsonAdapter<EnumSet<T>> newEnumSetAdapter(
            @NonNull final Type type, @NonNull Moshi moshi, @NonNull String delimiter) {
        final Type elementType = Types.collectionElementType(type, Collection.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new SplitCollectionJsonAdapter<EnumSet<T>, T>(elementAdapter, delimiter) {
            @Override
            EnumSet<T> newCollection() {
                //noinspection unchecked
                return EnumSet.noneOf((Class<T>) elementType);
            }
        };
    }

    abstract C newCollection();

    @Override
    public C fromJson(@NonNull JsonReader reader) throws IOException {
        C result = newCollection();
        String[] values = reader.nextString().split(delimiter);
        for (String value : values) {
            result.add(elementAdapter.fromJsonValue(value));
        }
        return result;
    }

    @Override
    public void toJson(@NonNull JsonWriter writer, C values) throws IOException {
        StringBuilder result = new StringBuilder();
        Iterator<T> iterator = values.iterator();
        if (iterator.hasNext()) {
            result.append(iterator.next());
            while (iterator.hasNext()) {
                result.append(delimiter);
                result.append(elementAdapter.toJsonValue(iterator.next()));
            }
        }
        writer.value(result.toString());
    }

    @Override
    public String toString() {
        return elementAdapter + ".collection()";
    }
}
