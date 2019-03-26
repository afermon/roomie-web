package com.cosmicode.roomie.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A RoomExpenseSplit.
 */
@Entity
@Table(name = "room_expense_split")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(shards = 1, replicas = 0, refreshInterval = "-1", indexName = "roomexpensesplit")
public class RoomExpenseSplit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "split")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoomExpenseSplitRecord> records = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("splits")
    private RoomExpense expense;

    @ManyToOne
    @JsonIgnoreProperties("roomExpenseSplits")
    private Roomie roomie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public RoomExpenseSplit amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Set<RoomExpenseSplitRecord> getRecords() {
        return records;
    }

    public RoomExpenseSplit records(Set<RoomExpenseSplitRecord> roomExpenseSplitRecords) {
        this.records = roomExpenseSplitRecords;
        return this;
    }

    public RoomExpenseSplit addRecords(RoomExpenseSplitRecord roomExpenseSplitRecord) {
        this.records.add(roomExpenseSplitRecord);
        roomExpenseSplitRecord.setSplit(this);
        return this;
    }

    public RoomExpenseSplit removeRecords(RoomExpenseSplitRecord roomExpenseSplitRecord) {
        this.records.remove(roomExpenseSplitRecord);
        roomExpenseSplitRecord.setSplit(null);
        return this;
    }

    public void setRecords(Set<RoomExpenseSplitRecord> roomExpenseSplitRecords) {
        this.records = roomExpenseSplitRecords;
    }

    public RoomExpense getExpense() {
        return expense;
    }

    public RoomExpenseSplit expense(RoomExpense roomExpense) {
        this.expense = roomExpense;
        return this;
    }

    public void setExpense(RoomExpense roomExpense) {
        this.expense = roomExpense;
    }

    public Roomie getRoomie() {
        return roomie;
    }

    public RoomExpenseSplit roomie(Roomie roomie) {
        this.roomie = roomie;
        return this;
    }

    public void setRoomie(Roomie roomie) {
        this.roomie = roomie;
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
        RoomExpenseSplit roomExpenseSplit = (RoomExpenseSplit) o;
        if (roomExpenseSplit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpenseSplit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpenseSplit{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
