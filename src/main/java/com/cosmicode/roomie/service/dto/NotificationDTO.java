package com.cosmicode.roomie.service.dto;

import com.cosmicode.roomie.domain.enumeration.NotificationState;
import com.cosmicode.roomie.domain.enumeration.NotificationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Notification entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    private Instant created;

    @NotNull
    @Size(min = 4, max = 50)
    private String title;

    @NotNull
    @Size(min = 4, max = 200)
    private String body;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationState state;

    private Long entityId;


    private Long recipientId;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationState getState() {
        return state;
    }

    public void setState(NotificationState state) {
        this.state = state;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long roomieId) {
        this.recipientId = roomieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", entityId=" + getEntityId() +
            ", recipient=" + getRecipientId() +
            "}";
    }
}
