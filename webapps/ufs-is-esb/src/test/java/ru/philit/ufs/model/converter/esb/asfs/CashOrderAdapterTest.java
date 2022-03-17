package ru.philit.ufs.model.converter.esb.asfs;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.philit.ufs.model.converter.esb.asfs.mapstruct.CashOrderAdapterMapStruct;
import ru.philit.ufs.model.entity.account.Representative;
import ru.philit.ufs.model.entity.common.ExternalEntityList;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs.SrvGetCashOrderRsMessage;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderRequest;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashOrderType;
import ru.philit.ufs.model.entity.oper.CashSymbol;
import ru.philit.ufs.model.entity.user.Subbranch;

public class CashOrderAdapterTest extends AsfsAdapterTest {

  private static final String FIX_UUID = "a55ed415-3976-41f7-916c-4c17ca79e969";

  private CashOrder cashOrder;
  private CashOrderRequest cashOrderRequest;
  private SrvCreateCashOrderRs response1;
  private SrvUpdStCashOrderRs response2;
  private SrvGetCashOrderRs response3;

  /**
   * Set up test data.
   */
  @Before
  public void setUp() {
    cashOrder = new CashOrder();
    cashOrder.setCashOrderStatus(CashOrderStatus.CREATED);
    cashOrder.setCashOrderId("12345");
    cashOrder.setAccountId("12344");
    cashOrder.setAmount(BigDecimal.TEN);
    cashOrder.setCashOrderINum("55555");
    cashOrder.setUserFullName("Ivanov Ivan Ivanovich");
    cashOrder.setFdestLeName("123");
    cashOrder.setCashOrderType(CashOrderType.KO_1);
    cashOrder.setCashSymbols(new ArrayList<>());
    CashSymbol cashSymbol = new CashSymbol();
    cashSymbol.setAmount(BigDecimal.TEN);
    cashSymbol.setCode("DC");
    cashOrder.getCashSymbols().add(cashSymbol);
    cashOrder.setCreatedDttm(date(2022, 2, 25, 12, 43));
    cashOrder.setLegalEntityShortName("OOO");
    cashOrder.setOperationId("1");
    Subbranch recipientBank = new Subbranch();
    recipientBank.setBankName("Bank1");
    recipientBank.setBic("222222");
    recipientBank.setGosbCode("123456");
    recipientBank.setOsbCode("1234");
    recipientBank.setSubbranchCode("3331");
    recipientBank.setTbCode("6543");
    recipientBank.setVspCode("21");
    Subbranch senderBank = new Subbranch();
    senderBank.setBankName("Bank2");
    senderBank.setBic("111111");
    senderBank.setGosbCode("123456");
    senderBank.setOsbCode("1234");
    senderBank.setSubbranchCode("3332");
    senderBank.setTbCode("2222");
    senderBank.setVspCode("444");
    cashOrder.setRecipientBank(recipientBank);
    cashOrder.setSenderBank(senderBank);
    cashOrder.setResponseCode("200");
    cashOrder.setResponseMsg("message");
    cashOrder.setClientTypeFk(false);
    cashOrder.setUserPosition("position");
    Representative repData = new Representative();
    repData.setAddress("Moscow, Arbat 12");
    repData.setBirthDate(date(1989, 5, 7, 12, 33));
    repData.setInn("1234567890");
    repData.setPlaceOfBirth("Moscow");
    repData.setFirstName("Ivan");
    repData.setPatronymic("Ivanovich");
    repData.setLastName("Ivanov");
    repData.setId("1");
    repData.setResident(false);
    cashOrder.setRepresentative(repData);
    cashOrderRequest = new CashOrderRequest();
    cashOrderRequest.setFromDate(date(2022, 3, 1, 0, 0));
    cashOrderRequest.setToDate(date(2022, 3, 31, 0, 0));


    CashOrderItem cashOrderItem = new CashOrderItem();
    cashOrderItem.setAccountId("12344");
    cashOrderItem.setAmount(BigDecimal.valueOf(1000));
    cashOrderItem.setCashOrderStatus(CashOrderStatusType.CREATED);
    cashOrderItem.setUserPosition("position");
    cashOrderItem.setUserFullName("Ivanov Ivan Ivanovich");
    cashOrderItem.setOperationId("1");
    cashOrderItem.setFDestLEName("123");
    cashOrderItem.setClientTypeFK(false);
    cashOrderItem.setRecipientBank("Bank1");
    cashOrderItem.setRecipientBankBIC("111111");
    cashOrderItem.setSenderBank("Bank2");
    cashOrderItem.setSenderBankBIC("222222");
    cashOrderItem.setLegalEntityShortName("OOO");
    cashOrderItem.setRepFIO("Ivanov Ivan Ivanovich");
    SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem.CashSymbols cashSymbol2
        = new SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem.CashSymbols();
    SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem
        .CashSymbols.CashSymbolItem cashSymbolItem2
        = new SrvGetCashOrderRs.SrvGetCashOrderRsMessage.CashOrderItem
        .CashSymbols.CashSymbolItem();
    cashSymbolItem2.setCashSymbol("DC");
    cashSymbolItem2.setCashSymbolAmount(BigDecimal.TEN);
    cashSymbol2.getCashSymbolItem().add(cashSymbolItem2);
    cashOrderItem.setCashSymbols(cashSymbol2);
    cashOrderItem.setCreatedDttm(xmlCalendar(2022, 2, 25, 12, 43));
    cashOrderItem.setINN("1234567890");
    cashOrderItem.setCashOrderINum("55555");
    cashOrderItem.setCashOrderType(ru.philit.ufs.model.entity.esb.asfs.CashOrderType.KO_1);
    cashOrderItem.setResponseMsg("200");
    cashOrderItem.setResponseCode("message");

    response1 = new SrvCreateCashOrderRs();
    response1.setHeaderInfo(headerInfo(FIX_UUID));
    response1.setSrvCreateCashOrderRsMessage(
        new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage());
    response1.getSrvCreateCashOrderRsMessage().setAccountId("12344");
    response1.getSrvCreateCashOrderRsMessage().setAmount(BigDecimal.valueOf(1000));
    response1.getSrvCreateCashOrderRsMessage().setCashOrderStatus(CashOrderStatusType.CREATED);
    response1.getSrvCreateCashOrderRsMessage().setUserPosition("position");
    response1.getSrvCreateCashOrderRsMessage().setUserFullName("Ivanov Ivan Ivanovich");
    response1.getSrvCreateCashOrderRsMessage().setOperationId("1");
    response1.getSrvCreateCashOrderRsMessage().setFDestLEName("123");
    response1.getSrvCreateCashOrderRsMessage().setClientTypeFK(false);
    response1.getSrvCreateCashOrderRsMessage().setRecipientBank("Bank1");
    response1.getSrvCreateCashOrderRsMessage().setRecipientBankBIC("111111");
    response1.getSrvCreateCashOrderRsMessage().setSenderBank("Bank2");
    response1.getSrvCreateCashOrderRsMessage().setSenderBankBIC("222222");
    response1.getSrvCreateCashOrderRsMessage().setLegalEntityShortName("OOO");
    response1.getSrvCreateCashOrderRsMessage().setRepFIO("Ivanov Ivan Ivanovich");
    SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols cashSymbols1
        = new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols();
    SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem cashSymbolItem1
        = new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem();
    cashSymbolItem1.setCashSymbol("DC");
    cashSymbolItem1.setCashSymbolAmount(BigDecimal.TEN);
    cashSymbols1.getCashSymbolItem().add(cashSymbolItem1);
    response1.getSrvCreateCashOrderRsMessage().setCashSymbols(cashSymbols1);
    response1.getSrvCreateCashOrderRsMessage()
        .setCreatedDttm(xmlCalendar(2022, 2, 25, 12, 43));
    response1.getSrvCreateCashOrderRsMessage().setINN("1234567890");
    response1.getSrvCreateCashOrderRsMessage().setCashOrderINum("55555");
    response1.getSrvCreateCashOrderRsMessage()
        .setCashOrderType(ru.philit.ufs.model.entity.esb.asfs.CashOrderType.KO_1);
    response1.getSrvCreateCashOrderRsMessage().setResponseMsg("200");
    response1.getSrvCreateCashOrderRsMessage().setResponseCode("message");

    response2 = new SrvUpdStCashOrderRs();
    response2.setHeaderInfo(headerInfo(FIX_UUID));
    response2.setSrvUpdCashOrderRsMessage(new SrvUpdStCashOrderRs.SrvUpdCashOrderRsMessage());
    response2.getSrvUpdCashOrderRsMessage()
        .setCashOrderType(ru.philit.ufs.model.entity.esb.asfs.CashOrderType.KO_1);
    response2.getSrvUpdCashOrderRsMessage().setCashOrderStatus(CashOrderStatusType.COMMITTED);
    response2.getSrvUpdCashOrderRsMessage().setCashOrderINum("55555");
    response2.getSrvUpdCashOrderRsMessage().setCashOrderId("12345");
    response2.getSrvUpdCashOrderRsMessage().setResponseMsg("200");
    response2.getSrvUpdCashOrderRsMessage().setResponseCode("8888");

    response3 = new SrvGetCashOrderRs();
    response3.setHeaderInfo(headerInfo(FIX_UUID));
    response3.setSrvGetCashOrderRsMessage(new SrvGetCashOrderRsMessage());
    response3.getSrvGetCashOrderRsMessage().getCashOrderItem().add(cashOrderItem);
  }

