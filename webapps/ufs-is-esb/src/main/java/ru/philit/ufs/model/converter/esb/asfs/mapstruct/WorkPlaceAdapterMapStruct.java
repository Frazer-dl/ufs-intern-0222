package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import org.mapstruct.factory.Mappers;
import ru.philit.ufs.model.converter.esb.asfs.AsfsAdapter;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.user.Workplace;

public class WorkPlaceAdapterMapStruct extends AsfsAdapter {

  private static final WorkPlaceAdapterMapper mapper
      = Mappers.getMapper(WorkPlaceAdapterMapper.class);

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
