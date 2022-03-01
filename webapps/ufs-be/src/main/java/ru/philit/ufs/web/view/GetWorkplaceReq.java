package ru.philit.ufs.web.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.philit.ufs.web.dto.BaseRequest;

@ToString
@Getter
@Setter
@SuppressWarnings("serial")
public class GetWorkplaceReq extends BaseRequest {

  /**
   * Идентификатор рабочего места.
   */
  private String workplaceId;

}
