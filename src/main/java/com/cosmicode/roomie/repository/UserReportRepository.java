package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

}
