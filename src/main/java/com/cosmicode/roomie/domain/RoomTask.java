package com.cosmicode.roomie.domain;


import com.cosmicode.roomie.domain.enumeration.RoomTaskState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A RoomTask.
 */
@Entity
@Table(name = "room_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "roomtask")
public class RoomTask implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Size(min = 4, max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "deadline")
    private Instant deadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private RoomTaskState state;

    @ManyToOne
    @JsonIgnoreProperties("roomTasks")
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public RoomTask created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public RoomTask title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public RoomTask description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public RoomTask deadline(Instant deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public RoomTaskState getState() {
        return state;
    }

    public RoomTask state(RoomTaskState state) {
        this.state = state;
        return this;
    }

    public void setState(RoomTaskState state) {
        this.state = state;
    }

    public Room getRoom() {
        return room;
    }

    public RoomTask room(Room room) {
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
        RoomTask roomTask = (RoomTask) o;
        if (roomTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomTask{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
