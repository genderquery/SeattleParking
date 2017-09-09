package com.github.genderquery.seattleparking;

import com.squareup.moshi.Json;

public enum ParkingCategory {
    @Json(name = "Carpool Parking")CARPOOL_PARKING,
    @Json(name = "No Parking Allowed")NO_PARKING_ALLOWED,
    @Json(name = "Paid Parking")PAID_PARKING,
    @Json(name = "Restricted Parking Zone")RESTRICTED_PARKING_ZONE,
    @Json(name = "Time Limited Parking")TIME_LIMITED_PARKING,
    @Json(name = "Unrestricted Parking")UNRESTRICTED_PARKING,
}
