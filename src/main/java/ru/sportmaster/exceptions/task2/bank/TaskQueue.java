package ru.sportmaster.exceptions.task2.bank;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Класс реализует очередь операций для отдельного счёта.
 * Наследуется от класса ConcurrentLinkedQueue, чтобы использовать
 * некоторые его методы.
 */
public class TaskQueue extends ConcurrentLinkedQueue<Operation> {

    /**
     * Признак, что очередь находится в состоянии обработки.
     */
    private boolean isProcessed;

    /**
     * По умолчанию операционная очередь создаётся с признаком, что обработка не производится.
     */
    public TaskQueue() {
        isProcessed = false;
    }

    /**
     * Процессинг очереди операций. Сперва проверяет, что обработка
     * ещё не запущена. Далее проставляется признак, что запущена обработка.
     * В цикле из очереди извлекается самая первая операция и запускается
     * её выполнение и так, пока очередь не опустеет. После чего проставляется
     * признак, что обработка очереди остановлена.
     */
    void process() {
        if (!isProcessed) {
            isProcessed = true;
            while (!isEmpty()) {
                poll().operate();
            }
            isProcessed = false;
        }
    }
}
