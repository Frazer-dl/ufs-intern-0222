package ru.philit.ufs.web.mapping;

import java.math.BigDecimal;
import java.util.List;
import ru.philit.ufs.model.entity.oper.CashDepositAnnouncement;
import ru.philit.ufs.model.entity.user.Workplace;
import ru.philit.ufs.web.dto.AnnouncementDto;
import ru.philit.ufs.web.dto.WorkplaceDto;

/**
 * Конвертер для объявлений на взнос наличных.
 */
public interface AnnouncementMapper {

  AnnouncementDto asDto(CashDepositAnnouncement in);

  String asDto(BigDecimal in);

  WorkplaceDto asDto(Workplace in);

  List<AnnouncementDto> asAnnouncementDto(List<CashDepositAnnouncement> in);

}
