package com.cosmicode.roomie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.cosmicode.roomie.domain.enumeration.ReportType;

/**
 * A UserReport.
 */
@Entity
@Table(name = "user_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userreport")
public class UserReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(min = 4, max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ReportType type;

    @OneToOne    @JoinColumn(unique = true)
    private Roomie roomie;

    @OneToOne    @JoinColumn(unique = true)
    private Room room;

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

    public UserReport date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public UserReport description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportType getType() {
        return type;
    }

    public UserReport type(ReportType type) {
        this.type = type;
        return this;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Roomie getRoomie() {
        return roomie;
    }

    public UserReport roomie(Roomie roomie) {
        this.roomie = roomie;
        return this;
    }

    public void setRoomie(Roomie roomie) {
        this.roomie = roomie;
    }

    public Room getRoom() {
        return room;
    }

    public UserReport room(Room room) {
        this.room = room;
        return this;
    }

    public void setRoom(Room room) {
        this.room = room;
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
        UserReport userReport = (UserReport) o;
        if (userReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserReport{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
