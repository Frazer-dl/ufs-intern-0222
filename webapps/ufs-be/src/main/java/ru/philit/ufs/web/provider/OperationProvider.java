package ru.philit.ufs.web.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.philit.ufs.model.cache.MockCache;
import ru.philit.ufs.model.cache.OperationCache;
import ru.philit.ufs.model.entity.account.Representative;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.CashOrderStatus;
import ru.philit.ufs.model.entity.oper.CashOrderType;
import ru.philit.ufs.model.entity.oper.Operation;
import ru.philit.ufs.model.entity.oper.OperationPackage;
import ru.philit.ufs.model.entity.oper.OperationPackageRequest;
import ru.philit.ufs.model.entity.oper.OperationTask;
import ru.philit.ufs.model.entity.oper.OperationTaskCardDeposit;
import ru.philit.ufs.model.entity.oper.OperationTaskDeposit;
import ru.philit.ufs.model.entity.oper.OperationTaskStatus;
import ru.philit.ufs.model.entity.oper.OperationTasksRequest;
import ru.philit.ufs.model.entity.user.ClientInfo;
import ru.philit.ufs.model.entity.user.Subbranch;
import ru.philit.ufs.util.UuidUtils;
import ru.philit.ufs.web.exception.InvalidDataException;

/**
 * Бизнес-логика работы с операциями.
 */
@Service
public class OperationProvider {

  private final RepresentativeProvider representativeProvider;
  private final OperationCache cache;
  private final MockCache mockCache;
  private final AtomicInteger cashOrderNumber = new AtomicInteger(0);

  /**
   * Конструктор бина.
   */
  @Autowired
  public OperationProvider(RepresentativeProvider representativeProvider, OperationCache cache,
      MockCache mockCache) {
    this.representativeProvider = representativeProvider;
    this.cache = cache;
    this.mockCache = mockCache;
  }

  /**
   * Добавление новой активной задачи.
   *
   * @param workplaceId уникальный номер УРМ/кассы
   * @param cashOrder кассовый ордер
   * @param depositTask данные для взноса на корпоративную карту
   * @param clientInfo информация о клиенте
   * @return пакет с новой активной задачей
   */
  public OperationPackage addActiveDepositTask(String workplaceId, CashOrder cashOrder,
      OperationTaskCardDeposit depositTask, ClientInfo clientInfo) {
    return addDepositTask(workplaceId, cashOrder,
        depositTask, OperationTaskStatus.ACTIVE, clientInfo);
  }

  /**
   * Добавление новой перенаправленной задачи.
   *
   * @param workplaceId уникальный номер УРМ/кассы
   * @param cashOrder кассовый ордер
   * @param depositTask данные для взноса на корпоративную карту
   * @param clientInfo информация о клиенте
   * @return пакет с новой перенаправленной задачей
   */
  public OperationPackage addForwardedDepositTask(String workplaceId, CashOrder cashOrder,
      OperationTaskCardDeposit depositTask, ClientInfo clientInfo) {
    return addDepositTask(workplaceId, cashOrder,
        depositTask, OperationTaskStatus.FORWARDED, clientInfo);
  }

  private OperationPackage addDepositTask(String workplaceId, CashOrder cashOrder,
      OperationTaskCardDeposit depositTask, OperationTaskStatus taskStatus,
      ClientInfo clientInfo) {
    if (StringUtils.isEmpty(workplaceId)) {
      throw new InvalidDataException("Отсутствует запрашиваемый номер УРМ/кассы");
    }
    if (cashOrder == null) {
      throw new InvalidDataException("Отсутствует кассовый ордер");
    }
    if (depositTask == null) {
      throw new InvalidDataException("Отсутствуют данные для взноса на корпоративную карту");
    }
    cashOrder.setCashOrderId(UuidUtils.getRandomUuid());
    cashOrder.setCashOrderStatus(CashOrderStatus.CREATED);
    cashOrder.setCashOrderType(CashOrderType.KO_1);
    cache.createCashOrder(cashOrder, clientInfo);
    cache.addCashOrderToCashBook(cashOrder);

    OperationPackageRequest packageRequest = new OperationPackageRequest();

    packageRequest.setWorkPlaceUid(workplaceId);
    packageRequest.setInn(depositTask.getInn());
    packageRequest.setUserLogin(clientInfo.getUser().getLogin());

    /*OperationPackage opPackage = cache.getPackage(packageRequest, clientInfo);
    if ((opPackage == null) || (opPackage.getId() == null)) {
      opPackage = cache.createPackage(packageRequest, clientInfo);
    }*/
    OperationPackage opPackage = cache.createPackage(packageRequest, clientInfo);

    depositTask.setStatus(taskStatus);

    OperationPackage addTasksPackage = new OperationPackage();
    addTasksPackage.setId(opPackage.getId());
    addTasksPackage.getToCardDeposits().add(depositTask);

    return cache.addTasksInPackage(addTasksPackage, clientInfo);
  }

