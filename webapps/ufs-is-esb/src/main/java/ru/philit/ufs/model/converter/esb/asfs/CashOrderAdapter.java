package ru.philit.ufs.model.converter.esb.asfs;

import java.util.ArrayList;
import java.util.stream.Collectors;
import ru.philit.ufs.model.entity.account.Representative;
import ru.philit.ufs.model.entity.common.ExternalEntityList;
import ru.philit.ufs.model.entity.common.OperationTypeCode;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderStatusType;
import ru.philit.ufs.model.entity.esb.asfs.OperTypeLabel;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderRequest;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashOrderType;
import ru.philit.ufs.model.entity.oper.CashSymbol;
import ru.philit.ufs.model.entity.user.Subbranch;

public class CashOrderAdapter extends AsfsAdapter {

  private static OperTypeLabel operTypeLabel(OperationTypeCode operationTypeCode) {
    return (operationTypeCode != null) ? OperTypeLabel.fromValue(operationTypeCode.code())
        : null;

  }

  private static CashOrderStatusType cashOrderStatusType(CashOrderStatus cashOrderStatus) {
    return (cashOrderStatus != null) ? CashOrderStatusType.fromValue(cashOrderStatus.code())
        : null;
  }

  private static CashOrderStatus cashOrderStatusType(CashOrderStatusType cashOrderStatusType) {
    return (cashOrderStatusType != null) ? CashOrderStatus.getByCode(cashOrderStatusType.value())
        : null;
  }

  private static CashOrderType cashOrderType(
      ru.philit.ufs.model.entity.esb.asfs.CashOrderType cashOrderType) {
    return (cashOrderType != null) ? CashOrderType.getByCode(cashOrderType.value()) : null;
  }

  private static SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.RepData repData(
      Representative representative) {
    SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.RepData repData
        = new SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.RepData();
    repData.setAddress(representative.getAddress());
    repData.setDateOfBirth(xmlCalendar(representative.getBirthDate()));
    repData.setINN(representative.getInn());
    repData.setPlaceOfBirth(representative.getPlaceOfBirth());
    repData.setRepFIO(
        representative.getLastName() + " "
        + representative.getFirstName() + " "
        + representative.getPatronymic()
    );
    repData.setRepID(representative.getId());
    repData.setResident(representative.isResident());
    return repData;
  }

  private static SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.AdditionalInfo additionalInfo(
      Subbranch subbranch, CashOrder cashOrder) {
    SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.AdditionalInfo additionalInfo =
        new SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage.AdditionalInfo();
    additionalInfo.setSubbranchCode(subbranch.getSubbranchCode());
    additionalInfo.setVSPCode(subbranch.getVspCode());
    additionalInfo.setOSBCode(subbranch.getOsbCode());
    additionalInfo.setGOSBCode(subbranch.getGosbCode());
    additionalInfo.setTBCode(subbranch.getTbCode());
    additionalInfo.setUserLogin(cashOrder.getUserLogin());
    additionalInfo.setAccount20202Num(cashOrder.getAccount20202Num());
    return additionalInfo;
  }

  private static void map(SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage message,
      CashOrder cashOrder) {
    message.setCashOrderId(cashOrder.getCashOrderId());
    message.setOperationType(operTypeLabel(cashOrder.getOperationType()));
    message.setCashOrderINum(cashOrder.getCashOrderINum());
    message.setAccountId(cashOrder.getAccountId());
    message.setAmount(cashOrder.getAmount());
    message.setAmountInWords(cashOrder.getAmountInWords());
    message.setCurrencyType(cashOrder.getCurrencyType());
    message.setCashOrderStatus(cashOrderStatusType(cashOrder.getCashOrderStatus()));
    message.setWorkPlaceUId(cashOrder.getWorkPlaceUId());
    Representative representative = cashOrder.getRepresentative();
    if (representative != null) {
      message.setRepData(repData(representative));
    }
    Subbranch subbranch = cashOrder.getRecipientBank();
    if (subbranch != null) {
      message.setAdditionalInfo(additionalInfo(subbranch, cashOrder));
    }
  }

