package com.cosmicode.roomie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.cosmicode.roomie.domain.enumeration.Lang;

import com.cosmicode.roomie.domain.enumeration.FeatureType;

/**
 * A RoomFeature.
 */
@Entity
@Table(name = "room_feature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "roomfeature")
public class RoomFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lang", nullable = false)
    private Lang lang;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private FeatureType type;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @Size(min = 4, max = 100)
    @Column(name = "description", length = 100)
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lang getLang() {
        return lang;
    }

    public RoomFeature lang(Lang lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public FeatureType getType() {
        return type;
    }

    public RoomFeature type(FeatureType type) {
        this.type = type;
        return this;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public RoomFeature name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public RoomFeature icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public RoomFeature description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        RoomFeature roomFeature = (RoomFeature) o;
        if (roomFeature.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomFeature.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomFeature{" +
            "id=" + getId() +
            ", lang='" + getLang() + "'" +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
