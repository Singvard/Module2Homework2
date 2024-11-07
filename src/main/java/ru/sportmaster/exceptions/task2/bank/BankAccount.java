package ru.sportmaster.exceptions.task2.bank;

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void withdraw(double amount) throws RuntimeException {
        balance -= amount;
    }

    public void deposit(double amount) throws Throwable {
        if (amount <= 0) {
            throw new RuntimeException("Повторите операцию.");
        }
        balance += amount;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
