package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomieState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomieState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomieStateRepository extends JpaRepository<RoomieState, Long> {

}
