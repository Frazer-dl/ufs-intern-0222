package ru.philit.ufs.model.converter.esb.asfs.mapstruct;

import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import ru.philit.ufs.model.entity.common.ExternalEntity;
import ru.philit.ufs.model.entity.common.ExternalEntityList;
import ru.philit.ufs.model.entity.common.OperationTypeCode;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderStatusType;
import ru.philit.ufs.model.entity.esb.asfs.HeaderInfoType;
import ru.philit.ufs.model.entity.esb.asfs.OperTypeLabel;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs.SrvGetCashOrderRsMessage;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderRequest;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashOrderType;
import ru.philit.ufs.model.entity.oper.CashSymbol;


@Mapper(componentModel = "spring")
public interface CashOrderAdapterMapper {

  @Mappings({
      @Mapping(source = "cashOrderId", target = "cashOrderId"),
      @Mapping(source = "operationType", target = "operationType"),
      @Mapping(source = "cashOrderINum", target = "cashOrderINum"),
      @Mapping(source = "accountId", target = "accountId"),
      @Mapping(source = "amount", target = "amount"),
      @Mapping(source = "amountInWords", target = "amountInWords"),
      @Mapping(source = "currencyType", target = "currencyType"),
      @Mapping(source = "cashOrderStatus", target = "cashOrderStatus"),
      @Mapping(source = "workPlaceUId", target = "workPlaceUId"),
      @Mapping(source = "representative.id", target = "repData.repID"),
      @Mapping(expression = "java(cashOrder.getRepresentative().getFirstName() + \" \""
          + " + cashOrder.getRepresentative().getLastName() + \" \""
          + " + cashOrder.getRepresentative().getPatronymic())", target = "repData.repFIO"),
      @Mapping(source = "representative.address", target = "repData.address"),
      @Mapping(source = "representative.birthDate", target = "repData.dateOfBirth"),
      @Mapping(source = "recipientBank.subbranchCode", target = "additionalInfo.subbranchCode"),
      @Mapping(source = "recipientBank.tbCode", target = "additionalInfo.TBCode"),
      @Mapping(source = "cashSymbols", target = "additionalInfo.cashSymbols.cashSymbolItem"),
      @Mapping(source = "account20202Num", target = "additionalInfo.account20202Num"),
      @Mapping(source = "userLogin", target = "additionalInfo.userLogin")
  })
  SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage mapCreate(CashOrder cashOrder);

  @Mappings({
      @Mapping(source = "cashOrderId", target = "cashOrderId"),
      @Mapping(source = "cashOrderStatus", target = "cashOrderStatus")
  })
  SrvUpdStCashOrderRq.SrvUpdCashOrderRqMessage mapUpdate(CashOrder cashOrder);

  @Mappings({
      @Mapping(source = "request.fromDate", target = "createdFrom"),
      @Mapping(source = "request.toDate", target = "createdTo")})
      SrvGetCashOrderRq.SrvGetCashOrderRqMessage mapGet(CashOrderRequest request);


  @Mappings({
      @Mapping(source = "cashOrderId", target = "cashOrderId"),
      @Mapping(source = "cashOrderINum", target = "cashOrderINum"),
      @Mapping(source = "accountId", target = "accountId"),
      @Mapping(source = "amount", target = "amount"),
      @Mapping(source = "cashOrderStatus", target = "cashOrderStatus"),
      @Mapping(expression = "java(response.getRepFIO().split(\" \")[0])",
          target = "representative.firstName"),
      @Mapping(expression = "java(response.getRepFIO().split(\" \")[1])",
          target = "representative.lastName"),
      @Mapping(expression = "java(response.getRepFIO().split(\" \")[2])",
          target = "representative.patronymic"),
      @Mapping(source = "senderBank", target = "senderBank.bankName"),
      @Mapping(source = "senderBankBIC", target = "senderBank.bic"),
      @Mapping(source = "recipientBank", target = "recipientBank.bankName"),
      @Mapping(source = "recipientBankBIC", target = "recipientBank.bic"),
      @Mapping(source = "INN", target = "recipientBank.inn"),
      @Mapping(source = "responseCode", target = "responseCode"),
      @Mapping(source = "responseMsg", target = "responseMsg"),
      @Mapping(source = "cashOrderType", target = "cashOrderType"),
      @Mapping(source = "createdDttm", target = "createdDttm"),
      @Mapping(source = "operationId", target = "operationId"),
      @Mapping(source = "legalEntityShortName", target = "legalEntityShortName"),
      @Mapping(source = "cashSymbols.cashSymbolItem", target = "cashSymbols"),
      @Mapping(source = "clientTypeFK", target = "clientTypeFk"),
      @Mapping(source = "FDestLEName", target = "fdestLeName"),
      @Mapping(source = "userFullName", target = "userFullName"),
      @Mapping(source = "userPosition", target = "userPosition")
  })
  CashOrder convert(SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage response);

