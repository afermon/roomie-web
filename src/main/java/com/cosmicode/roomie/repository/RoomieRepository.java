package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.Roomie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Roomie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomieRepository extends JpaRepository<Roomie, Long> {

    @Query(value = "select distinct roomie from Roomie roomie left join fetch roomie.lifestyles",
        countQuery = "select count(distinct roomie) from Roomie roomie")
    Page<Roomie> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct roomie from Roomie roomie left join fetch roomie.lifestyles")
    List<Roomie> findAllWithEagerRelationships();

    @Query("select roomie from Roomie roomie left join fetch roomie.lifestyles where roomie.id =:id")
    Optional<Roomie> findOneWithEagerRelationships(@Param("id") Long id);

}