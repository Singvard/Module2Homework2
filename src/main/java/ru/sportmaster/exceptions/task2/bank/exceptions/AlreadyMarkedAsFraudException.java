package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке отметить как мошеннический счёт, который уже имеет этот статус.
 */
public class AlreadyMarkedAsFraudException extends RuntimeException {
    private static final String MESSAGE = "Операция не выполнена: счёт уже подозревается в мошенничестве!";

    public AlreadyMarkedAsFraudException() {
        super(MESSAGE);
    }
}
