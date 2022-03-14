package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.oper.OperationTypeLimit;
import ru.philit.ufs.model.entity.user.Workplace;
import ru.philit.ufs.model.entity.user.WorkplaceType;

@Mapper(componentModel = "spring")
public interface WorkPlaceAdapterMapper {

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

  @Mapping(source = "workPlaceId", target = "workPlaceUId")
  SrvGetWorkPlaceInfoRq.SrvGetWorkPlaceInfoRqMessage map(String workPlaceId);

  /**
   * Преобразует транспортный объект рабочего места во внутреннюю сущность.
   */
  default WorkplaceType map(BigInteger value) {
    switch (value.intValue()) {
      case 0:
        return WorkplaceType.CASHBOX;
      case 1:
        return WorkplaceType.UWP;
      case 2:
        return WorkplaceType.OTHER;
      default:
        return null;
    }
  }

  /**
   * Преобразует транспортный объект рабочего места во внутреннюю сущность.
   */
  default List<OperationTypeLimit> map(List<SrvGetWorkPlaceInfoRs
      .SrvGetWorkPlaceInfoRsMessage.WorkPlaceOperationTypeLimit.OperationTypeLimitItem> list) {
    List<OperationTypeLimit> result = new ArrayList<>();
    if (list != null) {
      result = list.stream()
          .map(o -> {
            OperationTypeLimit operationTypeLimit = new OperationTypeLimit();
            operationTypeLimit.setCategoryId(o.getOperationCategory().toString());
            operationTypeLimit.setLimit(o.getOperationLimit());
            return operationTypeLimit;
          })
          .collect(Collectors.toList());
    }
    return result;
  }
}
