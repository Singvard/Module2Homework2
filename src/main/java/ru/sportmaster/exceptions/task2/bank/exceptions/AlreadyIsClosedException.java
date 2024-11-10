package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке закрыть уже закрытый счёт.
 */
public class AlreadyIsClosedException extends RuntimeException {
    private static final String MESSAGE = "Операция не выполнена: счёт уже закрыт!";

    public AlreadyIsClosedException() {
        super(MESSAGE);
    }
}
