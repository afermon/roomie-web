package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the RoomTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTaskRepository extends JpaRepository<RoomTask, Long> {

    List<RoomTask> findByRoomId(long id);
}
