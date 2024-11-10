package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке создать счёт с отрицательным балансом.
 */
public class NegativeBalanceException extends RuntimeException {
    private static final String MESSAGE = "Создание счёта с отрицательным балансом запрещено!";

    public NegativeBalanceException() {
        super(MESSAGE);
    }
}
