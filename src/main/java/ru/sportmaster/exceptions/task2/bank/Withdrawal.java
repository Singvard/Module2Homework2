package ru.sportmaster.exceptions.task2.bank;

/**
 * Класс реализует списание денежных средств на отдельном счёте. Наследуется от операции.
 */
public class Withdrawal extends Operation {

    /**
     * Создание списание. Внимание: у суммы перевода меняется знак и она превращается в сумму
     * списания, т.к. при переводе средства у отправителя списываются.
     *
     * @param sender      Счёт отправителя.
     * @param amount      Сумма перевода.
     * @param transaction Связная транзакция.
     */
    public Withdrawal(BankAccount sender, double amount, Transaction transaction) {
        super(sender, -amount, transaction);
    }

    /**
     * Переопределённый метод родительского класса. Обрабатывает списание. Здесь
     * производится дополнительная проверка, что текущая сумма средств на счёте
     * больше, либо равна сумме перевода.
     * Если данная проверка, то отмечает операцию списания как неуспешную и
     * запускает проверку статуса транзакции.
     */
    @Override
    public void operate() {
        if (account.getBalance() >= -amount) {
            super.operate();
        } else {
            System.out.println("Операция не выполнена: недостаточно средств на счёте.");
            isSuccessful = false;
            transaction.checkStatus();
        }
    }

    /**
     * Примитивные самописные юнит-тесты.
     *
     * @param args Стандартный параметр.
     */
    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     * Успешное списание средств у отправителя.
     */
    private static void test1() {
        BankAccount sender = new BankAccount("1", 1000);
        BankAccount receiver = new BankAccount("2", 0);
        Transaction transaction = new Transaction(sender, receiver, 0);
        Withdrawal withdrawal = new Withdrawal(sender, 500, transaction);
        withdrawal.operate();
        if (sender.getBalance() == 500 && withdrawal.isSuccessful) {
            System.out.println("Тест1 пройден");
        } else {
            System.out.println("Тест1 провален");
        }
    }

    /**
     * Запрет на списание средств у отправителя - недостаточно средств.
     */
    private static void test2() {
        BankAccount sender = new BankAccount("1", 100);
        BankAccount receiver = new BankAccount("2", 0);
        Transaction transaction = new Transaction(sender, receiver, 0);
        Withdrawal withdrawal = new Withdrawal(sender, 101, transaction);
        withdrawal.operate();
        if (sender.getBalance() == 100 && !withdrawal.isSuccessful) {
            System.out.println("Тест2 пройден");
        } else {
            System.out.println("Тест2 провален" + sender.getBalance());
        }
    }
}