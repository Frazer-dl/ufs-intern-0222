package ru.philit.ufs.web.mapping.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.philit.ufs.model.entity.account.Card;
import ru.philit.ufs.model.entity.account.CardNetworkCode;
import ru.philit.ufs.model.entity.account.CardType;
import ru.philit.ufs.model.entity.account.IdentityDocument;
import ru.philit.ufs.model.entity.account.IdentityDocumentType;
import ru.philit.ufs.model.entity.account.Representative;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashSymbol;
import ru.philit.ufs.model.entity.oper.Operation;
import ru.philit.ufs.model.entity.oper.OperationPackage;
import ru.philit.ufs.model.entity.oper.OperationTask;
import ru.philit.ufs.model.entity.oper.OperationTaskCardDeposit;
import ru.philit.ufs.model.entity.oper.OvnStatus;
import ru.philit.ufs.model.entity.user.Subbranch;
import ru.philit.ufs.web.dto.CardDepositDto;
import ru.philit.ufs.web.dto.CashOrderDto;
import ru.philit.ufs.web.dto.CashSymbolDto;
import ru.philit.ufs.web.dto.CreditCardDto;
import ru.philit.ufs.web.dto.IdentityDocumentDto;
import ru.philit.ufs.web.dto.OperationDto;
import ru.philit.ufs.web.dto.OperationPackageDto;
import ru.philit.ufs.web.dto.OperationTaskDto;
import ru.philit.ufs.web.dto.RepresentativeDto;
import ru.philit.ufs.web.dto.SubbranchDto;
import ru.philit.ufs.web.mapping.OperationMapper;

@Component
public class OperationMapperImpl extends CommonMapperImpl implements OperationMapper {

  @Override
  public OperationPackageDto asDto(OperationPackage in) {
    if (in == null) {
      return null;
    }
    OperationPackageDto out = new OperationPackageDto();

    out.setId(asDto(in.getId()));
    if (in.getStatus() != null) {
      out.setStatus(in.getStatus().value());
    }
    out.setToCardDeposits(asTaskDto(in.getToCardDeposits()));
    out.setFromCardWithdraw(asTaskDto(in.getFromCardWithdraws()));

    return out;
  }

  @Override
  public OperationTaskDto asDto(OperationTask in) {
    if (in == null) {
      return null;
    }
    OperationTaskDto out = new OperationTaskDto();

    out.setId(asDto(in.getId()));
    if (in.getStatus() != null) {
      out.setStatus(in.getStatus().value());
    }
    out.setChangedDate(asLongDateDto(in.getChangedDate()));
    out.setCreatedDate(asLongDateDto(in.getCreatedDate()));
    out.setStatusChangedDate(asLongDateDto(in.getStatusChangedDate()));

    return out;
  }

  @Override
  public OperationDto asDto(Operation in) {
    if (in == null) {
      return null;
    }
    OperationDto out = new OperationDto();

    out.setId(in.getId());
    if (in.getStatus() != null) {
      out.setStatus(in.getStatus().value());
    }
    out.setCreatedDate(asLongDateDto(in.getCreatedDate()));
    out.setCommittedDate(asLongDateDto(in.getCommittedDate()));

    return out;
  }

