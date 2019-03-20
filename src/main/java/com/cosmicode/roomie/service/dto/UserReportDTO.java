package com.cosmicode.roomie.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.cosmicode.roomie.domain.enumeration.ReportType;

/**
 * A DTO for the UserReport entity.
 */
public class UserReportDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(min = 4, max = 500)
    private String description;

    private ReportType type;

    private Long roomieId;

    private Long roomId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Long getRoomieId() {
        return roomieId;
    }

    public void setRoomieId(Long roomieId) {
        this.roomieId = roomieId;
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

        UserReportDTO userReportDTO = (UserReportDTO) o;
        if (userReportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userReportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserReportDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", roomie=" + getRoomieId() +
            ", room=" + getRoomId() +
            "}";
    }
}
