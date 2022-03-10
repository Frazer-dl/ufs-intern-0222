package ru.philit.ufs.model.converter.esb.asfs;

import java.util.ArrayList;
import org.mapstruct.factory.Mappers;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs.SrvGetWorkPlaceInfoRsMessage.WorkPlaceOperationTypeLimit;
import ru.philit.ufs.model.entity.oper.OperationTypeLimit;
import ru.philit.ufs.model.entity.user.Workplace;
import ru.philit.ufs.model.entity.user.WorkplaceType;

public class WorkPlaceAdapter extends AsfsAdapter {

  private static final WorkPlaceAdapterMapper mapper
      = Mappers.getMapper(WorkPlaceAdapterMapper.class);

  private static void map(SrvGetWorkPlaceInfoRs.SrvGetWorkPlaceInfoRsMessage message,
      Workplace workplace) {
    workplace.setType(WorkplaceType.getByCode(message.getWorkPlaceType().intValue()));
    workplace.setCashboxOnBoard(message.isCashboxOnBoard());
    workplace.setCashboxDeviceType(message.getCashboxDeviceType());
    workplace.setSubbranchCode(message.getSubbranchCode());
    workplace.setCurrencyType(message.getCurrentCurrencyType());
    workplace.setAmount(message.getAmount());
    workplace.setLimit(message.getWorkPlaceLimit());
    workplace.setCategoryLimits(new ArrayList<>());
    if (message.getWorkPlaceOperationTypeLimit() != null) {
      for (WorkPlaceOperationTypeLimit.OperationTypeLimitItem item:
          message.getWorkPlaceOperationTypeLimit().getOperationTypeLimitItem()) {
        OperationTypeLimit operationTypeLimit = new OperationTypeLimit();
        operationTypeLimit.setCategoryId(item.getOperationCategory().toString());
        operationTypeLimit.setLimit(item.getOperationLimit());
        workplace.getCategoryLimits().add(operationTypeLimit);
      }
    }
    workplace.setCashboxDeviceId(message.getCashboxOnBoardDevice());
  }

  /**
   * Возвращает объект запроса информации по рабочему месту.
   */
  public static SrvGetWorkPlaceInfoRq requestGetWorkPlace(String workPlaceId) {
    SrvGetWorkPlaceInfoRq request = new SrvGetWorkPlaceInfoRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvGetWorkPlaceInfoRqMessage(
        new SrvGetWorkPlaceInfoRq.SrvGetWorkPlaceInfoRqMessage());
    request.getSrvGetWorkPlaceInfoRqMessage().setWorkPlaceUId(workPlaceId);
    return request;
  }

  /**
   * Преобразует транспортный объект рабочего места во внутреннюю сущность.
   */
  public static Workplace convert(SrvGetWorkPlaceInfoRs response) {
    Workplace workplace = new Workplace();
    map(response.getHeaderInfo(), workplace);
    map(response.getSrvGetWorkPlaceInfoRsMessage(), workplace);
    return workplace;
  }

  //MapStruct https://russianblogs.com/article/74641633114/#1MapStruct_3
  /**
   * Возвращает объект запроса информации по рабочему месту.
   */
  public static SrvGetWorkPlaceInfoRq requestGetWorkPlaceMapStruct(String workPlaceId) {
    SrvGetWorkPlaceInfoRq request = new SrvGetWorkPlaceInfoRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvGetWorkPlaceInfoRqMessage(mapper.map(workPlaceId));
    return request;
  }

  /**
   * Преобразует транспортный объект рабочего места во внутреннюю сущность.
   */
  public static Workplace convertMapStruct(SrvGetWorkPlaceInfoRs response) {
    Workplace workplace = mapper
        .convert(response.getSrvGetWorkPlaceInfoRsMessage());
    map(response.getHeaderInfo(), workplace);
    return workplace;
  }
}
