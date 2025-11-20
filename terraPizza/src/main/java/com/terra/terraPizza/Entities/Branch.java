package com.terra.terraPizza.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;       // Adana
    private String district;   // Karaisalı
    private String neighborhood; // Karaköy Mh.

    private String name;       // Sarıçam Çarkıpare
    private boolean open;      // true/false
    private String hours;      // "11:00 - 22:15"

    private Double latitude;
    private Double longitude;

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public String getHours() {
        return hours;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
