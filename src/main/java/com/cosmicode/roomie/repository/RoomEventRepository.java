package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data  repository for the RoomEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomEventRepository extends JpaRepository<RoomEvent, Long> {

    @Query(value = "select distinct roomevent from RoomEvent roomevent left join fetch roomevent.organizer where roomevent.room.id =:id",
        countQuery = "select count(distinct roomevent) from RoomEvent roomevent where roomevent.room.id =:id")
    Page<RoomEvent> findAllByRoom(Pageable pageable, @Param("id") Long id);

    List<RoomEvent> findByStartTimeBetween(Instant startTime, Instant endTime);
}
