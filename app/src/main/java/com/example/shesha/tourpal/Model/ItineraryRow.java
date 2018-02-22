package com.example.shesha.tourpal.Model;

/**
 * Created by Shesha on 29-01-2018.
 */

public class ItineraryRow {
    private String PlaceName;

    public ItineraryRow(String placeName) {
        PlaceName = placeName;
    }

    public ItineraryRow() {
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }
}
