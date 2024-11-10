package ru.sportmaster.exceptions.task2.bank;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class BankApp {

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
//        stressTest(); //Осторожно, 9 миллионов транзакций за 3-4 минуты!!!
    }

    /**
     * Успешный перевод всех дублонов с одного счёта на другой.
     */
    private static void test1() {
        BankAccount account1 = new BankAccount("Счёт 1", 100);
        BankAccount account2 = new BankAccount("Счёт 2", 100);
        BankService bankService = new BankService();
        bankService.transferFunds(account1, account2, 100);

        if (account1.getBalance() == 0 && account2.getBalance() == 200) {
            System.out.println("Тест1 пройден");
        } else {
            System.out.println("Тест1 не пройден");
        }
    }

    /**
     * Неудачный перевод средств, т.к. дублонов на счёте отправителя меньше, чем сумма перевода.
     */
    private static void test2() {
        BankAccount account1 = new BankAccount("Счёт 1", 100);
        BankAccount account2 = new BankAccount("Счёт 2", 100);
        BankService bankService = new BankService();
        bankService.transferFunds(account1, account2, 200);

        if (account1.getBalance() == 100 && account2.getBalance() == 100) {
            System.out.println("Тест2 пройден");
        } else {
            System.out.println("Тест2 не пройден");
        }
    }

    /**
     * Неудачный перевод средств, т.к. счёт получателя закрыт.
     */
    private static void test3() {
        BankAccount account1 = new BankAccount("Счёт 1", 200);
        BankAccount account2 = new BankAccount("Счёт 2", 0);
        account2.setClosed();
        BankService bankService = new BankService();
        bankService.transferFunds(account1, account2, 100);

        if (account1.getBalance() == 200 && account2.getBalance() == 0) {
            System.out.println("Тест3 пройден");
        } else {
            System.out.println("Тест3 не пройден");
        }
    }

    /**
     * Неудачный перевод средств, т.к. счёт отправителя считается мошенническим.
     */
    private static void test4() {
        BankAccount account1 = new BankAccount("Счёт 1", 200);
        BankAccount account2 = new BankAccount("Счёт 2", 100);
        account1.setFraud(true);
        BankService bankService = new BankService();
        bankService.transferFunds(account1, account2, 200);

        if (account1.getBalance() == 200 && account2.getBalance() == 100) {
            System.out.println("Тест4 пройден");
        } else {
            System.out.println("Тест4 не пройден");
        }
    }

    /**
     * Имитация донатов от страждущих школотронов в топовом камхор-чате.
     * 9 счетов с 10кк дублонов каждый засылают на 10-й счёт по 1 дублону в каждой транзакции.
     * Всё крутится в цикле на 1кк итераций, в каждой итерации происходит 9 одновременных
     * транзакций. Несложно подсчитать, что таким образом будет произведено 9кк транзакций,
     * т.е. 10-й счёт получит 9кк дублонов. Такое же количество средств должно остаться на
     * каждом счёт-отправителе (богатые нынче пошли школотроны).
     */
    private static void stressTest() {
        long startTime = System.nanoTime();

        BankAccount account1 = new BankAccount("Счёт 1", 10_000_000);
        BankAccount account2 = new BankAccount("Счёт 2", 10_000_000);
        BankAccount account3 = new BankAccount("Счёт 3", 10_000_000);
        BankAccount account4 = new BankAccount("Счёт 4", 10_000_000);
        BankAccount account5 = new BankAccount("Счёт 5", 10_000_000);
        BankAccount account6 = new BankAccount("Счёт 6", 10_000_000);
        BankAccount account7 = new BankAccount("Счёт 7", 10_000_000);
        BankAccount account8 = new BankAccount("Счёт 8", 10_000_000);
        BankAccount account9 = new BankAccount("Счёт 9", 10_000_000);
        BankAccount account10 = new BankAccount("Счёт 10", 0);

        BankService bankService = new BankService();

        Callable<Void> task1 = () -> {
            bankService.transferFunds(account1, account10, 1);
            return null;
        };
        Callable<Void> task2 = () -> {
            bankService.transferFunds(account2, account10, 1);
            return null;
        };
        Callable<Void> task3 = () -> {
            bankService.transferFunds(account3, account10, 1);
            return null;
        };
        Callable<Void> task4 = () -> {
            bankService.transferFunds(account4, account10, 1);
            return null;
        };
        Callable<Void> task5 = () -> {
            bankService.transferFunds(account5, account10, 1);
            return null;
        };
        Callable<Void> task6 = () -> {
            bankService.transferFunds(account6, account10, 1);
            return null;
        };
        Callable<Void> task7 = () -> {
            bankService.transferFunds(account7, account10, 1);
            return null;
        };
        Callable<Void> task8 = () -> {
            bankService.transferFunds(account8, account10, 1);
            return null;
        };
        Callable<Void> task9 = () -> {
            bankService.transferFunds(account9, account10, 1);
            return null;
        };

        boolean tasksCompleted;
        try (ExecutorService executorService = Executors.newFixedThreadPool(9)) {
            List<Callable<Void>> tasks = Arrays.asList(task1, task2, task3, task4, task5, task6, task7, task8, task9);
            for (int i = 0; i < 1_000_000; i++) {
                try {
                    executorService.invokeAll(tasks);
                } catch (InterruptedException e) {
                    // Перехватываем InterruptedException и повторно устанавливаем флаг прерывания
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            executorService.shutdown();
            try {
                tasksCompleted = executorService.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                tasksCompleted = false; // Если ожидание было прервано, отмечаем, что задачи не завершились
            }

            if (tasksCompleted) {
                System.out.println("Все задачи успешно завершены!");
            } else {
                System.out.println("Некоторые задачи не успели завершиться в отведенное время!");
            }

            System.out.println(account1);
            System.out.println(account2);
            System.out.println(account3);
            System.out.println(account4);
            System.out.println(account5);
            System.out.println(account6);
            System.out.println(account7);
            System.out.println(account8);
            System.out.println(account9);
            System.out.println(account10);

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            double seconds = (double) elapsedTime / 1_000_000_000;
            System.out.printf("Время выполнения: %.3f s\n", seconds);
        }
    }
}
