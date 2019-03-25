package com.cosmicode.roomie.service.dto;

import com.cosmicode.roomie.domain.RoomFeature;
import com.cosmicode.roomie.domain.enumeration.CurrencyType;

import java.util.List;

public class SearchFilterDTO {

    private String query;

    private Double latitude;

    private Double longitude;

    private int distance;

    private String city;

    private String state;

    private CurrencyType currency;

    private int priceMin;

    private int priceMax;

    private List<RoomFeature> features = null;

    public SearchFilterDTO() {
    }

    public SearchFilterDTO(String query, int distance, CurrencyType currency, int priceMin, int priceMax, List<RoomFeature> features) {
        this.query = query;
        this.distance = distance;
        this.currency = currency;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.features = features;
    }

    public SearchFilterDTO(String query, Double latitude, Double longitude, int distance, CurrencyType currency, int priceMin, int priceMax, List<RoomFeature> features) {
        this.query = query;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.currency = currency;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.features = features;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public List<RoomFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<RoomFeature> features) {
        this.features = features;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
            "query='" + query + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", distance=" + distance +
            ", currency=" + currency +
            ", priceMin=" + priceMin +
            ", priceMax=" + priceMax +
            ", features=" + features +
            '}';
    }
}
