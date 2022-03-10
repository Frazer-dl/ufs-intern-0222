package ru.philit.ufs.model.converter.esb.asfs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.user.Workplace;

@Mapper(componentModel = "spring", uses = {UtilMapper.class})
public interface WorkPlaceAdapterMapper {

  @Mapping(source = "workPlaceId", target = "workPlaceUId")
  SrvGetWorkPlaceInfoRq.SrvGetWorkPlaceInfoRqMessage map(String workPlaceId);

  @Mappings({
      @Mapping(source = "workPlaceType", target = "type"),
      @Mapping(source = "cashboxOnBoard", target = "cashboxOnBoard"),
      @Mapping(source = "subbranchCode", target = "subbranchCode"),
      @Mapping(source = "cashboxOnBoardDevice", target = "cashboxDeviceId"),
      @Mapping(source = "cashboxDeviceType", target = "cashboxDeviceType"),
      @Mapping(source = "currentCurrencyType", target = "currencyType"),
      @Mapping(source = "amount", target = "amount"),
      @Mapping(source = "workPlaceLimit", target = "limit"),
      @Mapping(source = "workPlaceOperationTypeLimit.operationTypeLimitItem",
          target = "categoryLimits")
  })
  Workplace convert(SrvGetWorkPlaceInfoRs.SrvGetWorkPlaceInfoRsMessage response);
}
