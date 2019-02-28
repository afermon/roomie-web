package com.cosmicode.roomie.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.cosmicode.roomie.domain.enumeration.RoomState;
import com.cosmicode.roomie.domain.enumeration.RoomType;

/**
 * A DTO for the Room entity.
 */
public class RoomDTO implements Serializable {

    private Long id;

    @NotNull
    private RoomState state;

    @NotNull
    private Instant created;

    @NotNull
    private Instant published;

    @NotNull
    @Size(min = 4, max = 100)
    private String title;

    @Size(min = 4, max = 2000)
    private String description;

    @NotNull
    private Integer rooms;

    @NotNull
    private RoomType roomType;

    @Size(min = 4, max = 200)
    private String apoinmentsNotes;

    @NotNull
    private Boolean lookingForRoomie;

    @NotNull
    private LocalDate availableFrom;

    @NotNull
    private Boolean isPremium;

    private Long addressId;

    private Set<RoomieDTO> roomies = new HashSet<>();

    private Set<RoomFeatureDTO> features = new HashSet<>();

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomState getState() {
        return state;
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getPublished() {
        return published;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getApoinmentsNotes() {
        return apoinmentsNotes;
    }

    public void setApoinmentsNotes(String apoinmentsNotes) {
        this.apoinmentsNotes = apoinmentsNotes;
    }

    public Boolean isLookingForRoomie() {
        return lookingForRoomie;
    }

    public void setLookingForRoomie(Boolean lookingForRoomie) {
        this.lookingForRoomie = lookingForRoomie;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Boolean isIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Set<RoomieDTO> getRoomies() {
        return roomies;
    }

    public void setRoomies(Set<RoomieDTO> roomies) {
        this.roomies = roomies;
    }

    public Set<RoomFeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(Set<RoomFeatureDTO> roomFeatures) {
        this.features = roomFeatures;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long roomieId) {
        this.ownerId = roomieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;
        if (roomDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", created='" + getCreated() + "'" +
            ", published='" + getPublished() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", rooms=" + getRooms() +
            ", roomType='" + getRoomType() + "'" +
            ", apoinmentsNotes='" + getApoinmentsNotes() + "'" +
            ", lookingForRoomie='" + isLookingForRoomie() + "'" +
            ", availableFrom='" + getAvailableFrom() + "'" +
            ", isPremium='" + isIsPremium() + "'" +
            ", address=" + getAddressId() +
            ", owner=" + getOwnerId() +
            "}";
    }
}
