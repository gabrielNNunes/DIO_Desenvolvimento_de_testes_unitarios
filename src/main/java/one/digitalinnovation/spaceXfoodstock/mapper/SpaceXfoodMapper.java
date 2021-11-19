package one.digitalinnovation.spaceXfoodstock.mapper;

import one.digitalinnovation.spaceXfoodstock.dto.SpaceXfoodDTO;
import one.digitalinnovation.spaceXfoodstock.entity.SpaceXfood;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpaceXfoodMapper {

    SpaceXfoodMapper INSTANCE = Mappers.getMapper(SpaceXfoodMapper.class);

    SpaceXfood toModel(SpaceXfoodDTO spaceXfoodDTO);

    SpaceXfoodDTO toDTO(SpaceXfood spaceXfood);
}
