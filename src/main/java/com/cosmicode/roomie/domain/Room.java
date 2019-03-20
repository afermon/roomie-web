package com.cosmicode.roomie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.cosmicode.roomie.domain.enumeration.RoomState;

import com.cosmicode.roomie.domain.enumeration.RoomType;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private RoomState state;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Column(name = "published", nullable = false)
    private Instant published;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Size(min = 4, max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @Column(name = "rooms", nullable = false)
    private Integer rooms;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Size(min = 4, max = 200)
    @Column(name = "apoinments_notes", length = 200)
    private String apoinmentsNotes;

    @NotNull
    @Column(name = "looking_for_roomie", nullable = false)
    private Boolean lookingForRoomie;

    @NotNull
    @Column(name = "available_from", nullable = false)
    private LocalDate availableFrom;

    @NotNull
    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @OneToOne    @JoinColumn(unique = true)
    private Address address;

    @OneToOne    @JoinColumn(unique = true)
    private RoomExpense price;

    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Appointment> appointments = new HashSet<>();
    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomTask> roomTasks = new HashSet<>();
    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomEvent> roomEvents = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "room_roomies",
               joinColumns = @JoinColumn(name = "rooms_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "roomies_id", referencedColumnName = "id"))
    private Set<Roomie> roomies = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "room_features",
               joinColumns = @JoinColumn(name = "rooms_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "features_id", referencedColumnName = "id"))
    private Set<RoomFeature> features = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("rooms")
    private Roomie owner;

    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomExpense> expenses = new HashSet<>();
    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomPicture> pictures = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomState getState() {
        return state;
    }

    public Room state(RoomState state) {
        this.state = state;
        return this;
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public Instant getCreated() {
        return created;
    }

    public Room created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getPublished() {
        return published;
    }

    public Room published(Instant published) {
        this.published = published;
        return this;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public Room title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Room description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRooms() {
        return rooms;
    }

    public Room rooms(Integer rooms) {
        this.rooms = rooms;
        return this;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Room roomType(RoomType roomType) {
        this.roomType = roomType;
        return this;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getApoinmentsNotes() {
        return apoinmentsNotes;
    }

    public Room apoinmentsNotes(String apoinmentsNotes) {
        this.apoinmentsNotes = apoinmentsNotes;
        return this;
    }

    public void setApoinmentsNotes(String apoinmentsNotes) {
        this.apoinmentsNotes = apoinmentsNotes;
    }

    public Boolean isLookingForRoomie() {
        return lookingForRoomie;
    }

    public Room lookingForRoomie(Boolean lookingForRoomie) {
        this.lookingForRoomie = lookingForRoomie;
        return this;
    }

    public void setLookingForRoomie(Boolean lookingForRoomie) {
        this.lookingForRoomie = lookingForRoomie;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public Room availableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
        return this;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Boolean isIsPremium() {
        return isPremium;
    }

    public Room isPremium(Boolean isPremium) {
        this.isPremium = isPremium;
        return this;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Address getAddress() {
        return address;
    }

    public Room address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RoomExpense getPrice() {
        return price;
    }

    public Room price(RoomExpense roomExpense) {
        this.price = roomExpense;
        return this;
    }

    public void setPrice(RoomExpense roomExpense) {
        this.price = roomExpense;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public Room appointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    public Room addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setRoom(this);
        return this;
    }

    public Room removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setRoom(null);
        return this;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<RoomTask> getRoomTasks() {
        return roomTasks;
    }

    public Room roomTasks(Set<RoomTask> roomTasks) {
        this.roomTasks = roomTasks;
        return this;
    }

    public Room addRoomTask(RoomTask roomTask) {
        this.roomTasks.add(roomTask);
        roomTask.setRoom(this);
        return this;
    }

    public Room removeRoomTask(RoomTask roomTask) {
        this.roomTasks.remove(roomTask);
        roomTask.setRoom(null);
        return this;
    }

    public void setRoomTasks(Set<RoomTask> roomTasks) {
        this.roomTasks = roomTasks;
    }

    public Set<RoomEvent> getRoomEvents() {
        return roomEvents;
    }

    public Room roomEvents(Set<RoomEvent> roomEvents) {
        this.roomEvents = roomEvents;
        return this;
    }

    public Room addRoomEvent(RoomEvent roomEvent) {
        this.roomEvents.add(roomEvent);
        roomEvent.setRoom(this);
        return this;
    }

    public Room removeRoomEvent(RoomEvent roomEvent) {
        this.roomEvents.remove(roomEvent);
        roomEvent.setRoom(null);
        return this;
    }

    public void setRoomEvents(Set<RoomEvent> roomEvents) {
        this.roomEvents = roomEvents;
    }

    public Set<Roomie> getRoomies() {
        return roomies;
    }

    public Room roomies(Set<Roomie> roomies) {
        this.roomies = roomies;
        return this;
    }

    public Room addRoomies(Roomie roomie) {
        this.roomies.add(roomie);
        return this;
    }

    public Room removeRoomies(Roomie roomie) {
        this.roomies.remove(roomie);
        return this;
    }

    public void setRoomies(Set<Roomie> roomies) {
        this.roomies = roomies;
    }

    public Set<RoomFeature> getFeatures() {
        return features;
    }

    public Room features(Set<RoomFeature> roomFeatures) {
        this.features = roomFeatures;
        return this;
    }

    public Room addFeatures(RoomFeature roomFeature) {
        this.features.add(roomFeature);
        return this;
    }

    public Room removeFeatures(RoomFeature roomFeature) {
        this.features.remove(roomFeature);
        return this;
    }

    public void setFeatures(Set<RoomFeature> roomFeatures) {
        this.features = roomFeatures;
    }

    public Roomie getOwner() {
        return owner;
    }

    public Room owner(Roomie roomie) {
        this.owner = roomie;
        return this;
    }

    public void setOwner(Roomie roomie) {
        this.owner = roomie;
    }

    public Set<RoomExpense> getExpenses() {
        return expenses;
    }

    public Room expenses(Set<RoomExpense> roomExpenses) {
        this.expenses = roomExpenses;
        return this;
    }

    public Room addExpenses(RoomExpense roomExpense) {
        this.expenses.add(roomExpense);
        roomExpense.setRoom(this);
        return this;
    }

    public Room removeExpenses(RoomExpense roomExpense) {
        this.expenses.remove(roomExpense);
        roomExpense.setRoom(null);
        return this;
    }

    public void setExpenses(Set<RoomExpense> roomExpenses) {
        this.expenses = roomExpenses;
    }

    public Set<RoomPicture> getPictures() {
        return pictures;
    }

    public Room pictures(Set<RoomPicture> roomPictures) {
        this.pictures = roomPictures;
        return this;
    }

    public Room addPictures(RoomPicture roomPicture) {
        this.pictures.add(roomPicture);
        roomPicture.setRoom(this);
        return this;
    }

    public Room removePictures(RoomPicture roomPicture) {
        this.pictures.remove(roomPicture);
        roomPicture.setRoom(null);
        return this;
    }

    public void setPictures(Set<RoomPicture> roomPictures) {
        this.pictures = roomPictures;
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
        Room room = (Room) o;
        if (room.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), room.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", created='" + getCreated() + "'" +
            ", published='" + getPublished() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", rooms=" + getRooms() +
            ", roomType='" + getRoomType() + "'" +
            ", apoinmentsNotes='" + getApoinmentsNotes() + "'" +
            ", lookingForRoomie='" + isLookingForRoomie() + "'" +
            ", availableFrom='" + getAvailableFrom() + "'" +
            ", isPremium='" + isIsPremium() + "'" +
            "}";
    }
}
