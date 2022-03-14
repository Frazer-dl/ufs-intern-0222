package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import org.mapstruct.factory.Mappers;
import ru.philit.ufs.model.converter.esb.asfs.AsfsAdapter;
import ru.philit.ufs.model.entity.common.ExternalEntityContainer;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs;
import ru.philit.ufs.model.entity.oper.Limit;

public class OverLimitAdapterMapStruct extends AsfsAdapter {

  private static final OverLimitAdapterMapper mapper
      = Mappers.getMapper(OverLimitAdapterMapper.class);

  //MapStruct https://russianblogs.com/article/74641633114/#1MapStruct_3
  /**
   * Возвращает объект запроса проверки операции сверх лимита.
   */
  public static SrvCheckOverLimitRq requestOverLimitMapStruct(Limit limit) {
    SrvCheckOverLimitRq request = new SrvCheckOverLimitRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvCheckOverLimitRqMessage(mapper.map(limit));
    return request;
  }

  /**
   * Преобразует транспортный объект ответа во внутреннюю сущность.
   */
  public static ExternalEntityContainer<Boolean> convertMapStruct(SrvCheckOverLimitRs response) {
    ExternalEntityContainer<Boolean> container = mapper
        .convert(response.getSrvCheckOverLimitRsMessage());
    map(response.getHeaderInfo(), container);
    return container;
  }
}
