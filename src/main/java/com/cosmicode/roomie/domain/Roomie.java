package com.cosmicode.roomie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.cosmicode.roomie.domain.enumeration.Gender;

/**
 * A Roomie.
 */
@Entity
@Table(name = "roomie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "roomie")
public class Roomie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Size(min = 4, max = 750)
    @Column(name = "biography", length = 750)
    private String biography;

    @NotNull
    @Column(name = "picture", nullable = false)
    private String picture;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Size(min = 4, max = 25)
    @Pattern(regexp = "^(1[ \\-\\+]{0,3}|\\+1[ -\\+]{0,3}|\\+1|\\+)?((\\(\\+?1-[2-9][0-9]{1,2}\\))|(\\(\\+?[2-8][0-9][0-9]\\))|(\\(\\+?[1-9][0-9]\\))|(\\(\\+?[17]\\))|(\\([2-9][2-9]\\))|([ \\-\\.]{0,3}[0-9]{2,4}))?([ \\-\\.][0-9])?([ \\-\\.]{0,3}[0-9]{2,4}){2,3}$")
    @Column(name = "phone", length = 25)
    private String phone;

    @NotNull
    @Size(min = 0, max = 200)
    @Column(name = "mobile_device_id", length = 200, nullable = false)
    private String mobileDeviceID;

    @OneToOne    @JoinColumn(unique = true)
    private User user;

    @OneToOne    @JoinColumn(unique = true)
    private RoomieState state;

    @OneToOne    @JoinColumn(unique = true)
    private Address address;

    @OneToOne    @JoinColumn(unique = true)
    private UserPreferences configuration;

    @OneToMany(mappedBy = "roomie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomExpenseSplit> roomExpenseSplits = new HashSet<>();
    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Room> rooms = new HashSet<>();
    @OneToMany(mappedBy = "organizer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomEvent> roomEvents = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "roomie_lifestyle",
               joinColumns = @JoinColumn(name = "roomies_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "lifestyles_id", referencedColumnName = "id"))
    private Set<RoomFeature> lifestyles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Roomie birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public Roomie biography(String biography) {
        this.biography = biography;
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }

    public Roomie picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Gender getGender() {
        return gender;
    }

    public Roomie gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public Roomie phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobileDeviceID() {
        return mobileDeviceID;
    }

    public Roomie mobileDeviceID(String mobileDeviceID) {
        this.mobileDeviceID = mobileDeviceID;
        return this;
    }

    public void setMobileDeviceID(String mobileDeviceID) {
        this.mobileDeviceID = mobileDeviceID;
    }

    public User getUser() {
        return user;
    }

    public Roomie user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoomieState getState() {
        return state;
    }

    public Roomie state(RoomieState roomieState) {
        this.state = roomieState;
        return this;
    }

    public void setState(RoomieState roomieState) {
        this.state = roomieState;
    }

    public Address getAddress() {
        return address;
    }

    public Roomie address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public UserPreferences getConfiguration() {
        return configuration;
    }

    public Roomie configuration(UserPreferences userPreferences) {
        this.configuration = userPreferences;
        return this;
    }

    public void setConfiguration(UserPreferences userPreferences) {
        this.configuration = userPreferences;
    }

    public Set<RoomExpenseSplit> getRoomExpenseSplits() {
        return roomExpenseSplits;
    }

    public Roomie roomExpenseSplits(Set<RoomExpenseSplit> roomExpenseSplits) {
        this.roomExpenseSplits = roomExpenseSplits;
        return this;
    }

    public Roomie addRoomExpenseSplit(RoomExpenseSplit roomExpenseSplit) {
        this.roomExpenseSplits.add(roomExpenseSplit);
        roomExpenseSplit.setRoomie(this);
        return this;
    }

    public Roomie removeRoomExpenseSplit(RoomExpenseSplit roomExpenseSplit) {
        this.roomExpenseSplits.remove(roomExpenseSplit);
        roomExpenseSplit.setRoomie(null);
        return this;
    }

    public void setRoomExpenseSplits(Set<RoomExpenseSplit> roomExpenseSplits) {
        this.roomExpenseSplits = roomExpenseSplits;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public Roomie rooms(Set<Room> rooms) {
        this.rooms = rooms;
        return this;
    }

    public Roomie addRoom(Room room) {
        this.rooms.add(room);
        room.setOwner(this);
        return this;
    }

    public Roomie removeRoom(Room room) {
        this.rooms.remove(room);
        room.setOwner(null);
        return this;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public Set<RoomEvent> getRoomEvents() {
        return roomEvents;
    }

    public Roomie roomEvents(Set<RoomEvent> roomEvents) {
        this.roomEvents = roomEvents;
        return this;
    }

    public Roomie addRoomEvent(RoomEvent roomEvent) {
        this.roomEvents.add(roomEvent);
        roomEvent.setOrganizer(this);
        return this;
    }

    public Roomie removeRoomEvent(RoomEvent roomEvent) {
        this.roomEvents.remove(roomEvent);
        roomEvent.setOrganizer(null);
        return this;
    }

    public void setRoomEvents(Set<RoomEvent> roomEvents) {
        this.roomEvents = roomEvents;
    }

    public Set<RoomFeature> getLifestyles() {
        return lifestyles;
    }

    public Roomie lifestyles(Set<RoomFeature> roomFeatures) {
        this.lifestyles = roomFeatures;
        return this;
    }

    public Roomie addLifestyle(RoomFeature roomFeature) {
        this.lifestyles.add(roomFeature);
        return this;
    }

    public Roomie removeLifestyle(RoomFeature roomFeature) {
        this.lifestyles.remove(roomFeature);
        return this;
    }

    public void setLifestyles(Set<RoomFeature> roomFeatures) {
        this.lifestyles = roomFeatures;
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
        Roomie roomie = (Roomie) o;
        if (roomie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Roomie{" +
            "id=" + getId() +
            ", birthDate='" + getBirthDate() + "'" +
            ", biography='" + getBiography() + "'" +
            ", picture='" + getPicture() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", mobileDeviceID='" + getMobileDeviceID() + "'" +
            "}";
    }
}
