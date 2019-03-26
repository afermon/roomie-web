package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.RoomPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoomPicture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomPictureRepository extends JpaRepository<RoomPicture, Long> {

}