  @Test
  public void testRequestCreateCashOrder() {
    SrvCreateCashOrderRq request = CashOrderAdapter.requestCreateCashOrder(cashOrder);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvCreateCashOrderRqMessage());
    Assert.assertEquals(request.getSrvCreateCashOrderRqMessage().getAccountId(),
        cashOrder.getAccountId());
  }

  @Test
  public void testRequestUpdateCashOrder() {
    SrvUpdStCashOrderRq request = CashOrderAdapter.requestUpdCashOrder(cashOrder);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvUpdCashOrderRqMessage());
    Assert.assertEquals(request.getSrvUpdCashOrderRqMessage().getCashOrderId(), "12345");
    Assert.assertEquals(request.getSrvUpdCashOrderRqMessage().getCashOrderStatus(),
        CashOrderStatusType.CREATED);
  }

  @Test
  public void testRequestCreateCashOrderMapStruct() {
    SrvCreateCashOrderRq request = CashOrderAdapterMapStruct.requestCreateOrderMapStruct(cashOrder);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvCreateCashOrderRqMessage());
    Assert.assertEquals(request.getSrvCreateCashOrderRqMessage().getAccountId(),
        cashOrder.getAccountId());
  }

  @Test
  public void testRequestUpdStCashOrderMapStruct() {
    SrvUpdStCashOrderRq request
        = CashOrderAdapterMapStruct.requestUpdStCashOrderMapStruct(cashOrder);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvUpdCashOrderRqMessage());
    Assert.assertEquals(request.getSrvUpdCashOrderRqMessage().getCashOrderId(), "12345");
    Assert.assertEquals(request.getSrvUpdCashOrderRqMessage().getCashOrderStatus(),
        CashOrderStatusType.CREATED);
  }

  @Test
  public void testRequestGetCashOrder() {
    SrvGetCashOrderRq request = CashOrderAdapter.requestGetCashOrder(cashOrderRequest);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvGetCashOrderRqMessage());
    Assert.assertEquals(request.getSrvGetCashOrderRqMessage().getCreatedFrom(),
        xmlCalendar(2022, 3, 1, 0, 0));
  }

  @Test
  public void testRequestGetCashOrderMapStruct() {
    SrvGetCashOrderRq request =
        CashOrderAdapterMapStruct.requestGetCashOrderMapStruct(cashOrderRequest);
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvGetCashOrderRqMessage());
    Assert.assertEquals(request.getSrvGetCashOrderRqMessage().getCreatedFrom(),
        xmlCalendar(2022, 3, 1, 0, 0));
  }

  @Test
  public void testConverterCreateCashOrder() {
    CashOrder cashOrder = CashOrderAdapter.convert(response1);
    assertHeaderInfo(cashOrder, FIX_UUID);
    Assert.assertEquals(cashOrder.getAccountId(), response1.getSrvCreateCashOrderRsMessage()
        .getAccountId());
    Assert.assertEquals(cashOrder.getCashOrderId(), response1.getSrvCreateCashOrderRsMessage()
        .getCashOrderId());
  }

  @Test
  public void testConverterUpdateCashOrder() {
    CashOrder cashOrder = CashOrderAdapter.convert(response2);
    assertHeaderInfo(cashOrder, FIX_UUID);
    Assert.assertEquals(cashOrder.getCashOrderId(), response2.getSrvUpdCashOrderRsMessage()
        .getCashOrderId());
    Assert.assertEquals(cashOrder.getCashOrderStatus(), CashOrderStatus.COMMITTED);
  }

  @Test
  public void testConverterCreateCashOrderMapStruct() {
    CashOrder cashOrder = CashOrderAdapterMapStruct.convertMapStruct(response1);
    assertHeaderInfo(cashOrder, FIX_UUID);
    Assert.assertEquals(cashOrder.getAccountId(), response1.getSrvCreateCashOrderRsMessage()
        .getAccountId());
    Assert.assertEquals(cashOrder.getCashOrderId(), response1.getSrvCreateCashOrderRsMessage()
        .getCashOrderId());
  }

  @Test
  public void testConverterUpdStMapStruct() {
    CashOrder cashOrder = CashOrderAdapterMapStruct.convertMapStruct(response2);
    assertHeaderInfo(cashOrder, FIX_UUID);
    Assert.assertEquals(cashOrder.getCashOrderId(), response2.getSrvUpdCashOrderRsMessage()
        .getCashOrderId());
    Assert.assertEquals(cashOrder.getCashOrderStatus(), CashOrderStatus.COMMITTED);
  }

  @Test
  public void testConverterGetCashOrder() {
    ExternalEntityList<CashOrder> list = CashOrderAdapter.convert(response3);
    assertHeaderInfo(list, FIX_UUID);
    Assert.assertEquals(list.getItems().get(0).getCashOrderId(),
        response3.getSrvGetCashOrderRsMessage().getCashOrderItem().get(0).getCashOrderId());
  }

  @Test
  public void testConverterGetCashOrderMapStruct() {
    ExternalEntityList<CashOrder> list = CashOrderAdapterMapStruct.convertMapStruct(response3);
    assertHeaderInfo(list, FIX_UUID);
    Assert.assertEquals(list.getItems().get(0).getCashOrderId(),
        response3.getSrvGetCashOrderRsMessage().getCashOrderItem().get(0).getCashOrderId());
  }
}
