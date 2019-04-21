package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the RoomExpenseSplit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomExpenseSplitRepository extends JpaRepository<RoomExpenseSplit, Long> {

    List<RoomExpenseSplit> findAllByExpenseId(Long id);

}
