package com.cosmicode.roomie.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

import com.cosmicode.roomie.domain.enumeration.CurrencyType;

/**
 * A RoomExpense.
 */
@Entity
@Table(name = "room_expense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "roomexpense")
public class RoomExpense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(min = 4, max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyType currency;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "periodicity", nullable = false)
    private Integer periodicity;

    @NotNull
    @Column(name = "month_day", nullable = false)
    private Integer monthDay;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @OneToMany(mappedBy = "expense")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomExpenseSplit> splits = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("expenses")
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RoomExpense name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public RoomExpense description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public RoomExpense currency(CurrencyType currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public RoomExpense amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPeriodicity() {
        return periodicity;
    }

    public RoomExpense periodicity(Integer periodicity) {
        this.periodicity = periodicity;
        return this;
    }

    public void setPeriodicity(Integer periodicity) {
        this.periodicity = periodicity;
    }

    public Integer getMonthDay() {
        return monthDay;
    }

    public RoomExpense monthDay(Integer monthDay) {
        this.monthDay = monthDay;
        return this;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public RoomExpense startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public RoomExpense finishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public Set<RoomExpenseSplit> getSplits() {
        return splits;
    }

    public RoomExpense splits(Set<RoomExpenseSplit> roomExpenseSplits) {
        this.splits = roomExpenseSplits;
        return this;
    }

    public RoomExpense addSplits(RoomExpenseSplit roomExpenseSplit) {
        this.splits.add(roomExpenseSplit);
        roomExpenseSplit.setExpense(this);
        return this;
    }

    public RoomExpense removeSplits(RoomExpenseSplit roomExpenseSplit) {
        this.splits.remove(roomExpenseSplit);
        roomExpenseSplit.setExpense(null);
        return this;
    }

    public void setSplits(Set<RoomExpenseSplit> roomExpenseSplits) {
        this.splits = roomExpenseSplits;
    }

    public Room getRoom() {
        return room;
    }

    public RoomExpense room(Room room) {
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
        RoomExpense roomExpense = (RoomExpense) o;
        if (roomExpense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpense{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", amount=" + getAmount() +
            ", periodicity=" + getPeriodicity() +
            ", monthDay=" + getMonthDay() +
            ", startDate='" + getStartDate() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            "}";
    }
}
