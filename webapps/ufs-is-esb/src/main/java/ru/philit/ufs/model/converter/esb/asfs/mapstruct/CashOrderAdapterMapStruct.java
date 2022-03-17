package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import org.mapstruct.factory.Mappers;
import ru.philit.ufs.model.converter.esb.asfs.AsfsAdapter;
import ru.philit.ufs.model.entity.common.ExternalEntityList;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderRequest;

public class CashOrderAdapterMapStruct extends AsfsAdapter {

  private static final CashOrderAdapterMapper mapper
      = Mappers.getMapper(CashOrderAdapterMapper.class);

  //MapStruct https://russianblogs.com/article/74641633114/#1MapStruct_3
  /**
   * Возвращает объект запроса на создание кассового ордера.
   */
  public static SrvCreateCashOrderRq requestCreateOrderMapStruct(CashOrder cashOrder) {
    SrvCreateCashOrderRq request = new SrvCreateCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvCreateCashOrderRqMessage(mapper.mapCreate(cashOrder));
    return request;
  }

  /**
   * Возвращает объект запроса на обновление кассового ордера.
   */
  public static SrvUpdStCashOrderRq requestUpdStCashOrderMapStruct(CashOrder cashOrder) {
    SrvUpdStCashOrderRq request = new SrvUpdStCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvUpdCashOrderRqMessage(mapper.mapUpdate(cashOrder));
    return request;
  }

  /**
   * Возвращает объект запроса на взятие кассового ордера.
   */
  public static SrvGetCashOrderRq requestGetCashOrderMapStruct(CashOrderRequest coRequest) {
    SrvGetCashOrderRq request = new SrvGetCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvGetCashOrderRqMessage(mapper.mapGet(coRequest));
    return request;
  }

  /**
   * Преобразует транспортный объект кассового ордера во внутреннюю сущность.
   */
  public static CashOrder convertMapStruct(SrvCreateCashOrderRs response) {
    CashOrder cashOrder = mapper.convert(response.getSrvCreateCashOrderRsMessage());
    map(response.getHeaderInfo(), cashOrder);
    return cashOrder;
  }

  /**
   * Преобразует транспортный объект обновленного кассового ордера во внутреннюю сущность.
   */
  public static CashOrder convertMapStruct(SrvUpdStCashOrderRs response) {
    CashOrder cashOrder = mapper.convert(response.getSrvUpdCashOrderRsMessage());
    map(response.getHeaderInfo(), cashOrder);
    return cashOrder;
  }

  /**
   * Преобразует транспортный объект кассового ордера во внутреннюю сущность.
   */
  public static ExternalEntityList<CashOrder> convertMapStruct(SrvGetCashOrderRs response) {
    ExternalEntityList<CashOrder> entityList = mapper.convert(response.getSrvGetCashOrderRsMessage());
    map(response.getHeaderInfo(), entityList);
    return entityList;
  }
}
