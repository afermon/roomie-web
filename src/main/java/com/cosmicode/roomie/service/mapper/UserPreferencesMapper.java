package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.UserPreferences;
import com.cosmicode.roomie.service.dto.UserPreferencesDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity UserPreferences and its DTO UserPreferencesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserPreferencesMapper extends EntityMapper<UserPreferencesDTO, UserPreferences> {



    default UserPreferences fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setId(id);
        return userPreferences;
    }
}
