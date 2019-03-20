package com.cosmicode.roomie.service.dto;

import com.cosmicode.roomie.domain.enumeration.AccountState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RoomieState entity.
 */
public class RoomieStateDTO implements Serializable {

    private Long id;

    @NotNull
    private AccountState state;

    private LocalDate suspendedDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public LocalDate getSuspendedDate() {
        return suspendedDate;
    }

    public void setSuspendedDate(LocalDate suspendedDate) {
        this.suspendedDate = suspendedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomieStateDTO roomieStateDTO = (RoomieStateDTO) o;
        if (roomieStateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomieStateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomieStateDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", suspendedDate='" + getSuspendedDate() + "'" +
            "}";
    }
}
