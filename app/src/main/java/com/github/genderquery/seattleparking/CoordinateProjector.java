package com.github.genderquery.seattleparking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.genderquery.arcgis.rest.model.Envelope;
import com.github.genderquery.arcgis.rest.model.Point;
import com.github.genderquery.arcgis.rest.model.SpatialReference;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.UnknownAuthorityCodeException;
import org.osgeo.proj4j.io.Proj4FileReader;

import java.io.IOException;

// TODO instance cache?
public class CoordinateProjector {

    private static final String TAG = "CoordinateProjector";

    private final org.osgeo.proj4j.proj.Projection projection;

    public CoordinateProjector(@NonNull SpatialReference spatialReference) {
        this("ESRI", String.valueOf(spatialReference.wkid));
    }

    public CoordinateProjector(@NonNull String authority, @NonNull String id) {
        String name = authority + ":" + id;
        Proj4FileReader proj4FileReader = new Proj4FileReader();
        String[] params = new String[0];
        // TODO put the relevant parameters into a string literal?
        try {
            params = proj4FileReader.readParametersFromFile(authority, id);
        } catch (IOException e) {
            Log.wtf(TAG, Log.getStackTraceString(e));
        }
        if (params == null)
            throw new UnknownAuthorityCodeException(name);
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem mCoordinateReferenceSystem =
                crsFactory.createFromParameters(name, params);
        projection = mCoordinateReferenceSystem.getProjection();
    }

    public LatLngBounds to(@NonNull Envelope envelope) {
        return to(envelope.xmin, envelope.ymin, envelope.xmax, envelope.ymax);
    }

    public LatLngBounds to(double xmin, double ymin, double xmax, double ymax) {
        return new LatLngBounds(to(xmin, ymin), to(xmax, ymax));
    }

    public LatLng to(double x, double y) {
        ProjCoordinate src = new ProjCoordinate(x, y);
        ProjCoordinate dst = new ProjCoordinate();
        projection.inverseProject(src, dst);
        return new LatLng(dst.y, dst.x);
    }

    public Envelope from(LatLngBounds latLngBounds) {
        return new Envelope(from(latLngBounds.southwest), from(latLngBounds.northeast));
    }

    public Point from(LatLng latLng) {
        ProjCoordinate src = new ProjCoordinate(latLng.longitude, latLng.latitude);
        ProjCoordinate dst = new ProjCoordinate();
        projection.project(src, dst);
        return new Point(dst.x, dst.y);
    }
}