  /**
   * Получение списка операция в статусе FORWARDED.
   *
   * @param clientInfo информация о клиенте
   * @return список операций
   */
  public List<OperationTaskCardDeposit> getForwardedDepositTasks(ClientInfo clientInfo) {
    OperationTasksRequest tasksRequest = new OperationTasksRequest();
    tasksRequest.setTaskStatusGlobal(OperationTaskStatus.FORWARDED);

    List<OperationPackage> operationPackages = cache.getTasksInPackages(tasksRequest,
        clientInfo);

    List<OperationTaskCardDeposit> resultList = new ArrayList<>();
    for (OperationPackage opPackage : operationPackages) {
      for (OperationTask task : opPackage.getToCardDeposits()) {
        if (task instanceof OperationTaskCardDeposit) {
          OperationTaskCardDeposit taskCardDeposit = (OperationTaskCardDeposit) task;
          Representative representative = representativeProvider.getRepresentativeById(
              taskCardDeposit.getRepresentativeId(), clientInfo);
          if (representative != null) {
            StringBuilder fullName = new StringBuilder();
            if (!StringUtils.isEmpty(representative.getLastName())) {
              fullName.append(representative.getLastName()).append(" ");
            }
            if (!StringUtils.isEmpty(representative.getFirstName())) {
              fullName.append(representative.getFirstName()).append(" ");
            }
            if (!StringUtils.isEmpty(representative.getPatronymic())) {
              fullName.append(representative.getPatronymic()).append(" ");
            }
            taskCardDeposit.setRepFio(fullName.toString().trim());
          }
          taskCardDeposit.setPackageId(opPackage.getId());
          resultList.add(taskCardDeposit);
        }
      }
    }

    return resultList;
  }

  /**
   * Подтверждение операции.
   *
   * @param packageId идентификатор пакета задач
   * @param taskId идентификатор задачи
   * @param workplaceId номер УРМ/кассы
   * @param operationTypeCode код типа операции
   * @param clientInfo информация о клиенте
   * @return информация об операции
   */
  public Operation confirmOperation(Long packageId, Long taskId, String workplaceId,
      String operationTypeCode, ClientInfo clientInfo) {
    if (packageId == null) {
      throw new InvalidDataException("Отсутствует запрашиваемый идентификатор пакета задач");
    }
    if (taskId == null) {
      throw new InvalidDataException("Отсутствует запрашиваемый идентификатор задачи");
    }
    if (StringUtils.isEmpty(workplaceId)) {
      throw new InvalidDataException("Отсутствует запрашиваемый номер УРМ/кассы");
    }
    if (StringUtils.isEmpty(operationTypeCode)) {
      throw new InvalidDataException("Отсутствует запрашиваемый код типа операции");
    }

    OperationTasksRequest getTasksRequest = new OperationTasksRequest();
    getTasksRequest.setPackageId(packageId);

    OperationPackage opPackage = cache.getTasksInPackage(getTasksRequest, clientInfo);
    if (opPackage == null) {
      throw new InvalidDataException("Запрашиваемый пакет задач не найден");
    }

    List<OperationTask> depositTasks = opPackage.getToCardDeposits();
    for (OperationTask operationTask : depositTasks) {
      if (operationTask.getId().equals(taskId)) {
        operationTask.setStatus(OperationTaskStatus.COMPLETED);
        break;
      }
    }
    OperationPackage updateTasksPackage = new OperationPackage();
    updateTasksPackage.setId(packageId);
    updateTasksPackage.setToCardDeposits(depositTasks);

    cache.updateTasksInPackage(updateTasksPackage, clientInfo);

    OperationTask operationTaskDeposit;
    switch (operationTypeCode) {
      case "ToAccountDepositRub":
        operationTaskDeposit = updateTasksPackage.getToAccountDeposits().stream()
          .findFirst().orElse(null);
        break;
      case "ToCardDeposit":
        operationTaskDeposit = updateTasksPackage.getToCardDeposits().stream()
          .findFirst().orElse(null);
        break;
      default:
        operationTaskDeposit = null;
    }
    if (operationTaskDeposit != null) {
      Operation operation = mockCache.createOperation(workplaceId, operationTypeCode);
      CashOrder cashOrder = createCashOrder(operation,
          (OperationTaskDeposit) operationTaskDeposit);
      cache.createCashOrder(cashOrder, clientInfo);
      operation.setCashOrderId(cashOrder.getCashOrderId());
      operation = mockCache.commitOperation(operation);
      cashOrder = cache.updCashOrder(cashOrder, clientInfo);
      cache.addCashOrderToCashBook(cashOrder);
      cache.addOperation(taskId, operation);
      return operation;
    }
    return null;
  }

