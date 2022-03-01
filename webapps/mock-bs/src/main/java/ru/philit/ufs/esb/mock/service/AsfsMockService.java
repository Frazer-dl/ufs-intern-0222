package ru.philit.ufs.esb.mock.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.philit.ufs.esb.MessageProcessor;
import ru.philit.ufs.esb.mock.client.EsbClient;
import ru.philit.ufs.model.cache.MockCache;
import ru.philit.ufs.model.converter.esb.JaxbConverter;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderStatusType;
import ru.philit.ufs.model.entity.esb.asfs.CashOrderType;
import ru.philit.ufs.model.entity.esb.asfs.HeaderInfoType;
import ru.philit.ufs.model.entity.esb.asfs.LimitStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCreateCashOrderRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs.SrvGetWorkPlaceInfoRsMessage;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvUpdStCashOrderRs;


/**
 * Сервис на обработку запросов к АСФС.
 */
@Service
public class AsfsMockService extends CommonMockService implements MessageProcessor {
  private static final String CONTEXT_PATH = "ru.philit.ufs.model.entity.esb.asfs";

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final EsbClient esbClient;
  private final MockCache mockCache;

  private final JaxbConverter jaxbConverter = new JaxbConverter(CONTEXT_PATH);

  @Autowired
  public AsfsMockService(EsbClient esbClient, MockCache mockCache) {
    this.esbClient = esbClient;
    this.mockCache = mockCache;
  }

  @PostConstruct
  public void init() {
    esbClient.addMessageProcessor(this);
    logger.info("{} started", this.getClass().getSimpleName());
  }

  @Override
  public boolean processMessage(String requestMessage) {
    try {
      Object request = jaxbConverter.getObject(requestMessage);
      logger.debug("Received message: {}", request);
      if (request != null) {
        if (request instanceof SrvCheckOverLimitRq) {
          sendResponse(getResponse((SrvCheckOverLimitRq) request));

        } else if (request instanceof SrvCreateCashOrderRq) {
          sendResponse(getResponse((SrvCreateCashOrderRq) request));

        } else if (request instanceof SrvGetWorkPlaceInfoRq) {
          sendResponse(getResponse((SrvGetWorkPlaceInfoRq) request));

        } else if (request instanceof SrvUpdStCashOrderRq) {
          sendResponse(getResponse((SrvUpdStCashOrderRq) request));
        }
        return true;
      }
    } catch (JAXBException e) {
      // this message can not be processed this processor
      logger.trace("this message can not be processed this processor", e);
    }
    return false;
  }

  private void sendResponse(Object responseObject) throws JAXBException {
    String responseMessage = jaxbConverter.getXml(responseObject);
    esbClient.sendMessage(responseMessage);
  }

  private SrvCheckOverLimitRs getResponse(SrvCheckOverLimitRq request) {
    SrvCheckOverLimitRs response = new SrvCheckOverLimitRs();
    response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
    response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
    response.setSrvCheckOverLimitRsMessage(new SrvCheckOverLimitRs.SrvCheckOverLimitRsMessage());
    String login = request.getSrvCheckOverLimitRqMessage().getUserLogin();
    response.getSrvCheckOverLimitRsMessage().setResponseCode("200");
    response.getSrvCheckOverLimitRsMessage()
        .setStatus(mockCache.checkOverLimit(login, new Date()) ? LimitStatusType.LIMIT_PASSED :
            LimitStatusType.LIMIT_ERROR);
    return response;
  }

