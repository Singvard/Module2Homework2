package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке отметить как мошеннический счёт, который уже закрыт.
 */
public class TryToMarkAsFraudClosedAccountException extends RuntimeException {
    private static final String MESSAGE = "Операция не выполнена: лицевой счёт закрыт!";

    public TryToMarkAsFraudClosedAccountException() {
        super(MESSAGE);
    }
}
