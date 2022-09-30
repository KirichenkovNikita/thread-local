package ru.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomThreadLocalTest {
    private static final CustomThreadLocal<String> THREAD_LOCAL = new CustomThreadLocal<>();

    @Test
    void getWhenCallVariousThreadThenReturnVariousValue() throws InterruptedException {
        THREAD_LOCAL.cleanMap();

        THREAD_LOCAL.set("From main thread");
        Thread thread1 = new Thread(new TestRunnableClass(), "first_thread");
        thread1.start();
        Thread thread2 = new Thread(new TestRunnableClass(), "second_thread");
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("fromMainThread: " + THREAD_LOCAL.get());

        assertEquals(THREAD_LOCAL.getVariables().size(), 3);
    }

    @Test
    void getWhenCallOneThreadThenReturnOneValue() throws InterruptedException {
        THREAD_LOCAL.cleanMap();

        THREAD_LOCAL.set("first_value");
        String firstValue = THREAD_LOCAL.get();
        System.out.println("fromMainThread: " + firstValue);

        THREAD_LOCAL.set("second_value");
        String secondValue = THREAD_LOCAL.get();
        System.out.println("fromMainThread: " + secondValue);

        THREAD_LOCAL.set("third_value");
        String thirdValue = THREAD_LOCAL.get();
        System.out.println("fromMainThread: " + thirdValue);


        assertEquals(THREAD_LOCAL.getVariables().size(), 1);
        assertEquals(firstValue, "first_value");
        assertEquals(secondValue, "second_value");
        assertEquals(thirdValue, "third_value");
    }

    @Test
    void removeWhenRemoveThenAnswerNull() {
        THREAD_LOCAL.cleanMap();

        THREAD_LOCAL.set("first_value");
        String firstValue = THREAD_LOCAL.get();
        System.out.println("fromMainThread: " + firstValue);

        THREAD_LOCAL.remove();
        String secondValue = THREAD_LOCAL.get();
        System.out.println("fromMainThread: " + secondValue);

        assertEquals(THREAD_LOCAL.getVariables().size(), 0);
        assertEquals(firstValue, "first_value");
        assertNull(secondValue);
    }

    private static class TestRunnableClass implements Runnable {

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + " first threadLocal: "
                    + THREAD_LOCAL.get());

            THREAD_LOCAL.set(name + " thread value");
            System.out.println(name + " end threadLocal: "
                    + THREAD_LOCAL.get());
        }
    };
}