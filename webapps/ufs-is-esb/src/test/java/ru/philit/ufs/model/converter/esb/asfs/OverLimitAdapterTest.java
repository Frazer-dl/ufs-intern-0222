package ru.philit.ufs.model.converter.esb.asfs;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.philit.ufs.model.entity.common.ExternalEntityContainer;
import ru.philit.ufs.model.entity.esb.asfs.LimitStatusType;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvCheckOverLimitRs.SrvCheckOverLimitRsMessage;
import ru.philit.ufs.model.entity.oper.Limit;

public class OverLimitAdapterTest extends AsfsAdapterTest {

  private static final String FIX_UUID = "a55ed415-3976-41f7-916c-4c17ca79e969";

  private Limit limit;
  private SrvCheckOverLimitRs response;

  /**
   * Set up test data.
   */
  @Before
  public void setUp() {
    limit = new Limit();
    limit.setUserLogin("Lev");
    limit.setAmount(BigDecimal.ONE);
    limit.setTobeIncreased(true);
    limit.setLimitStatusType(true);

    response = new SrvCheckOverLimitRs();
    response.setHeaderInfo(headerInfo(FIX_UUID));
    response.setSrvCheckOverLimitRsMessage(new SrvCheckOverLimitRsMessage());
    response.getSrvCheckOverLimitRsMessage().setStatus(LimitStatusType.LIMIT_PASSED);
    response.getSrvCheckOverLimitRsMessage().setResponseCode("200");
  }

  @Test
  public void testRequestOverLimit() {
    SrvCheckOverLimitRq request = OverLimitAdapter.requestOverLimit(limit);
    assertHeaderInfo(request.getHeaderInfo());
    Assert.assertNotNull(request.getSrvCheckOverLimitRqMessage());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().getUserLogin(),
        limit.getUserLogin());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().getAmount(), limit.getAmount());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().isTobeIncreased(),
        limit.isTobeIncreased());
  }

  @Test
  public void testRequestOverLimitMapStruct() {
    SrvCheckOverLimitRq request = OverLimitAdapter.requestOverLimitMapStruct(limit);
    assertHeaderInfo(request.getHeaderInfo());
    Assert.assertNotNull(request.getSrvCheckOverLimitRqMessage());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().getUserLogin(),
        limit.getUserLogin());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().getAmount(), limit.getAmount());
    Assert.assertEquals(request.getSrvCheckOverLimitRqMessage().isTobeIncreased(),
        limit.isTobeIncreased());
  }

  @Test
  public void testConverterOverLimit() {
    ExternalEntityContainer<Boolean> container = OverLimitAdapter.convert(response);
    assertHeaderInfo(container, FIX_UUID);
    Assert.assertEquals(container.getData(), true);
  }

  @Test
  public void testConverterOverLimitMapStruct() {
    ExternalEntityContainer<Boolean> container = OverLimitAdapter.convertMapStruct(response);
    assertHeaderInfo(container, FIX_UUID);
    Assert.assertEquals(container.getData(), true);
  }

}
