package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке снять статус мошеннического со счёта, который и так не мошеннический.
 */
public class AlreadyNonFraudulentException extends RuntimeException {
    private static final String MESSAGE = "Операция не выполнена: счёт уже не считается мошенническим!";

    public AlreadyNonFraudulentException() {
        super(MESSAGE);
    }
}
