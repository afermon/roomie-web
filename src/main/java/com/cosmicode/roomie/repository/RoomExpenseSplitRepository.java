package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomExpenseSplit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomExpenseSplit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomExpenseSplitRepository extends JpaRepository<RoomExpenseSplit, Long> {

}
