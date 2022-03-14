package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.philit.ufs.model.entity.common.ExternalEntityContainer;
import ru.philit.ufs.model.entity.esb.asfs.LimitStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs;
import ru.philit.ufs.model.entity.oper.Limit;

@Mapper(componentModel = "spring")
public interface OverLimitAdapterMapper {

  @Mappings({
      @Mapping(source = "userLogin", target = "userLogin"),
      @Mapping(source = "tobeIncreased", target = "tobeIncreased"),
      @Mapping(source = "amount", target = "amount")
  })
  SrvCheckOverLimitRq.SrvCheckOverLimitRqMessage map(Limit limit);

  default boolean map(LimitStatusType limitStatusType) {
    return (limitStatusType.value().equals(LimitStatusType.LIMIT_PASSED.value()));
  }

  @Mappings({
      @Mapping(source = "responseCode", target = "responseCode"),
      @Mapping(source = "status", target = "data")
  })
  ExternalEntityContainer<Boolean> convert(SrvCheckOverLimitRs.SrvCheckOverLimitRsMessage response);
}
