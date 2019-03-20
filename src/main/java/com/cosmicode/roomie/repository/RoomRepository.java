package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Room entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "select distinct room from Room room left join fetch room.roomies left join fetch room.features",
        countQuery = "select count(distinct room) from Room room")
    Page<Room> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct room from Room room left join fetch room.roomies left join fetch room.features")
    List<Room> findAllWithEagerRelationships();

    @Query("select room from Room room left join fetch room.roomies left join fetch room.features where room.id =:id")
    Optional<Room> findOneWithEagerRelationships(@Param("id") Long id);

}
