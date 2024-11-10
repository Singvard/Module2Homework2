package ru.sportmaster.exceptions.task2.bank;

import ru.sportmaster.exceptions.task2.bank.exceptions.*;

/**
 * Класс реализует лицевой счёт клиента.
 */
public class BankAccount {
    /**
     * Номер счёта.
     */
    private final String accountNumber;

    /**
     * Баланс - количество денежных средств на счёте. Не может быть отрицательным.
     */
    private double balance;

    /**
     * Признак мошеннического счёта. Изменить признак может только специалист банка.
     * Если true - то запрет на получение/вывод денежных средств.
     */
    private boolean isFraud;

    /**
     * Признак закрытого счёта. Клиент может закрыть счёт самостоятельно или через
     * оператора банка. Счёт можно закрыть только при нулевом балансе.
     * Если true - счёт закрыт.
     */
    private boolean isClosed;

    /**
     * Очередь операций для счёта.
     */
    private final TaskQueue taskQueue;

    /**
     * Открытие нового счёта. По умолчанию создается с признаками не закрытого и не мошеннического.
     *
     * @param accountNumber Номер счёта.
     * @param balance       Баланс на счёте.
     */
    public BankAccount(String accountNumber, double balance) {
        if (balance < 0) throw new NegativeBalanceException();

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isFraud = false;
        this.isClosed = false;
        this.taskQueue = new TaskQueue();
    }

    /**
     * Получение номера счёта.
     *
     * @return Номер текущего счёта.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Получение баланса счёта.
     *
     * @return Количество денежных средств на балансе счёта.
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Изменение баланса текущего счёта, используется только для отмены операций.
     *
     * @param balance Баланс текущего счёта.
     */
    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Отображение признака заблокированного за подозрение в мошенничестве счёта.
     *
     * @return Признак мошеннического счёта.
     */
    public boolean isFraud() {
        return isFraud;
    }

    /**
     * Изменение признака заблокированного за подозрение в мошенничестве счёта.
     * Клиент не может изменять признак самостоятельно.
     * Нельзя изменять у закрытого счёта.
     * Нельзя отметить как мошеннический тот, который уже мошеннический.
     * Нельзя отметить как не мошеннический тот, который уже не мошеннический.
     *
     * @param fraud Признак мошеннического счёта (true/false).
     */
    public void setFraud(boolean fraud) {
        if (isClosed) {
            throw new TryToMarkAsFraudClosedAccountException();
        }
        if (isFraud && fraud) {
            throw new AlreadyMarkedAsFraudException();
        } else if (!isFraud && !fraud) {
            throw new AlreadyNonFraudulentException();
        } else {
            isFraud = fraud;
        }
    }

    /**
     * Получение признака открытого/закрытого счёта.
     *
     * @return Признак открытого/закрытого счёта.
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Изменяет признак закрытого/открытого счёта.
     * Клиент может закрыть счёт самостоятельно или через оператора банка.
     * Счёт можно закрыть только при нулевом балансе.
     * Закрытый счёт нельзя открыть.
     */
    public void setClosed() {
        if (isClosed) {
            throw new AlreadyIsClosedException();
        }
        if (balance != 0) {
            throw new NonZeroBalanceException();
        }
        isClosed = true;
    }

    /**
     * Получение операционной очереди текущего счёта.
     *
     * @return Очередь операций.
     */
    public TaskQueue getTaskQueue() {
        return taskQueue;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

    /**
     * Примитивные самописные юнит-тесты.
     *
     * @param args Стандартный параметр.
     */
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    private static void test1() {
        try {
            new BankAccount("1", -1);
            System.out.println("Тест1: не пройден");
        } catch (NegativeBalanceException e) {
            if (e.getMessage().equals("Создание счёта с отрицательным балансом запрещено!")) {
                System.out.println("Тест1: пройден");
            }
        }
    }

    private static void test2() {
        BankAccount account = new BankAccount("1", 1);
        account.isClosed = true;
        try {
            account.setFraud(true);
            System.out.println("Тест2: не пройден");
        } catch (TryToMarkAsFraudClosedAccountException e) {
            if (e.getMessage().equals("Операция не выполнена: лицевой счёт закрыт!")) {
                System.out.println("Тест2: пройден");
            }
        }
    }

    private static void test3() {
        BankAccount account = new BankAccount("1", 1);
        account.isFraud = true;
        try {
            account.setFraud(true);
            System.out.println("Тест3: не пройден");
        } catch (AlreadyMarkedAsFraudException e) {
            if (e.getMessage().equals("Операция не выполнена: счёт уже подозревается в мошенничестве!")) {
                System.out.println("Тест3: пройден");
            }
        }
    }

    private static void test4() {
        BankAccount account = new BankAccount("1", 1);
        try {
            account.setFraud(false);
            System.out.println("Тест4: не пройден");
        } catch (AlreadyNonFraudulentException e) {
            if (e.getMessage().equals("Операция не выполнена: счёт уже не считается мошенническим!")) {
                System.out.println("Тест4: пройден");
            }
        }
    }

    private static void test5() {
        BankAccount account = new BankAccount("1", 1);
        account.isClosed = true;
        try {
            account.setClosed();
            System.out.println("Тест5: не пройден");
        } catch (AlreadyIsClosedException e) {
            if (e.getMessage().equals("Операция не выполнена: счёт уже закрыт!")) {
                System.out.println("Тест5: пройден");
            }
        }
    }

    private static void test6() {
        BankAccount account = new BankAccount("1", 1);
        try {
            account.setClosed();
            System.out.println("Тест6: не пройден");
        } catch (NonZeroBalanceException e) {
            if (e.getMessage().equals("Невозможно закрыть счёт при ненулевом балансе. Выведите средства и повторите попытку!")) {
                System.out.println("Тест6: пройден");
            }
        }
    }
}
