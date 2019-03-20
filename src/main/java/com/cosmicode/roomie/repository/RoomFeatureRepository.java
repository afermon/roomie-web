package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomFeature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomFeatureRepository extends JpaRepository<RoomFeature, Long> {

}
