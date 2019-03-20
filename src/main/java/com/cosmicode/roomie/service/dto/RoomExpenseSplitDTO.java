package com.cosmicode.roomie.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RoomExpenseSplit entity.
 */
public class RoomExpenseSplitDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;


    private Long expenseId;

    private Long roomieId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long roomExpenseId) {
        this.expenseId = roomExpenseId;
    }

    public Long getRoomieId() {
        return roomieId;
    }

    public void setRoomieId(Long roomieId) {
        this.roomieId = roomieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoomExpenseSplitDTO roomExpenseSplitDTO = (RoomExpenseSplitDTO) o;
        if (roomExpenseSplitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roomExpenseSplitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoomExpenseSplitDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", expense=" + getExpenseId() +
            ", roomie=" + getRoomieId() +
            "}";
    }
}
