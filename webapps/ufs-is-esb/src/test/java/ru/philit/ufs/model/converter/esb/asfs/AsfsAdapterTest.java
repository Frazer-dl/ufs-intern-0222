package ru.philit.ufs.model.converter.esb.asfs;

import org.junit.Assert;
import org.junit.Test;
import ru.philit.ufs.model.converter.esb.multi.MultiAdapter;
import ru.philit.ufs.model.entity.common.ExternalEntity;
import ru.philit.ufs.model.entity.esb.asfs.HeaderInfoType;

public class AsfsAdapterTest extends AsfsAdapterBaseTest {

  @Test
  public void testHeaderInfo() {
    HeaderInfoType headerInfo = AsfsAdapter.headerInfo();
    assertHeaderInfo(headerInfo);
  }

  @Test
  public void testMultiAdapter() {
    ExternalEntity externalEntity = MultiAdapter.convert(headerInfo());
    Assert.assertNull(externalEntity);
  }
}
