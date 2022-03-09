package ru.philit.ufs.web.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@SuppressWarnings("serial")
public class GetCashBookReq {
  /**
   * Кассовые операции с даты.
   */
  private String fromDate;
  /**
   * Кассовые операции по дату.
   */
  private String toDate;
}
