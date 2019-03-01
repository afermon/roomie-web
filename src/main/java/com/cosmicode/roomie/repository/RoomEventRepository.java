package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomEventRepository extends JpaRepository<RoomEvent, Long> {

}
