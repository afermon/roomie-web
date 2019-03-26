package com.cosmicode.roomie.service.dto;

import com.cosmicode.roomie.domain.enumeration.CurrencyType;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserPreferences entity.
 */
public class UserPreferencesDTO implements Serializable {

    private Long id;

    private CurrencyType currency;

    private Boolean todoListNotifications;

    private Boolean calendarNotifications;

    private Boolean paymentsNotifications;

    private Boolean appointmentsNotifications;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Boolean isTodoListNotifications() {
        return todoListNotifications;
    }

    public void setTodoListNotifications(Boolean todoListNotifications) {
        this.todoListNotifications = todoListNotifications;
    }

    public Boolean isCalendarNotifications() {
        return calendarNotifications;
    }

    public void setCalendarNotifications(Boolean calendarNotifications) {
        this.calendarNotifications = calendarNotifications;
    }

    public Boolean isPaymentsNotifications() {
        return paymentsNotifications;
    }

    public void setPaymentsNotifications(Boolean paymentsNotifications) {
        this.paymentsNotifications = paymentsNotifications;
    }

    public Boolean isAppointmentsNotifications() {
        return appointmentsNotifications;
    }

    public void setAppointmentsNotifications(Boolean appointmentsNotifications) {
        this.appointmentsNotifications = appointmentsNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserPreferencesDTO userPreferencesDTO = (UserPreferencesDTO) o;
        if (userPreferencesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPreferencesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPreferencesDTO{" +
            "id=" + getId() +
            ", currency='" + getCurrency() + "'" +
            ", todoListNotifications='" + isTodoListNotifications() + "'" +
            ", calendarNotifications='" + isCalendarNotifications() + "'" +
            ", paymentsNotifications='" + isPaymentsNotifications() + "'" +
            ", appointmentsNotifications='" + isAppointmentsNotifications() + "'" +
            "}";
    }
}
