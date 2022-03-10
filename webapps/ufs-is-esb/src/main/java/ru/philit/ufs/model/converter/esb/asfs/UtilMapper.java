package ru.philit.ufs.model.converter.esb.asfs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;
import ru.philit.ufs.model.entity.common.OperationTypeCode;
import ru.philit.ufs.model.entity.esb.asfs.LimitStatusType;
import ru.philit.ufs.model.entity.esb.asfs.OperTypeLabel;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.oper.CashOrderType;
import ru.philit.ufs.model.entity.oper.CashSymbol;
import ru.philit.ufs.model.entity.oper.OperationTypeLimit;
import ru.philit.ufs.model.entity.user.WorkplaceType;

@Component
public class UtilMapper {

  protected static CashOrderType map(
      ru.philit.ufs.model.entity.esb.asfs.CashOrderType cashOrderType) {
    return (cashOrderType != null) ? CashOrderType.getByCode(cashOrderType.value()) : null;
  }


  protected static OperTypeLabel map(OperationTypeCode operationTypeCode) {
    return (operationTypeCode != null) ? OperTypeLabel.fromValue(operationTypeCode.code()) : null;
  }

  protected static boolean map(LimitStatusType limitStatusType) {
    return (limitStatusType.value().equals(LimitStatusType.LIMIT_PASSED.value()));
  }

  protected static List<OperationTypeLimit> map(List<SrvGetWorkPlaceInfoRs
      .SrvGetWorkPlaceInfoRsMessage.WorkPlaceOperationTypeLimit.OperationTypeLimitItem> list) {
    List<OperationTypeLimit> result = new ArrayList<>();
    if (list != null) {
      result = list.stream()
          .map(o -> {
            OperationTypeLimit operationTypeLimit = new OperationTypeLimit();
            operationTypeLimit.setCategoryId(o.getOperationCategory().toString());
            operationTypeLimit.setLimit(o.getOperationLimit());
            return operationTypeLimit;
          })
          .collect(Collectors.toList());
    }
    return result;
  }

  protected static Date map(XMLGregorianCalendar xmlCalendar) {
    return (xmlCalendar != null) ? xmlCalendar.toGregorianCalendar().getTime() : null;
  }

  protected static WorkplaceType map(BigInteger value) {
    switch (value.intValue()) {
      case 0:
        return WorkplaceType.CASHBOX;
      case 1:
        return WorkplaceType.UWP;
      case 2:
        return WorkplaceType.OTHER;
      default:
        return null;
    }
  }

  protected static List<CashSymbol> map(ru.philit.ufs.model.entity.esb
      .asfs.SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols cashSymbols) {
    return cashSymbols.getCashSymbolItem().stream().map(c -> {
      CashSymbol cashSymbol = new CashSymbol();
      cashSymbol.setAmount(c.getCashSymbolAmount());
      cashSymbol.setDescription(c.getCashSymbol());
      cashSymbol.setCode(c.getCashSymbol());
      return cashSymbol;
    }).collect(Collectors.toList());
  }
}