  @Override
  public CardDepositDto asDto(OperationTaskCardDeposit in) {
    if (in == null) {
      return null;
    }
    CardDepositDto out = new CardDepositDto();

    out.setTaskId(asDto(in.getId()));
    out.setTaskStatus(in.getStatus() != null ? in.getStatus().code() : null);
    out.setPackageId(asDto(in.getPackageId()));
    out.setId(in.getOvnUid());
    out.setNum(asDto(in.getOvnNum()));
    out.setStatus(in.getOvnStatus() != null ? in.getOvnStatus().code() : null);
    out.setRepresentativeId(in.getRepresentativeId());
    out.setRepFio(in.getRepFio());
    out.setLegalEntityShortName(in.getLegalEntityShortName());
    out.setAmount(asDto(in.getAmount()));
    out.setCreatedDate(asLongDateDto(in.getCreatedDate()));
    out.setInn(in.getInn());
    out.setKpp(in.getKpp());
    out.setAccountId(in.getAccountId());
    out.setSenderAccountId(in.getSenderAccountId());
    out.setSenderBank(in.getSenderBank());
    out.setSenderBankBic(in.getSenderBankBic());
    out.setRecipientAccountId(in.getRecipientAccountId());
    out.setRecipientBank(in.getRecipientBank());
    out.setRecipientBankBic(in.getRecipientBankBic());
    out.setSource(in.getSource());
    out.setClientTypeFk(in.isClientTypeFk());
    out.setOrganisationNameFk(in.getOrganisationNameFk());
    out.setPersonalAccountId(in.getPersonalAccountId());
    out.setCurrencyType(in.getCurrencyType());
    out.setSymbols(asSymbolDto(in.getCashSymbols()));
    out.setCard(asDto(in.getCard()));

    return out;
  }

  private CashSymbolDto asDto(CashSymbol in) {
    if (in == null) {
      return null;
    }
    CashSymbolDto out = new CashSymbolDto();

    out.setCode(in.getCode());
    out.setDesc(in.getDescription());
    out.setAmount(asDto(in.getAmount()));

    return out;
  }

  @Override
  public List<CardDepositDto> asDto(Collection<OperationTaskCardDeposit> in) {
    if (in == null) {
      return Collections.emptyList();
    }
    List<CardDepositDto> out = new ArrayList<>();

    for (OperationTaskCardDeposit task : in) {
      CardDepositDto taskDto = asDto(task);
      if (taskDto != null) {
        out.add(taskDto);
      }
    }
    return out;
  }

  @Override
  public List<CashOrderDto> asDto(List<CashOrder> in) {
    if (in == null) {
      return Collections.emptyList();
    }
    List<CashOrderDto> out = new ArrayList<>();

    for (CashOrder cashOrder: in) {
      CashOrderDto co = new CashOrderDto();
      co.setId(cashOrder.getCashOrderId());
      co.setCashSymbols(asSymbolDto(cashOrder.getCashSymbols()));
      co.setAccount20202Num(cashOrder.getAccount20202Num());
      co.setSubbranchDto(asDto(cashOrder.getRecipientBank()));
      co.setAmount(asDto(cashOrder.getAmount()));
      co.setCashOrderINum(cashOrder.getCashOrderINum());
      co.setCashOrderStatus(cashOrder.getCashOrderStatus().value());
      co.setCurrencyType(cashOrder.getCurrencyType());
      co.setRepresentativeDto(asDto(cashOrder.getRepresentative()));
      out.add(co);
    }
    return out;
  }

  @Override
  public CashOrderDto asDto(CashOrder in) {
    if (in == null) {
      return null;
    }
    CashOrderDto out = new CashOrderDto();

    out.setId(in.getCashOrderId());
    out.setCashSymbols(asSymbolDto(in.getCashSymbols()));
    out.setAccount20202Num(in.getAccount20202Num());
    out.setSubbranchDto(asDto(in.getRecipientBank()));
    out.setAmount(asDto(in.getAmount()));
    out.setCashOrderINum(in.getCashOrderINum());
    out.setCashOrderStatus(in.getCashOrderStatus().value());
    out.setCurrencyType(in.getCurrencyType());
    out.setRepresentativeDto(asDto(in.getRepresentative()));

    return out;
  }

