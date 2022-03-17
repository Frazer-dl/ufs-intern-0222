package ru.philit.ufs.model.entity.oper;

import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Объект запроса списка кассовых ордеров (условия отбора).
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class CashOrderRequest implements Serializable {

  private Date fromDate;
  private Date toDate;
}

