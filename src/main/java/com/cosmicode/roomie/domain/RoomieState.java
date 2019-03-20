package com.cosmicode.roomie.domain;


import com.cosmicode.roomie.domain.enumeration.AccountState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RoomieState.
 */
@Entity
@Table(name = "roomie_state")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "roomiestate")
public class RoomieState implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AccountState state;

    @Column(name = "suspended_date")
    private LocalDate suspendedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountState getState() {
        return state;
    }

    public RoomieState state(AccountState state) {
        this.state = state;
        return this;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public LocalDate getSuspendedDate() {
        return suspendedDate;
    }

    public RoomieState suspendedDate(LocalDate suspendedDate) {
        this.suspendedDate = suspendedDate;
        return this;
    }

    public void setSuspendedDate(LocalDate suspendedDate) {
        this.suspendedDate = suspendedDate;
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
        RoomieState roomieState = (RoomieState) o;
        if (roomieState.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomieState.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomieState{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", suspendedDate='" + getSuspendedDate() + "'" +
            "}";
    }
}
