package ru.sportmaster.exceptions.task2.bank;

import java.util.concurrent.atomic.DoubleAdder;

/**
 * Класс реализует операцию - действие с денежными средствами на отдельном счёте.
 */
public class Operation {

    /**
     * Транзакция, связанная с операцией.
     */
    protected Transaction transaction;

    /**
     * Счёт, связанный с операцией.
     */
    protected BankAccount account;

    /**
     * Сумма перевода
     */
    protected double amount;

    /**
     * Статус операции. Возможные значения: null - состояние неизвестно (т.е. операция не завершена),
     * true - операция успешно выполнена, false - операцию выполнить не удалось.
     */
    protected Boolean isSuccessful;

    /**
     * Получение статуса операции.
     *
     * @return Статус операции.
     */
    public Boolean getSuccessful() {
        return isSuccessful;
    }

    /**
     * Создание операции. Статус по умолчанию не определён.
     *
     * @param account     Счёт, на которым производится операция.
     * @param amount      Сумма перевода.
     * @param transaction Связанная транзакция.
     */
    public Operation(BankAccount account, double amount, Transaction transaction) {
        this.account = account;
        this.amount = amount;
        this.isSuccessful = null;
        this.transaction = transaction;
    }

    /**
     * Обработка операции.
     * 1. Сперва проверяем счёт на подозрение в мошенничестве. Если проверка не пройдена,
     * то отмечаем операцию как неудачную, запускаем проверку транзакции и выходим.
     * 2. Затем проверяем, чтобы счёт не был закрыт. Если проверка не пройдена,
     * то отмечаем операцию как неудачную, запускаем проверку транзакции и выходим.
     * 3. Если всё хорошо, то прибавляем к балансу счёта сумму перевода (для списания
     * она будет отрицательной, для зачисления - положительной). Отмечаем операцию
     * как успешную и запускаем проверку транзакции.
     */
    public void operate() {
        if (account.isFraud()) {
            System.out.println("Операция не выполнена: счёт заморожен за подозрение в мошенничестве.");
            isSuccessful = false;
            transaction.checkStatus();
            return;
        }
        if (account.isClosed()) {
            System.out.println("Операция не выполнена: лицевой счёт закрыт.");
            isSuccessful = false;
            transaction.checkStatus();
            return;
        }
        DoubleAdder adder = new DoubleAdder();
        adder.add(account.getBalance());
        adder.add(amount);
        account.setBalance(adder.sum());
        isSuccessful = true;
        transaction.checkStatus();
    }
}
