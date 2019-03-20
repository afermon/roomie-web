package com.cosmicode.roomie.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.cosmicode.roomie.domain.enumeration.CurrencyType;

/**
 * A UserPreferences.
 */
@Entity
@Table(name = "user_preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "userpreferences")
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyType currency;

    @Column(name = "todo_list_notifications")
    private Boolean todoListNotifications;

    @Column(name = "calendar_notifications")
    private Boolean calendarNotifications;

    @Column(name = "payments_notifications")
    private Boolean paymentsNotifications;

    @Column(name = "appointments_notifications")
    private Boolean appointmentsNotifications;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public UserPreferences currency(CurrencyType currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Boolean isTodoListNotifications() {
        return todoListNotifications;
    }

    public UserPreferences todoListNotifications(Boolean todoListNotifications) {
        this.todoListNotifications = todoListNotifications;
        return this;
    }

    public void setTodoListNotifications(Boolean todoListNotifications) {
        this.todoListNotifications = todoListNotifications;
    }

    public Boolean isCalendarNotifications() {
        return calendarNotifications;
    }

    public UserPreferences calendarNotifications(Boolean calendarNotifications) {
        this.calendarNotifications = calendarNotifications;
        return this;
    }

    public void setCalendarNotifications(Boolean calendarNotifications) {
        this.calendarNotifications = calendarNotifications;
    }

    public Boolean isPaymentsNotifications() {
        return paymentsNotifications;
    }

    public UserPreferences paymentsNotifications(Boolean paymentsNotifications) {
        this.paymentsNotifications = paymentsNotifications;
        return this;
    }

    public void setPaymentsNotifications(Boolean paymentsNotifications) {
        this.paymentsNotifications = paymentsNotifications;
    }

    public Boolean isAppointmentsNotifications() {
        return appointmentsNotifications;
    }

    public UserPreferences appointmentsNotifications(Boolean appointmentsNotifications) {
        this.appointmentsNotifications = appointmentsNotifications;
        return this;
    }

    public void setAppointmentsNotifications(Boolean appointmentsNotifications) {
        this.appointmentsNotifications = appointmentsNotifications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPreferences userPreferences = (UserPreferences) o;
        if (userPreferences.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPreferences.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
            "id=" + getId() +
            ", currency='" + getCurrency() + "'" +
            ", todoListNotifications='" + isTodoListNotifications() + "'" +
            ", calendarNotifications='" + isCalendarNotifications() + "'" +
            ", paymentsNotifications='" + isPaymentsNotifications() + "'" +
            ", appointmentsNotifications='" + isAppointmentsNotifications() + "'" +
            "}";
    }
}
