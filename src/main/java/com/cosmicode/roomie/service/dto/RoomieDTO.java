package com.cosmicode.roomie.service.dto;

import com.cosmicode.roomie.domain.enumeration.Gender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Roomie entity.
 */
public class RoomieDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate birthDate;

    @Size(min = 4, max = 750)
    private String biography;

    @NotNull
    private String picture;

    @NotNull
    private Gender gender;

    @Size(min = 4, max = 25)
    @Pattern(regexp = "^(1[ \\-\\+]{0,3}|\\+1[ -\\+]{0,3}|\\+1|\\+)?((\\(\\+?1-[2-9][0-9]{1,2}\\))|(\\(\\+?[2-8][0-9][0-9]\\))|(\\(\\+?[1-9][0-9]\\))|(\\(\\+?[17]\\))|(\\([2-9][2-9]\\))|([ \\-\\.]{0,3}[0-9]{2,4}))?([ \\-\\.][0-9])?([ \\-\\.]{0,3}[0-9]{2,4}){2,3}$")
    private String phone;

    @NotNull
    @Size(min = 0, max = 200)
    private String mobileDeviceID;


    private Long userId;

    private UserDTO user;

    private Long stateId;

    private Long addressId;

    private Long configurationId;

    private Set<RoomFeatureDTO> lifestyles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileDeviceID() {
        return mobileDeviceID;
    }

    public void setMobileDeviceID(String mobileDeviceID) {
        this.mobileDeviceID = mobileDeviceID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long roomieStateId) {
        this.stateId = roomieStateId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long userPreferencesId) {
        this.configurationId = userPreferencesId;
    }

    public Set<RoomFeatureDTO> getLifestyles() {
        return lifestyles;
    }

    public void setLifestyles(Set<RoomFeatureDTO> roomFeatures) {
        this.lifestyles = roomFeatures;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomieDTO roomieDTO = (RoomieDTO) o;
        if (roomieDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomieDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomieDTO{" +
            "id=" + getId() +
            ", birthDate='" + getBirthDate() + "'" +
            ", biography='" + getBiography() + "'" +
            ", picture='" + getPicture() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", mobileDeviceID='" + getMobileDeviceID() + "'" +
            ", user=" + getUserId() +
            ", state=" + getStateId() +
            ", address=" + getAddressId() +
            ", configuration=" + getConfigurationId() +
            "}";
    }
}
