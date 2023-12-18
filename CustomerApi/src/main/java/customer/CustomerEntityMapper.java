package customer;

import customer.dto.UpdateCustomerDto;
import customer.entity.CustomerEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerEntityMapper {
    CustomerEntityMapper INSTANCE = Mappers.getMapper(CustomerEntityMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerEntity updateDtoToEntity(UpdateCustomerDto dto, @MappingTarget CustomerEntity entity);
}