  private RepresentativeDto asDto(Representative in) {
    if (in == null) {
      return null;
    }
    RepresentativeDto out = new RepresentativeDto();

    out.setAddress(in.getAddress());
    out.setBirthDate(asShortDateDto(in.getBirthDate()));
    out.setBirthPlace(in.getPlaceOfBirth());
    out.setDocument(asDto(in.getIdentityDocuments().get(0)));
    out.setEmail(in.getEmail());
    out.setFirstName(in.getFirstName());
    out.setLastName(in.getLastName());
    out.setPatronymic(in.getPatronymic());
    out.setFullName(in.getFirstName() + " " + in.getLastName() + " " + in.getPatronymic());
    out.setId(in.getId());
    out.setInn(in.getInn());
    out.setPhoneMobile(in.getPhoneNumMobile());
    out.setPhoneWork(in.getPhoneNumWork());
    out.setPostcode(in.getPostindex());
    out.setResident(in.isResident());

    return out;
  }

  private CreditCardDto asDto(Card in) {
    if (in == null) {
      return null;
    }
    CreditCardDto out = new CreditCardDto();

    out.setNumber(in.getNumber());
    out.setExpiryDate(asShortDateDto(in.getExpiryDate()));
    out.setIssuingNetworkCode(in.getIssuingNetworkCode().name());
    out.setType(in.getType().name());
    out.setOwnerFirstName(in.getOwnerFirstName());
    out.setOwnerLastName(in.getOwnerLastName());

    return out;
  }

  @Override
  public OperationTaskCardDeposit asEntity(CardDepositDto in) {
    if (in == null) {
      return null;
    }
    OperationTaskCardDeposit out = new OperationTaskCardDeposit();

    out.setOvnUid(in.getId());
    out.setOvnNum(asLongEntity(in.getNum()));
    if (in.getStatus() != null) {
      out.setOvnStatus(OvnStatus.getByValue(in.getStatus()));
    }
    out.setRepresentativeId(in.getRepresentativeId());
    out.setRepFio(in.getRepFio());
    out.setLegalEntityShortName(in.getLegalEntityShortName());
    out.setAmount(asDecimalEntity(in.getAmount()));
    try {
      out.setCreatedDate(asLongDateEntity(in.getCreatedDate()));
    } catch (ParseException e) {
      out.setCreatedDate(null);
    }
    out.setInn(in.getInn());
    out.setKpp(in.getKpp());
    out.setAccountId(in.getAccountId());
    out.setSenderAccountId(in.getSenderAccountId());
    out.setSenderBank(in.getSenderBank());
    out.setSenderBankBic(in.getSenderBankBic());
    out.setRecipientAccountId(in.getRecipientAccountId());
    out.setRecipientBank(in.getRecipientBank());
    out.setRecipientBankBic(in.getRecipientBankBic());
    out.setSource(in.getSource());
    out.setClientTypeFk(in.isClientTypeFk());
    out.setOrganisationNameFk(in.getOrganisationNameFk());
    out.setPersonalAccountId(in.getPersonalAccountId());
    out.setCurrencyType(in.getCurrencyType());
    out.setCard(asEntity(in.getCard()));
    out.setCashSymbols(asSymbolEntity(in.getSymbols()));

    return out;
  }

  @Override
  public CashOrder asEntity(CashOrderDto in) {
    if (in == null) {
      return null;
    }
    CashOrder out = new CashOrder();

    out.setCashOrderId(in.getId());
    out.setAccount20202Num(in.getAccount20202Num());
    out.setAmount(super.asDecimalEntity(in.getAmount()));
    out.setCashOrderINum(in.getCashOrderINum());
    out.setCashSymbols(asSymbolEntity(in.getCashSymbols()));
    out.setCurrencyType(in.getCurrencyType());
    out.setCashOrderStatus(parseCashOrderStatus(in.getCashOrderStatus()));
    out.setRepresentative(asEntity(in.getRepresentativeDto()));
    out.setRecipientBank(asEntity(in.getSubbranchDto()));

    return out;
  }

  @Override
  public Long asEntity(String in) {
    return asLongEntity(in);
  }

