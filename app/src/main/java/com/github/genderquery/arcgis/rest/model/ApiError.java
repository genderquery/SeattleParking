package com.github.genderquery.arcgis.rest.model;

public class ApiError extends ApiResponse {
    public int code;
    public String message;
    public String[] details;
}
