package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTaskRepository extends JpaRepository<RoomTask, Long> {

}
