package ru.sportmaster.exceptions.task2.bank;

/**
 * Класс реализует зачисление денежных средств на отдельном счёте. Наследуется от операции.
 */
public class Replenishment extends Operation {

    /**
     * Конструктор класса, который по сути ничем не отличатся от родительского.
     *
     * @param receiver    Счёт отправителя.
     * @param amount      Сумма перевода.
     * @param transaction Связанная транзакция.
     */
    public Replenishment(BankAccount receiver, double amount, Transaction transaction) {
        super(receiver, amount, transaction);
    }
}