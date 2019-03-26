package com.cosmicode.roomie.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RoomExpenseSplitRecord entity.
 */
public class RoomExpenseSplitRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(min = 4, max = 25)
    private String state;


    private Long splitId;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getSplitId() {
        return splitId;
    }

    public void setSplitId(Long roomExpenseSplitId) {
        this.splitId = roomExpenseSplitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomExpenseSplitRecordDTO roomExpenseSplitRecordDTO = (RoomExpenseSplitRecordDTO) o;
        if (roomExpenseSplitRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpenseSplitRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpenseSplitRecordDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", state='" + getState() + "'" +
            ", split=" + getSplitId() +
            "}";
    }
}
