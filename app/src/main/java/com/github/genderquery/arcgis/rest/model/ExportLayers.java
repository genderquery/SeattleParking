package com.github.genderquery.arcgis.rest.model;

// TODO make a string converter
public class ExportLayers {
    //TODO enum?
    public String option;
    public int[] layers;

    public ExportLayers(String option, int[] layers) {
        this.option = option;
        this.layers = layers;
    }
}
