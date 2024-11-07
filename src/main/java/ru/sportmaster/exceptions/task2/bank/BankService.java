package ru.sportmaster.exceptions.task2.bank;

public class BankService {
    public void transferFunds(BankAccount fromAccount, BankAccount toAccount, double amount) {
        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println("Успешно переведено: " + amount + " рублей со счета " + fromAccount + " на счет " + toAccount);
        } catch (Throwable e) {
            System.out.println("Ошибка!!!");
            e.printStackTrace();
        }
    }
}