  private static void map(SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage message,
      CashOrder cashOrder) {
    cashOrder.setCashOrderId(message.getCashOrderId());
    cashOrder.setCashOrderINum(message.getCashOrderINum());
    cashOrder.setCashOrderType(cashOrderType(message.getCashOrderType()));
    cashOrder.setCashOrderStatus(cashOrderStatusType(message.getCashOrderStatus()));
    cashOrder.setAccountId(message.getAccountId());
    cashOrder.setAmount(message.getAmount());
    cashOrder.setCashSymbols(new ArrayList<>());
    if (message.getCashSymbols() != null) {
      for (SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem
          item : message.getCashSymbols().getCashSymbolItem()) {
        CashSymbol cashSymbol = new CashSymbol();
        cashSymbol.setCode(item.getCashSymbol());
        cashSymbol.setAmount(item.getCashSymbolAmount());
        cashOrder.getCashSymbols().add(cashSymbol);
      }
    }
    cashOrder.setCreatedDttm(date(message.getCreatedDttm()));
    cashOrder.setFdestLeName(message.getFDestLEName());

    Representative representative = new Representative();
    representative.setInn(message.getINN());
    representative.setLastName(message.getRepFIO().split(" ")[0]);
    representative.setFirstName(message.getRepFIO().split(" ")[1]);
    representative.setPatronymic(message.getRepFIO().split(" ")[2]);
    cashOrder.setRepresentative(representative);
    cashOrder.setLegalEntityShortName(message.getLegalEntityShortName());
    cashOrder.setOperationId(message.getOperationId());
    Subbranch recipientBank = new Subbranch();
    recipientBank.setBankName(message.getRecipientBank());
    recipientBank.setBic(message.getRecipientBankBIC());
    cashOrder.setRecipientBank(recipientBank);
    cashOrder.setResponseCode(message.getResponseCode());
    cashOrder.setResponseMsg(message.getResponseMsg());
    cashOrder.setUserFullName(message.getUserFullName());
    Subbranch senderBank = new Subbranch();
    senderBank.setBankName(message.getRecipientBank());
    senderBank.setBic(message.getRecipientBankBIC());
    cashOrder.setRecipientBank(senderBank);
    cashOrder.setClientTypeFk(message.isClientTypeFK());
    cashOrder.setUserPosition(message.getUserPosition());
  }

  private static void map(SrvUpdStCashOrderRs.SrvUpdCashOrderRsMessage message,
      CashOrder cashOrder) {
    cashOrder.setCashOrderId(message.getCashOrderId());
    cashOrder.setResponseCode(message.getResponseCode());
    cashOrder.setResponseMsg(message.getResponseMsg());
    cashOrder.setCashOrderINum(message.getCashOrderINum());
    cashOrder.setCashOrderStatus(cashOrderStatusType(message.getCashOrderStatus()));
    cashOrder.setCashOrderType(cashOrderType(message.getCashOrderType()));
  }

  private static void map(SrvGetCashOrderRq.SrvGetCashOrderRqMessage message,
      CashOrderRequest cashOrderRequest) {
    message.setCreatedFrom(xmlCalendar(cashOrderRequest.getFromDate()));
    message.setCreatedTo(xmlCalendar(cashOrderRequest.getToDate()));
  }

