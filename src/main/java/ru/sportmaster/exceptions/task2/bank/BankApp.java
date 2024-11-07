package ru.sportmaster.exceptions.task2.bank;

public class BankApp {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("12480", 500.0);
        BankAccount account2 = new BankAccount("69001", 300.0);

        BankService bankService = new BankService();

        // Успешный перевод
        bankService.transferFunds(account1, account2, 100);

        // Отклоненный перевод
        bankService.transferFunds(account1, account2, 600);
    }
}