  private Card asEntity(CreditCardDto in) {
    if (in == null) {
      return null;
    }
    Card out = new Card();

    out.setNumber(in.getNumber());
    try {
      out.setExpiryDate(asShortDateEntity(in.getExpiryDate()));
    } catch (ParseException e) {
      out.setExpiryDate(null);
    }
    if (in.getIssuingNetworkCode() != null) {
      out.setIssuingNetworkCode(CardNetworkCode.getByValue(in.getIssuingNetworkCode()));
    }
    if (in.getType() != null) {
      out.setType(CardType.getByValue(in.getType()));
    }
    out.setOwnerFirstName(in.getOwnerFirstName());
    out.setOwnerLastName(in.getOwnerLastName());

    return out;
  }

  private CashSymbol asEntity(CashSymbolDto in) {
    CashSymbol out = new CashSymbol();

    out.setCode(in.getCode());
    out.setAmount(asDecimalEntity(in.getAmount()));

    return out;
  }

  private Subbranch asEntity(SubbranchDto in) {
    if (in == null) {
      return null;
    }
    Subbranch out = new Subbranch();

    out.setTbCode(in.getTbCode());
    out.setGosbCode(in.getGosbCode());
    out.setOsbCode(in.getOsbCode());
    out.setVspCode(in.getVspCode());
    out.setSubbranchCode(in.getCode());
    out.setInn(in.getInn());
    out.setBic(in.getBic());
    out.setBankName(in.getBankName());

    return out;
  }

  private Representative asEntity(RepresentativeDto in) {
    if (in == null) {
      return null;
    }
    Representative out = new Representative();

    out.setLastName(in.getLastName());
    out.setFirstName(in.getFirstName());
    out.setPatronymic(in.getPatronymic());
    try {
      out.setBirthDate(asShortDateEntity(in.getBirthDate()));
    } catch (ParseException e) {
      out.setBirthDate(null);
    }
    out.setPlaceOfBirth(in.getBirthPlace());
    out.setInn(in.getInn());
    out.setAddress(in.getAddress());
    out.setPostindex(in.getPostcode());
    out.setResident(in.isResident());
    out.setIdentityDocuments(asEntity(in.getDocument()));

    return out;
  }

  private List<IdentityDocument> asEntity(IdentityDocumentDto in) {
    if (in == null) {
      return null;
    }
    IdentityDocument out = new IdentityDocument();

    if (in.getType() != null) {
      out.setType(IdentityDocumentType.valueOf(in.getType()));
    }
    out.setSeries(in.getSeries());
    out.setNumber(in.getNumber());
    out.setIssuedBy(in.getIssuedBy());
    try {
      out.setIssuedDate(asShortDateEntity(in.getIssuedDate()));
    } catch (ParseException e) {
      out.setIssuedDate(null);
    }
    List<IdentityDocument> list = new ArrayList<>();
    list.add(out);
    return list;
  }

  private List<OperationTaskDto> asTaskDto(List<OperationTask> in) {
    if (in == null) {
      return Collections.emptyList();
    }
    List<OperationTaskDto> out = new ArrayList<>();

    for (OperationTask operationTask : in) {
      out.add(asDto(operationTask));
    }
    return out;
  }

  private List<CashSymbolDto> asSymbolDto(Collection<CashSymbol> in) {
    if (in == null) {
      return Collections.emptyList();
    }
    List<CashSymbolDto> out = new ArrayList<>();

    for (CashSymbol cashSymbol : in) {
      out.add(asDto(cashSymbol));
    }

    return out;
  }

  private List<CashSymbol> asSymbolEntity(List<CashSymbolDto> in) {
    if (in == null) {
      return Collections.emptyList();
    }
    List<CashSymbol> out = new ArrayList<>();

    for (CashSymbolDto cashSymbol : in) {
      out.add(asEntity(cashSymbol));
    }
    return out;
  }

  private CashOrderStatus parseCashOrderStatus(String cashOrderStatus) {
    if (cashOrderStatus != null) {
      return CashOrderStatus.valueOf(cashOrderStatus);
    }
    return null;
  }
}