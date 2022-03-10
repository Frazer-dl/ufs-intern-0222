package ru.philit.ufs.model.converter.esb.asfs;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashSymbol;

@Mapper(componentModel = "spring", uses = {UtilMapper.class})
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
}
