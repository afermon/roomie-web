package com.cosmicode.roomie.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.cosmicode.roomie.domain.enumeration.CurrencyType;

/**
 * A DTO for the RoomExpense entity.
 */
public class RoomExpenseDTO implements Serializable {

    private Long id;

    private Boolean isRent;

    @NotNull
    @Size(min = 4, max = 100)
    private String name;

    @Size(min = 4, max = 500)
    private String description;

    @NotNull
    private CurrencyType currency;

    @NotNull
    private Double amount;

    @NotNull
    private Integer periodicity;

    @NotNull
    private Integer monthDay;

    private LocalDate startDate;

    private LocalDate finishDate;

    private Long roomId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsRent() {
        return isRent;
    }

    public void setIsRent(Boolean isRent) {
        this.isRent = isRent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Integer periodicity) {
        this.periodicity = periodicity;
    }

    public Integer getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(Integer monthDay) {
        this.monthDay = monthDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
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

        RoomExpenseDTO roomExpenseDTO = (RoomExpenseDTO) o;
        if (roomExpenseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpenseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpenseDTO{" +
            "id=" + getId() +
            ", isRent='" + isIsRent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", amount=" + getAmount() +
            ", periodicity=" + getPeriodicity() +
            ", monthDay=" + getMonthDay() +
            ", startDate='" + getStartDate() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            ", room=" + getRoomId() +
            "}";
    }
}
