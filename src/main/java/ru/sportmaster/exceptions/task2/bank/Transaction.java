package ru.sportmaster.exceptions.task2.bank;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * Класс реализует транзакцию - перевод денежных средств с одного счёта на другой.
 */
public class Transaction {
    /**
     * Формат вывода суммы перевода с точностью до сотых (т.е. до копеек)
     */
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.##");

    /**
     * Уникальный идентификатор транзакции.
     */
    private final String id;

    /**
     * Счёт отправителя средств
     */
    private final BankAccount sender;

    /**
     * Счёт получателя средств
     */
    private final BankAccount receiver;

    /**
     * Сумма перевода
     */
    private final double amount;

    /**
     * Операция списания средств со счёта отправителя.
     */
    private final Withdrawal withdrawal;

    /**
     * Операция зачисления средств на счёт получателя.
     */
    private final Replenishment replenishment;

    /**
     * Статус транзакции. Возможные значения: null - состояние неизвестно (т.е. транзакция не завершена),
     * true - успешно выполнена, false - транзакцию выполнить не удалось.
     */
    private Boolean isSuccessful;

    /**
     * Получение операции списания средств, связанной с транзакцией.
     *
     * @return Операция списания.
     */
    public Withdrawal getWithdrawal() {
        return withdrawal;
    }

    /**
     * Получение операции зачисления средств, связанной с транзакцией.
     *
     * @return Операция зачисления.
     */
    public Replenishment getReplenishment() {
        return replenishment;
    }

    /**
     * Создание транзакции с указанием отправителя, получателя и суммы перевода.
     * Автоматически генерируется её идентификатор. Создаются связанные операции
     * списания, зачисления и проставляется статус незавершённой транзакции.
     *
     * @param sender   Отправитель средств.
     * @param receiver Получатель средств.
     * @param amount   Сумма перевода.
     */
    public Transaction(BankAccount sender, BankAccount receiver, double amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.withdrawal = new Withdrawal(sender, amount, this);
        this.replenishment = new Replenishment(receiver, amount, this);
        this.isSuccessful = null;
    }

    /**
     * Проверка статуса транзакции. Полностью зависит от статуса связанных операций списания и зачисления.
     * 1. Если списание не удалось, а зачисление было успешным, то транзакция помечается как невыполненная
     * и выполняется отмена зачисления.
     * 2. Если списание было успешным, а зачисление неудачным, то транзакция помечается как невыполненная
     * и выполняется отмена списания.
     * 3. Если и списание, и зачисление были успешными, то транзакция помечается как завершённая и
     * её данные записывается в лог.
     * 4. Если и списание, и зачисление были неудачными, то транзакция помечается как невыполненная и
     * её данные записывается в лог.
     */
    public void checkStatus() {
        if (Boolean.TRUE.equals(replenishment.getSuccessful()) && Boolean.TRUE.equals(withdrawal.getSuccessful())) {
            isSuccessful = true;
            writeToLog();
        }
        if (Boolean.FALSE.equals(withdrawal.getSuccessful()) && Boolean.TRUE.equals(replenishment.getSuccessful())) {
            isSuccessful = false;
            rollbackReplenishment();
        }
        if (Boolean.FALSE.equals(replenishment.getSuccessful()) && Boolean.TRUE.equals(withdrawal.getSuccessful())) {
            isSuccessful = false;
            rollbackWithdrawal();
        }
        if (Boolean.FALSE.equals(replenishment.getSuccessful()) && Boolean.FALSE.equals(withdrawal.getSuccessful())) {
            isSuccessful = false;
            writeToLog();
        }
    }

    /**
     * Отмена списания. На счёт отправителя возвращается списанная сумма и данные транзакции записываются в лог.
     */
    private void rollbackWithdrawal() {
        DoubleAdder adder = new DoubleAdder();
        adder.add(sender.getBalance());
        adder.add(amount);
        sender.setBalance(adder.sum());
        writeToLog();
    }

    /**
     * Отмена зачисления. Со счёта получателя списывается зачисленная сумма и данные транзакции записываются в лог.
     */
    private void rollbackReplenishment() {
        DoubleAdder adder = new DoubleAdder();
        adder.add(receiver.getBalance());
        adder.add(-amount);
        receiver.setBalance(adder.sum());
        writeToLog();
    }

    /**
     * Запись данных о транзакции в лог (на самом деле вывод в консоль).
     */
    private void writeToLog() {
        String string = """
                Транзакция: %s, счёт отправителя: %s, счёт получателя: %s, сумма: %s, успех: %b.
                """;
        System.out.printf((string), id, sender.getAccountNumber(), receiver.getAccountNumber(),
                FORMATTER.format(amount), isSuccessful);
    }
}
