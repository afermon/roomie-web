package com.cosmicode.roomie.repository;

import com.cosmicode.roomie.domain.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

}
