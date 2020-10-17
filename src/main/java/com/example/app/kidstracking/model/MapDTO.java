package com.example.app.kidstracking.model;

public class MapDTO {
    private Double latitude;
    private Double longitude;

    public MapDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MapDTO() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MapDTO{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