  /**
   * Отмена операции.
   *
   * @param packageId идентификатор пакета задач
   * @param taskId идентификатор задачи
   * @param workplaceId номер УРМ/кассы
   * @param operationTypeCode код типа операции
   * @return информация об операции
   */
  public Operation cancelOperation(Long packageId, Long taskId, String workplaceId,
      String operationTypeCode, ClientInfo clientInfo) {
    if (packageId == null) {
      throw new InvalidDataException("Отсутствует запрашиваемый идентификатор пакета задач");
    }
    if (taskId == null) {
      throw new InvalidDataException("Отсутствует запрашиваемый идентификатор задачи");
    }
    if (StringUtils.isEmpty(workplaceId)) {
      throw new InvalidDataException("Отсутствует запрашиваемый номер УРМ/кассы");
    }
    if (StringUtils.isEmpty(operationTypeCode)) {
      throw new InvalidDataException("Отсутствует запрашиваемый код типа операции");
    }

    OperationTasksRequest getTasksRequest = new OperationTasksRequest();
    getTasksRequest.setPackageId(packageId);
    OperationPackage opPackage = cache.getTasksInPackage(getTasksRequest, clientInfo);
    if (opPackage == null) {
      throw new InvalidDataException("Запрашиваемый пакет задач не найден");
    }

    List<OperationTask> depositTasks = opPackage.getToCardDeposits();
    for (OperationTask operationTask : depositTasks) {
      if (operationTask.getId().equals(taskId)) {
        operationTask.setStatus(OperationTaskStatus.DECLINED);
        break;
      }
    }
    OperationPackage updateTasksPackage = new OperationPackage();
    updateTasksPackage.setId(packageId);
    updateTasksPackage.setToCardDeposits(depositTasks);
    cache.updateTasksInPackage(updateTasksPackage, clientInfo);

    Operation operation = mockCache.createOperation(workplaceId, operationTypeCode);
    operation = mockCache.cancelOperation(operation);

    cache.addOperation(taskId, operation);

    return operation;
  }

  private CashOrder createCashOrder(Operation operation, OperationTaskDeposit taskDeposit) {
    CashOrder cashOrder = new CashOrder();
    cashOrder.setResponseCode(String.valueOf(taskDeposit.getResponseCode()));
    cashOrder.setCashOrderId(UuidUtils.getRandomUuid());
    cashOrder.setCashOrderINum(String.valueOf(cashOrderNumber.incrementAndGet()));
    cashOrder.setCreatedDttm(operation.getCreatedDate());
    cashOrder.setCashOrderType(CashOrderType.KO_1);
    cashOrder.setOperationType(operation.getTypeCode());
    cashOrder.setCashOrderStatus(CashOrderStatus.CREATED);
    cashOrder.setRepresentative(new Representative());
    cashOrder.getRepresentative().setId(taskDeposit.getRepresentativeId());
    cashOrder.getRepresentative().setLastName(taskDeposit.getRepFio().split(" ")[0]);
    cashOrder.getRepresentative().setFirstName(taskDeposit.getRepFio().split(" ")[1]);
    cashOrder.getRepresentative().setPatronymic(taskDeposit.getRepFio().split(" ")[3]);
    cashOrder.getRepresentative().setInn(taskDeposit.getInn());
    cashOrder.setLegalEntityShortName(taskDeposit.getLegalEntityShortName());
    cashOrder.setAmount(taskDeposit.getAmount());
    cashOrder.setAccountId(taskDeposit.getAccountId());
    cashOrder.setRecipientBank(new Subbranch());
    cashOrder.getRecipientBank().setBankName(taskDeposit.getRecipientBank());
    cashOrder.getRecipientBank().setBic(taskDeposit.getRecipientBankBic());
    cashOrder.getRecipientBank().setCorrespondentAccount(taskDeposit.getRecipientAccountId());
    cashOrder.setSenderBank(new Subbranch());
    cashOrder.getSenderBank().setBankName(taskDeposit.getSenderBank());
    cashOrder.getSenderBank().setBic(taskDeposit.getSenderBankBic());
    cashOrder.getSenderBank().setCorrespondentAccount(taskDeposit.getSenderAccountId());
    cashOrder.setClientTypeFk(taskDeposit.isClientTypeFk());
    cashOrder.setOperationId(operation.getId());
    cashOrder.setCurrencyType(taskDeposit.getCurrencyType());
    cashOrder.setCashSymbols(taskDeposit.getCashSymbols());
    cashOrder.setWorkPlaceUId(operation.getWorkplaceId());
    return cashOrder;
  }
}
