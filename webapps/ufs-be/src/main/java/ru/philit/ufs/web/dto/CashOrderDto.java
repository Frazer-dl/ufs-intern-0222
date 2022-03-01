package ru.philit.ufs.web.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@SuppressWarnings("serial")
public class CashOrderDto implements Serializable {
  /**
   * id кассового ордера.
   */
  private String id;
  /**
   * Сумма кассового ордера.
   */
  private String amount;
  /**
   * Тип валюты кассового ордера.
   */
  private String currencyType;
  /**
   * Инициатор кассового ордера.
   */
  private RepresentativeDto representativeDto;
  /**
   * Денежные единицы.
   */
  private List<CashSymbolDto> cashSymbols;
  /**
   * Подразделение банка.
   */
  private SubbranchDto subbranchDto;
  /**
   * Имя аккаунта.
   */
  private String account20202Num;
  /**
   * Номер кассового ордера.
   */
  private String cashOrderINum;
  /**
   * Статус кассового ордера.
   */
  private String cashOrderStatus;
}
