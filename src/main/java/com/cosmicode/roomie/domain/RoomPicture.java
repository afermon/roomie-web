package com.cosmicode.roomie.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RoomPicture.
 */
@Entity
@Table(name = "room_picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "roompicture")
public class RoomPicture implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @ManyToOne
    @JsonIgnoreProperties("pictures")
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public RoomPicture url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsMain() {
        return isMain;
    }

    public RoomPicture isMain(Boolean isMain) {
        this.isMain = isMain;
        return this;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Room getRoom() {
        return room;
    }

    public RoomPicture room(Room room) {
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
        RoomPicture roomPicture = (RoomPicture) o;
        if (roomPicture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomPicture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomPicture{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", isMain='" + isIsMain() + "'" +
            "}";
    }
}
