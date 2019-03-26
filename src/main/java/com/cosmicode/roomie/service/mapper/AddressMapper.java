package com.cosmicode.roomie.service.mapper;

import com.cosmicode.roomie.domain.Address;
import com.cosmicode.roomie.service.dto.AddressDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {



    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
