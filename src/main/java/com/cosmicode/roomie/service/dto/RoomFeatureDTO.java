package com.cosmicode.roomie.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.cosmicode.roomie.domain.enumeration.Lang;
import com.cosmicode.roomie.domain.enumeration.FeatureType;

/**
 * A DTO for the RoomFeature entity.
 */
public class RoomFeatureDTO implements Serializable {

    private Long id;

    @NotNull
    private Lang lang;

    @NotNull
    private FeatureType type;

    @NotNull
    @Size(min = 4, max = 50)
    private String name;

    @NotNull
    private String icon;

    @Size(min = 4, max = 100)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomFeatureDTO roomFeatureDTO = (RoomFeatureDTO) o;
        if (roomFeatureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomFeatureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomFeatureDTO{" +
            "id=" + getId() +
            ", lang='" + getLang() + "'" +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
