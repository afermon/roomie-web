package com.cosmicode.roomie.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.cosmicode.roomie.domain.enumeration.RoomTaskState;

/**
 * A DTO for the RoomTask entity.
 */
public class RoomTaskDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant created;

    @NotNull
    @Size(min = 4, max = 50)
    private String title;

    @Size(min = 4, max = 500)
    private String description;

    private Instant deadline;

    @NotNull
    private RoomTaskState state;

    private Long roomId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public RoomTaskState getState() {
        return state;
    }

    public void setState(RoomTaskState state) {
        this.state = state;
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

        RoomTaskDTO roomTaskDTO = (RoomTaskDTO) o;
        if (roomTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomTaskDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", state='" + getState() + "'" +
            ", room=" + getRoomId() +
            "}";
    }
}
