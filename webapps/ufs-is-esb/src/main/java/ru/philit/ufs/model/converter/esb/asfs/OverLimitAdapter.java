package ru.philit.ufs.model.converter.esb.asfs;

import ru.philit.ufs.model.entity.common.ExternalEntityContainer;
import ru.philit.ufs.model.entity.esb.asfs.LimitStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs;
import ru.philit.ufs.model.entity.oper.Limit;

public class OverLimitAdapter extends AsfsAdapter {

  private static boolean limitStatusType(LimitStatusType limitStatusType) {
    return (limitStatusType.value().equals(LimitStatusType.LIMIT_PASSED.value()));
  }

  private static void map(SrvCheckOverLimitRq.SrvCheckOverLimitRqMessage message, Limit limit) {
    message.setUserLogin(limit.getUserLogin());
    message.setAmount(limit.getAmount());
    message.setTobeIncreased(limit.isTobeIncreased());
  }

  private static void map(SrvCheckOverLimitRs.SrvCheckOverLimitRsMessage message,
      ExternalEntityContainer<Boolean> container) {
    container.setData(limitStatusType(message.getStatus()));
    container.setResponseCode(message.getResponseCode());
  }

  /**
   * Возвращает объект запроса проверки операции сверх лимита.
   */
  public static SrvCheckOverLimitRq requestOverLimit(Limit limit) {
    SrvCheckOverLimitRq request = new SrvCheckOverLimitRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvCheckOverLimitRqMessage(new SrvCheckOverLimitRq.SrvCheckOverLimitRqMessage());
    map(request.getSrvCheckOverLimitRqMessage(), limit);
    return request;
  }

  /**
   * Преобразует транспортный объект ответа во внутреннюю сущность.
   */
  public static ExternalEntityContainer<Boolean> convert(SrvCheckOverLimitRs response) {
    ExternalEntityContainer<Boolean> container = new ExternalEntityContainer<>();
    container.setResponseCode(response.getSrvCheckOverLimitRsMessage().getResponseCode());
    container.setData(limitStatusType(response.getSrvCheckOverLimitRsMessage().getStatus()));
    map(response.getHeaderInfo(), container);
    map(response.getSrvCheckOverLimitRsMessage(), container);
    return container;
  }
}
