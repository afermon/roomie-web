package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomExpenseSplitRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomExpenseSplitRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomExpenseSplitRecordRepository extends JpaRepository<RoomExpenseSplitRecord, Long> {

}
