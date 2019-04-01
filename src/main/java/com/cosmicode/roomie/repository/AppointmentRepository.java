package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "select distinct appointment from Appointment appointment where appointment.petitioner.user.login = ?#{principal.username} or appointment.room.owner.user.login = ?#{principal.username}")
    Page<Appointment> findAllRoomie(Pageable pageable);

}
