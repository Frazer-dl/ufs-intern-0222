package ru.philit.ufs.model.converter.esb.asfs;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.philit.ufs.model.converter.esb.asfs.mapstruct.WorkPlaceAdapterMapStruct;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRq;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs;
import ru.philit.ufs.model.entity.esb.asfs.SrvGetWorkPlaceInfoRs.SrvGetWorkPlaceInfoRsMessage;
import ru.philit.ufs.model.entity.user.Workplace;
import ru.philit.ufs.model.entity.user.WorkplaceType;

public class WorkPlaceAdapterTest extends AsfsAdapterTest {

  private static final String FIX_UUID = "a55ed415-3976-43f7-912c-4c26ca79e969";

  private Workplace workplace;
  private SrvGetWorkPlaceInfoRs response;

  /**
   * Set up test data.
   */
  @Before
  public void setUp() {
    workplace = new Workplace();
    workplace.setId("1");
    workplace.setType(WorkplaceType.CASHBOX);
    workplace.setCashboxOnBoard(false);
    workplace.setCashboxDeviceType("DC");
    workplace.setSubbranchCode("770351");
    workplace.setCurrencyType("USD");
    workplace.setAmount(BigDecimal.valueOf(100));
    workplace.setLimit(BigDecimal.TEN);
    workplace.setReceiveDate(date(2022, 2, 25, 12, 43));
    workplace.setRequestUid("10000");
    workplace.setCashboxDeviceId("12");

    response = new SrvGetWorkPlaceInfoRs();
    response.setHeaderInfo(headerInfo(FIX_UUID));
    response.setSrvGetWorkPlaceInfoRsMessage(new SrvGetWorkPlaceInfoRsMessage());
    response.getSrvGetWorkPlaceInfoRsMessage().setWorkPlaceType(BigInteger.valueOf(0));
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxOnBoard(false);
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxOnBoardDevice("DC");
    response.getSrvGetWorkPlaceInfoRsMessage().setSubbranchCode("770351");
    response.getSrvGetWorkPlaceInfoRsMessage().setCurrentCurrencyType("USD");
    response.getSrvGetWorkPlaceInfoRsMessage().setAmount(BigDecimal.valueOf(100));
    response.getSrvGetWorkPlaceInfoRsMessage().setWorkPlaceLimit(BigDecimal.TEN);
    response.getSrvGetWorkPlaceInfoRsMessage().setCashboxDeviceType("DC");
  }

  @Test
  public void testRequestGetWorkPlaceInfo() {
    SrvGetWorkPlaceInfoRq request = WorkPlaceAdapter.requestGetWorkPlace("1");
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvGetWorkPlaceInfoRqMessage());
    Assert.assertEquals(request.getSrvGetWorkPlaceInfoRqMessage().getWorkPlaceUId(), "1");
  }

  @Test
  public void testRequestGetWorkPlaceInfoMapStruct() {
    SrvGetWorkPlaceInfoRq request = WorkPlaceAdapterMapStruct.requestGetWorkPlaceMapStruct("1");
    assertHeaderInfo(headerInfo());
    Assert.assertNotNull(request.getSrvGetWorkPlaceInfoRqMessage());
    Assert.assertEquals(request.getSrvGetWorkPlaceInfoRqMessage().getWorkPlaceUId(), "1");
  }

  @Test
  public void convertGetWorkPlaceInfoRs() {
    Workplace workplace = WorkPlaceAdapter.convert(response);
    assertHeaderInfo(workplace, FIX_UUID);
    Assert.assertEquals(workplace.getCashboxDeviceType(),
        response.getSrvGetWorkPlaceInfoRsMessage().getCashboxDeviceType());
    Assert.assertEquals(workplace.getType().code(),
        response.getSrvGetWorkPlaceInfoRsMessage().getWorkPlaceType().intValue());
    Assert.assertEquals(workplace.getAmount(),
        response.getSrvGetWorkPlaceInfoRsMessage().getAmount());
    Assert.assertEquals(workplace.getLimit(),
        response.getSrvGetWorkPlaceInfoRsMessage().getWorkPlaceLimit());
    Assert.assertEquals(workplace.getSubbranchCode(),
        response.getSrvGetWorkPlaceInfoRsMessage().getSubbranchCode());
  }

  @Test
  public void convertGetWorkPlaceInfoRsMapStruct() {
    Workplace workplace = WorkPlaceAdapterMapStruct.convertMapStruct(response);
    assertHeaderInfo(workplace, FIX_UUID);
    Assert.assertEquals(workplace.getCashboxDeviceType(),
        response.getSrvGetWorkPlaceInfoRsMessage().getCashboxDeviceType());
    Assert.assertEquals(workplace.getType().code(),
        response.getSrvGetWorkPlaceInfoRsMessage().getWorkPlaceType().intValue());
    Assert.assertEquals(workplace.getAmount(),
        response.getSrvGetWorkPlaceInfoRsMessage().getAmount());
    Assert.assertEquals(workplace.getLimit(),
        response.getSrvGetWorkPlaceInfoRsMessage().getWorkPlaceLimit());
    Assert.assertEquals(workplace.getSubbranchCode(),
        response.getSrvGetWorkPlaceInfoRsMessage().getSubbranchCode());
  }
}
