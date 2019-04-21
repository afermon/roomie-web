package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the RoomExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomExpenseRepository extends JpaRepository<RoomExpense, Long> {

    List<RoomExpense> findByStartDateBeforeAndFinishDateAfter(LocalDate startDate, LocalDate endDate);

    List<RoomExpense> findAllByRoomId(Long id);
}
