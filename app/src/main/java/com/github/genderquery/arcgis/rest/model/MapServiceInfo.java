package com.github.genderquery.arcgis.rest.model;

import com.github.genderquery.moshi.Split;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class MapServiceInfo extends ApiResponse {
    public String currentVersion;
    public String serviceDescription;
    public String mapName;
    public String description;
    public String copyrightText;
    public List<Layer> layers;
    public List<Table> tables;
    public SpatialReference spatialReference;
    public Boolean singleFusedMapCache;
    public Envelope initialExtent;
    public Envelope fullExtent;
    public Unit units;
    @Split(delimiter = ",") public EnumSet<ImageFormat> supportedImageFormatTypes;
    public Map<String,String> documentInfo;
    @Split(delimiter = ",") public EnumSet<Capability> capabilities;
}
