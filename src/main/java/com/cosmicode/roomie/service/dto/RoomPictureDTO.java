package com.cosmicode.roomie.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RoomPicture entity.
 */
public class RoomPictureDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Boolean isMain;

    private Long roomId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomPictureDTO roomPictureDTO = (RoomPictureDTO) o;
        if (roomPictureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomPictureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomPictureDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", isMain='" + isIsMain() + "'" +
            ", room=" + getRoomId() +
            "}";
    }
}
