package com.github.genderquery.arcgis.rest.model;

import java.util.List;

public class Layer {
    public Integer id;
    public String name;
    public Integer parentLayerId;
    public Boolean defaultVisibility;
    public List<Integer> subLayerIds;
    public Integer minScale;
    public Integer maxScale;
}
