package ru.sportmaster.exceptions.task2.bank;

/**
 * Класс реализует банковский сервис.
 */
public class BankService {

    /**
     * Перевод денежных средств между счетами. Сперва создаётся новая транзакция с операциями списания и зачисления.
     * Далее операция списания кладётся в операционную очередь отправителя и запускается её обработка.
     * Затем операция начисления добавляется в операционную очередь получателя и запускается её обработка.
     *
     * @param sender   Счёт отправителя.
     * @param receiver Счёт получателя.
     * @param amount   Сумма перевода.
     */
    public void transferFunds(BankAccount sender, BankAccount receiver, double amount) {

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            System.out.println("Перевод средств невозможен: счёт получателя совпадает со счётом отправителя!");
            return;
        }

        if (amount <= 0) {
            System.out.println("Перевод средств невозможен: сумма перевода должна быть больше 0!");
            return;
        }

        Transaction transaction = new Transaction(sender, receiver, amount);
        sender.getTaskQueue().add(transaction.getWithdrawal());
        sender.getTaskQueue().process();
        receiver.getTaskQueue().add(transaction.getReplenishment());
        receiver.getTaskQueue().process();
    }
}
