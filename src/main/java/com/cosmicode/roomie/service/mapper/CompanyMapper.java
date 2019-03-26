package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Company;
import com.cosmicode.roomie.service.dto.CompanyDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {



    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
