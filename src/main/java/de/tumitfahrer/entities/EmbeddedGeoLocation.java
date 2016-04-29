package de.tumitfahrer.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EmbeddedGeoLocation {

    @NotNull
    @Column(name = "LATITUDE", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "LONGITUDE", nullable = false)
    private Double longitude;


    public EmbeddedGeoLocation() {
        this(null, null);
    }

    public EmbeddedGeoLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
