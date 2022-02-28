package ru.philit.ufs.esb.mock.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.philit.ufs.esb.mock.client.EsbClient;
import ru.philit.ufs.model.cache.MockCache;

public class AsfsMockServiceTest {

  @Mock
  private EsbClient esbClient;
  @Mock
  private MockCache mockCache;

  private AsfsMockService service;

  /**
   * Set up test data.
   */
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    service = new AsfsMockService(esbClient, mockCache);
  }

  @Test
  public void testInit() throws Exception {
    service.init();
  }

  @Test
  public void testProcessMessage_SrvCrCashOrderRq() throws Exception {
    String requestMessage = "<SrvCreateCashOrderRq><HeaderInfo/>"
        + "<SrvCreateCashOrderRqMessage/></SrvCreateCashOrderRq>";
    assertTrue(service.processMessage(requestMessage));
  }

  @Test
  public void testProcessMessage_SrvUpdStCashOrderRq() throws Exception {

    String requestMessage = "<SrvUpdStCashOrderRq><HeaderInfo/>"
        + "<SrvUpdCashOrderRqMessage>"
        + "<cashOrderId>12345"
        + "</cashOrderId>"
        + "<cashOrderStatus>Committed</cashOrderStatus>"
        + "</SrvUpdCashOrderRqMessage>"
        + "</SrvUpdStCashOrderRq>";
    assertTrue(service.processMessage(requestMessage));
  }

  @Test
  public void testProcessMessage_SrvGetWorkplaceInfoRq() throws Exception {
    String requestMessage = "<SrvGetWorkPlaceInfoRq><HeaderInfo/>"
        + "<SrvGetWorkPlaceInfoRqMessage/></SrvGetWorkPlaceInfoRq>";
    assertTrue(service.processMessage(requestMessage));
  }

  @Test
  public void testProcessMessage_SrvCheckOverLimitRq() throws Exception {
    String requestMessage = "<SrvCheckOverLimitRq><HeaderInfo/>"
        + "<SrvCheckOverLimitRqMessage>"
        + "<userLogin>1</userLogin>"
        + "</SrvCheckOverLimitRqMessage>"
        + "</SrvCheckOverLimitRq>";
    assertTrue(service.processMessage(requestMessage));
  }

  @Test
  public void testProcessMessage_Other() throws Exception {
    String requestMessage = "Not valid xml";
    assertFalse(service.processMessage(requestMessage));
  }
}
