package com.cosmicode.roomie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RoomExpenseSplitRecord.
 */
@Entity
@Table(name = "room_expense_split_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "roomexpensesplitrecord")
public class RoomExpenseSplitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(min = 4, max = 25)
    @Column(name = "state", length = 25, nullable = false)
    private String state;

    @ManyToOne
    @JsonIgnoreProperties("records")
    private RoomExpenseSplit split;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public RoomExpenseSplitRecord date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public RoomExpenseSplitRecord state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public RoomExpenseSplit getSplit() {
        return split;
    }

    public RoomExpenseSplitRecord split(RoomExpenseSplit roomExpenseSplit) {
        this.split = roomExpenseSplit;
        return this;
    }

    public void setSplit(RoomExpenseSplit roomExpenseSplit) {
        this.split = roomExpenseSplit;
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
        RoomExpenseSplitRecord roomExpenseSplitRecord = (RoomExpenseSplitRecord) o;
        if (roomExpenseSplitRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpenseSplitRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpenseSplitRecord{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
