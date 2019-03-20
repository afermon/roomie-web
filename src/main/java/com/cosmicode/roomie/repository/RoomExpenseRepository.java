package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomExpenseRepository extends JpaRepository<RoomExpense, Long> {

}
