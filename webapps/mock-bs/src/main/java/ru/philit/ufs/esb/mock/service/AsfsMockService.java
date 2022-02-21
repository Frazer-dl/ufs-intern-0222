package ru.philit.ufs.esb.mock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.philit.ufs.esb.MessageProcessor;
import ru.philit.ufs.esb.mock.client.EsbClient;
import ru.philit.ufs.model.converter.esb.JaxbConverter;
import ru.philit.ufs.model.entity.esb.asfs.*;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

/**
 * Сервис на обработку запросов к АСФС.
 */
@Service
public class AsfsMockService extends CommonMockService implements MessageProcessor {

    private static final String CONTEXT_PATH = "ru.philit.ufs.model.entity.esb.asfs";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EsbClient esbClient;

    private final JaxbConverter jaxbConverter = new JaxbConverter(CONTEXT_PATH);

    @Autowired
    public AsfsMockService(EsbClient esbClient) {
        this.esbClient = esbClient;
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

                } else if (request instanceof SrvGetCashOrderRq) {
                    sendResponse(getResponse((SrvGetCashOrderRq) request));
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
        return response;
    }

    private SrvCreateCashOrderRs getResponse(SrvCreateCashOrderRq request) {
        SrvCreateCashOrderRs response = new SrvCreateCashOrderRs();
        response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
        return response;
    }

    private SrvGetWorkPlaceInfoRs getResponse(SrvGetWorkPlaceInfoRq request) {
        SrvGetWorkPlaceInfoRs response = new SrvGetWorkPlaceInfoRs();
        response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
        return response;
    }

    private SrvUpdStCashOrderRs getResponse(SrvUpdStCashOrderRq request) {
        SrvUpdStCashOrderRs response = new SrvUpdStCashOrderRs();
        response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
        return response;
    }

    private SrvGetCashOrderRs getResponse(SrvGetCashOrderRq request) {
        SrvGetCashOrderRs response = new SrvGetCashOrderRs();
        response.setHeaderInfo(copyHeaderInfo(request.getHeaderInfo()));
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
