package com.myapps.growdiary.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Plant implements Serializable {
    private int id;
    private String image;
    private String title;
    private String status;

    private String plantingDate;
    private String wateringFrequency;

    private String observationDate;
    private String observationLocation;

    private String notes;

    private String lastUpdate;

    public enum DocumentationType {
        HOME_GARDENER,
        WILD_DISCOVERY
    }
    private DocumentationType type;

    public Plant(){};

    public Plant(String title, String status, DocumentationType type) {
        this.id = MSPV.getMe().getNextID();
        this.title = title;
        this.status = status;
        this.type = type;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public Plant setImage(String image) {
        this.image = image;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Plant setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Plant setStatus(String status) {
        this.status = status;
        return this;
    }

    public DocumentationType getType() {
        return type;
    }

    public Plant setType(DocumentationType type) {
        this.type = type;
        return this;
    }
    public String getPlantingDate() {
        return plantingDate;
    }

    public Plant setPlantingDate(String plantingDate) {
        this.plantingDate = plantingDate;
        return this;
    }

    public String getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(String wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public String getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(String observationDate) {
        this.observationDate = observationDate;
    }

    public String getObservationLocation() {
        return observationLocation;
    }

    public void setObservationLocation(String observationLocation) {
        this.observationLocation = observationLocation;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id); // Compare based on unique identifier, such as id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use unique identifier for hashing, such as id
    }
}