  private static void map(SrvGetCashOrderRs.SrvGetCashOrderRsMessage message,
      ExternalEntityList<CashOrder> cashOrderList) {
    cashOrderList.setItems(message.getCashOrderItem().stream().map(coi -> {
      CashOrder co = new CashOrder();
      co.setCashOrderId(coi.getCashOrderId());
      co.setCashOrderINum(coi.getCashOrderINum());
      co.setCashOrderType(cashOrderType(coi.getCashOrderType()));
      co.setCashOrderStatus(cashOrderStatusType(coi.getCashOrderStatus()));
      co.setAccountId(coi.getAccountId());
      co.setAmount(coi.getAmount());
      co.setCashSymbols(new ArrayList<>());
      if (coi.getCashSymbols() != null) {
        for (SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem.CashSymbols.CashSymbolItem
            item : coi.getCashSymbols().getCashSymbolItem()) {
          CashSymbol cashSymbol = new CashSymbol();
          cashSymbol.setCode(item.getCashSymbol());
          cashSymbol.setAmount(item.getCashSymbolAmount());
          co.getCashSymbols().add(cashSymbol);
        }
      }
      co.setCreatedDttm(date(coi.getCreatedDttm()));
      co.setFdestLeName(coi.getFDestLEName());

      Representative representative = new Representative();
      representative.setInn(coi.getINN());
      representative.setLastName(coi.getRepFIO().split(" ")[0]);
      representative.setFirstName(coi.getRepFIO().split(" ")[1]);
      representative.setPatronymic(coi.getRepFIO().split(" ")[2]);
      co.setRepresentative(representative);
      co.setLegalEntityShortName(coi.getLegalEntityShortName());
      co.setOperationId(coi.getOperationId());
      Subbranch recipientBank = new Subbranch();
      recipientBank.setBankName(coi.getRecipientBank());
      recipientBank.setBic(coi.getRecipientBankBIC());
      co.setRecipientBank(recipientBank);
      co.setResponseCode(coi.getResponseCode());
      co.setResponseMsg(coi.getResponseMsg());
      co.setUserFullName(coi.getUserFullName());
      Subbranch senderBank = new Subbranch();
      senderBank.setBankName(coi.getRecipientBank());
      senderBank.setBic(coi.getRecipientBankBIC());
      co.setRecipientBank(senderBank);
      co.setClientTypeFk(coi.isClientTypeFK());
      co.setUserPosition(coi.getUserPosition());
      return co;
    }).collect(Collectors.toList()));
  }

  /**
   * Возвращает объект запроса на создание кассового ордера.
   */
  public static SrvCreateCashOrderRq requestCreateCashOrder(CashOrder cashOrder) {
    SrvCreateCashOrderRq request = new SrvCreateCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvCreateCashOrderRqMessage(new SrvCreateCashOrderRq.SrvCreateCashOrderRqMessage());
    map(request.getSrvCreateCashOrderRqMessage(), cashOrder);
    return request;
  }

  /**
   * Возвращает объект запроса на обновление кассового ордера.
   */
  public static SrvUpdStCashOrderRq requestUpdCashOrder(CashOrder cashOrder) {
    SrvUpdStCashOrderRq request = new SrvUpdStCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvUpdCashOrderRqMessage(new SrvUpdStCashOrderRq.SrvUpdCashOrderRqMessage());
    request.getSrvUpdCashOrderRqMessage().setCashOrderId(cashOrder.getCashOrderId());
    request.getSrvUpdCashOrderRqMessage()
        .setCashOrderStatus(cashOrderStatusType(cashOrder.getCashOrderStatus()));
    return request;
  }

  /**
   * Возвращает объект запроса на получение кассовых ордеров.
   */
  public static SrvGetCashOrderRq requestGetCashOrder(CashOrderRequest cashOrderRequest) {
    SrvGetCashOrderRq request = new SrvGetCashOrderRq();
    request.setHeaderInfo(headerInfo());
    request.setSrvGetCashOrderRqMessage(new SrvGetCashOrderRq.SrvGetCashOrderRqMessage());
    map(request.getSrvGetCashOrderRqMessage(), cashOrderRequest);
    return request;
  }

  /**
   * Преобразует транспортный объект кассового ордера во внутреннюю сущность.
   */
  public static CashOrder convert(SrvCreateCashOrderRs response) {
    CashOrder cashOrder = new CashOrder();
    map(response.getHeaderInfo(), cashOrder);
    map(response.getSrvCreateCashOrderRsMessage(), cashOrder);
    return cashOrder;
  }

  /**
   * Преобразует транспортный объект обновленного кассового ордера во внутреннюю сущность.
   */
  public static CashOrder convert(SrvUpdStCashOrderRs response) {
    CashOrder cashOrder = new CashOrder();
    map(response.getHeaderInfo(), cashOrder);
    map(response.getSrvUpdCashOrderRsMessage(), cashOrder);
    return cashOrder;
  }

  /**
   * Преобразует транспортный объект обновленного кассового ордера во внутреннюю сущность.
   */
  public static ExternalEntityList<CashOrder> convert(SrvGetCashOrderRs response) {
    ExternalEntityList<CashOrder> cashOrderList = new ExternalEntityList<>();
    map(response.getHeaderInfo(), cashOrderList);
    map(response.getSrvGetCashOrderRsMessage(), cashOrderList);
    return cashOrderList;
  }
}
