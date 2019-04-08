package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data  repository for the RoomTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTaskRepository extends JpaRepository<RoomTask, Long> {

    List<RoomTask> findByRoomId(long id);

    List<RoomTask> findByDeadlineBetween(Instant startTime, Instant endTime);
}
