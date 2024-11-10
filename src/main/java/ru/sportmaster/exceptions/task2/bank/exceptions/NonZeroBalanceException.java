package ru.sportmaster.exceptions.task2.bank.exceptions;

/**
 * Исключение, выбрасываемое при попытке закрыть счёт с ненулевым балансом.
 */
public class NonZeroBalanceException extends RuntimeException {
    private static final String MESSAGE = "Невозможно закрыть счёт при ненулевом балансе. Выведите средства и повторите попытку!";

    public NonZeroBalanceException() {
        super(MESSAGE);
    }
}