  private SrvCreateCashOrderRs getResponse(SrvCreateCashOrderRq request) {
    SrvCreateCashOrderRs response = new SrvCreateCashOrderRs();
    response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
    response.setSrvCreateCashOrderRsMessage(new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage());
    response.getSrvCreateCashOrderRsMessage().setAccountId("12344");
    response.getSrvCreateCashOrderRsMessage().setAmount(BigDecimal.valueOf(10000));
    response.getSrvCreateCashOrderRsMessage().setCashOrderStatus(CashOrderStatusType.CREATED);
    response.getSrvCreateCashOrderRsMessage().setUserPosition("1");
    response.getSrvCreateCashOrderRsMessage().setUserFullName("Ivanov Ivan Ivanovich");
    response.getSrvCreateCashOrderRsMessage().setOperationId("1");
    response.getSrvCreateCashOrderRsMessage().setFDestLEName("123");
    response.getSrvCreateCashOrderRsMessage().setClientTypeFK(true);
    response.getSrvCreateCashOrderRsMessage().setRecipientBank("Bank1");
    response.getSrvCreateCashOrderRsMessage().setRecipientBankBIC("111111");
    response.getSrvCreateCashOrderRsMessage().setSenderBank("Bank2");
    response.getSrvCreateCashOrderRsMessage().setSenderBankBIC("222222");
    response.getSrvCreateCashOrderRsMessage().setRepFIO("Ivanov Ivan Ivanovich");
    SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols cashSymbols
        = new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols();
    SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem cashSymbolItem
        = new SrvCreateCashOrderRs.SrvCreateCashOrderRsMessage.CashSymbols.CashSymbolItem();
    cashSymbolItem.setCashSymbol("DC");
    cashSymbolItem.setCashSymbolAmount(BigDecimal.valueOf(10000));
    cashSymbols.getCashSymbolItem().add(cashSymbolItem);
    response.getSrvCreateCashOrderRsMessage().setCashSymbols(cashSymbols);
    response.getSrvCreateCashOrderRsMessage().setResponseCode("0");
    response.getSrvCreateCashOrderRsMessage().setResponseMsg("");
    response.getSrvCreateCashOrderRsMessage().setCashOrderId("12345");
    response.getSrvCreateCashOrderRsMessage().setCashOrderINum("");
    response.getSrvCreateCashOrderRsMessage()
        .setCreatedDttm(xmlCalendar(2022, 2, 25, 12, 43));
    response.getSrvCreateCashOrderRsMessage().setINN("1234567890");
    response.getSrvCreateCashOrderRsMessage().setCashOrderINum("55555");
    response.getSrvCreateCashOrderRsMessage().setCashOrderType(CashOrderType.KO_1);
    response.getSrvCreateCashOrderRsMessage().setLegalEntityShortName("");
    response.getSrvCreateCashOrderRsMessage().setResponseMsg("200");
    response.getSrvCreateCashOrderRsMessage().setResponseCode("message");
    Date date = date(response.getSrvCreateCashOrderRsMessage().getCreatedDttm());
    mockCache.crCashOrder(response.getSrvCreateCashOrderRsMessage().getCashOrderId(),
        response.getSrvCreateCashOrderRsMessage(), date);
    return response;
  }

  private SrvGetWorkPlaceInfoRs getResponse(SrvGetWorkPlaceInfoRq request) {
    SrvGetWorkPlaceInfoRs response = new SrvGetWorkPlaceInfoRs();
    response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
    response.setSrvGetWorkPlaceInfoRsMessage(new SrvGetWorkPlaceInfoRsMessage());
    response.getSrvGetWorkPlaceInfoRsMessage().setWorkPlaceType(BigInteger.valueOf(0));
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxOnBoard(false);
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxOnBoardDevice("DC");
    response.getSrvGetWorkPlaceInfoRsMessage().setSubbranchCode("770351");
    response.getSrvGetWorkPlaceInfoRsMessage().setCurrentCurrencyType("USD");
    response.getSrvGetWorkPlaceInfoRsMessage().setAmount(BigDecimal.valueOf(100));
    response.getSrvGetWorkPlaceInfoRsMessage().setWorkPlaceLimit(BigDecimal.TEN);
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxDeviceType("DC");
    return response;
  }

  private SrvUpdStCashOrderRs getResponse(SrvUpdStCashOrderRq request) {
    SrvUpdStCashOrderRs response = new SrvUpdStCashOrderRs();
    response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
    response.setSrvUpdCashOrderRsMessage(new SrvUpdStCashOrderRs.SrvUpdCashOrderRsMessage());
    String cashOrderId = request.getSrvUpdCashOrderRqMessage().getCashOrderId();
    CashOrderStatusType statusType = request.getSrvUpdCashOrderRqMessage().getCashOrderStatus();
    response.getSrvUpdCashOrderRsMessage().setCashOrderType(CashOrderType.KO_1);
    response.getSrvUpdCashOrderRsMessage().setCashOrderStatus(statusType);
    response.getSrvUpdCashOrderRsMessage().setCashOrderINum("55555");
    response.getSrvUpdCashOrderRsMessage().setCashOrderId(cashOrderId);
    response.getSrvUpdCashOrderRsMessage().setResponseMsg("message");
    response.getSrvUpdCashOrderRsMessage().setResponseCode("200");
    mockCache.updStCashOrder(cashOrderId, statusType);
    return response;
  }

  private HeaderInfoType copyHeaderInfo(HeaderInfoType headerInfo0) {
    HeaderInfoType headerInfo = new HeaderInfoType();
    headerInfo.setRqUID(headerInfo0.getRqUID());
    headerInfo.setRqTm(headerInfo0.getRqTm());
    headerInfo.setSpName(headerInfo0.getSystemId());
    headerInfo.setSystemId(headerInfo0.getSpName());
    return headerInfo;
  }
}
