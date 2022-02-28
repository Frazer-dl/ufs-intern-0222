package ru.philit.ufs.model.entity.oper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.philit.ufs.model.entity.account.Representative;
import ru.philit.ufs.model.entity.common.ExternalEntity;
import ru.philit.ufs.model.entity.common.OperationTypeCode;
import ru.philit.ufs.model.entity.user.Subbranch;

@EqualsAndHashCode(of = {"cashOrderId"}, callSuper = false)
@ToString
@Getter
@Setter
public class CashOrder extends ExternalEntity {

  private String cashOrderId;
  private OperationTypeCode operationType;
  private String cashOrderINum;
  private String accountId;
  private BigDecimal amount;
  private String amountInWords;
  private String currencyType;
  private CashOrderStatus cashOrderStatus;
  private String workPlaceUId;
  private Representative representative;
  private Subbranch recipientBank;
  private Subbranch senderBank;
  private String responseCode;
  private String responseMsg;
  private CashOrderType cashOrderType;
  private Date createdDttm;
  private String operationId;
  private String legalEntityShortName;
  private List<CashSymbol> cashSymbols;
  private Boolean clientTypeFk;
  private String fdestLeName;
  private String userLogin;
  private String userFullName;
  private String userPosition;
  private String account20202Num;

}
