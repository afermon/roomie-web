package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
