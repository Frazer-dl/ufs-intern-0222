package ru.philit.ufs.web.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@SuppressWarnings("serial")
public class GetOverLimitReq {

  /**
   * Сумма операции.
   */
  private String amount;

}