  @Mappings({
      @Mapping(source = "cashOrderId", target = "cashOrderId"),
      @Mapping(source = "cashOrderINum", target = "cashOrderINum"),
      @Mapping(source = "cashOrderStatus", target = "cashOrderStatus"),
      @Mapping(source = "responseCode", target = "responseCode"),
      @Mapping(source = "responseMsg", target = "responseMsg"),
      @Mapping(source = "cashOrderType", target = "cashOrderType")
  })
  CashOrder convert(SrvUpdStCashOrderRs.SrvUpdCashOrderRsMessage response);

  @Mappings({
      @Mapping(source = "message.cashOrderItem", target = "items")
  })
  ExternalEntityList<CashOrder> convert(SrvGetCashOrderRsMessage message);

  @Mappings({
      @Mapping(source = "cashSymbol", target = "code"),
      @Mapping(source = "cashSymbolAmount", target = "amount")
  })
  CashSymbol map(CashSymbolItem cashSymbolItem);

  @ValueMappings({
      @ValueMapping(source = "CREATED", target = "CREATED"),
      @ValueMapping(source = "COMMITTED", target = "COMMITTED")
  })
  CashOrderStatus map(CashOrderStatusType cashOrderStatusType);

  @ValueMappings({
      @ValueMapping(source = "CREATED", target = "CREATED"),
      @ValueMapping(source = "COMMITTED", target = "COMMITTED")
  })
  CashOrderStatusType map(CashOrderStatus cashOrderStatus);

  @Mappings({
      @Mapping(source = "code", target = "cashSymbol"),
      @Mapping(source = "amount", target = "cashSymbolAmount")
  })
  SrvCreateCashOrderRqMessage
      .AdditionalInfo.CashSymbols.CashSymbolItem map(CashSymbol  cashSymbol);

  @Mappings({
      @Mapping(source = "cashOrderItem.cashOrderId", target = "cashOrderId"),
      @Mapping(source = "cashOrderItem.cashOrderINum", target = "cashOrderINum"),
      @Mapping(source = "cashOrderItem.accountId", target = "accountId"),
      @Mapping(source = "cashOrderItem.amount", target = "amount"),
      @Mapping(source = "cashOrderItem.cashOrderStatus", target = "cashOrderStatus"),
      @Mapping(expression = "java(cashOrderItem.getRepFIO().split(\" \")[0])",
          target = "representative.firstName"),
      @Mapping(expression = "java(cashOrderItem.getRepFIO().split(\" \")[1])",
          target = "representative.lastName"),
      @Mapping(expression = "java(cashOrderItem.getRepFIO().split(\" \")[2])",
          target = "representative.patronymic"),
      @Mapping(source = "cashOrderItem.senderBank", target = "senderBank.bankName"),
      @Mapping(source = "cashOrderItem.senderBankBIC", target = "senderBank.bic"),
      @Mapping(source = "cashOrderItem.recipientBank", target = "recipientBank.bankName"),
      @Mapping(source = "cashOrderItem.recipientBankBIC", target = "recipientBank.bic"),
      @Mapping(source = "cashOrderItem.INN", target = "recipientBank.inn"),
      @Mapping(source = "cashOrderItem.responseCode", target = "responseCode"),
      @Mapping(source = "cashOrderItem.responseMsg", target = "responseMsg"),
      @Mapping(source = "cashOrderItem.cashOrderType", target = "cashOrderType"),
      @Mapping(source = "cashOrderItem.createdDttm", target = "createdDttm"),
      @Mapping(source = "cashOrderItem.operationId", target = "operationId"),
      @Mapping(source = "cashOrderItem.legalEntityShortName", target = "legalEntityShortName"),
      @Mapping(source = "cashOrderItem.cashSymbols.cashSymbolItem", target = "cashSymbols"),
      @Mapping(source = "cashOrderItem.clientTypeFK", target = "clientTypeFk"),
      @Mapping(source = "cashOrderItem.FDestLEName", target = "fdestLeName"),
      @Mapping(source = "cashOrderItem.userFullName", target = "userFullName"),
      @Mapping(source = "cashOrderItem.userPosition", target = "userPosition")
  })
  CashOrder map(CashOrderItem cashOrderItem);

  @Mappings({
      @Mapping(source = "cashSymbolItem.cashSymbolAmount", target = "amount"),
      @Mapping(source = "cashSymbolItem.cashSymbol", target = "code")
  })
  CashSymbol map(SrvGetCashOrderRsMessage.CashOrderItem.CashSymbols.CashSymbolItem cashSymbolItem);

  default CashOrderType map(
      ru.philit.ufs.model.entity.esb.asfs.CashOrderType cashOrderType) {
    return (cashOrderType != null) ? CashOrderType.getByCode(cashOrderType.value()) : null;
  }

  default OperTypeLabel map(OperationTypeCode operationTypeCode) {
    return (operationTypeCode != null) ? OperTypeLabel.fromValue(operationTypeCode.code()) : null;
  }

  default void headerInfoType(HeaderInfoType headerInfo, ExternalEntity entity) {
    entity.setRequestUid(headerInfo.getRqUID());
    entity.setReceiveDate(new Date());
  }

}
