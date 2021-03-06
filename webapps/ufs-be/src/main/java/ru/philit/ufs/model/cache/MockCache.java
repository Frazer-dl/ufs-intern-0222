package ru.philit.ufs.model.cache;

import ru.philit.ufs.model.entity.account.Card;
import ru.philit.ufs.model.entity.oper.CashOrder;
import ru.philit.ufs.model.entity.oper.Operation;
import ru.philit.ufs.model.entity.oper.OperationTaskDeposit;
import ru.philit.ufs.model.entity.user.User;

/**
 * Интерфейс доступа к временному кешу данных.
 */
public interface MockCache {

  User getUser(String userLogin, String password);

  Card getCreditCard();

  Operation createOperation(String workplaceId, String operationTypeCode);

  Operation commitOperation(Operation operation);

  Operation cancelOperation(Operation operation);

}
