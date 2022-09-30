package ru.company.test;

import ru.company.CustomThreadLocal;
import ru.company.printer.TestResultPrinter;

public class CustomThreadLocalTest {
    private static final CustomThreadLocal<String> THREAD_LOCAL = new CustomThreadLocal<>();
    private static final String FIRST_TEST_TITLE = "First test - Two threads are created and a different value is stored in each thread." + System.lineSeparator();
    private static final String SECOND_TEST_TITLE = "Second test - Three different values are written to one thread in turn, while the variable is updated" + System.lineSeparator();
    private static final String THIRD_TEST_TITLE = "Third test - A value is written to one thread, then it is deleted, the array of variables becomes empty" + System.lineSeparator();
    private static final String TEXT_VARIABLE_COUNT = "In the end of the test, the table contains %x variable values";
    private static final String TEXT_VARIABLE_VALUE = "The value of the variable for the thread %s is %s";
    private final TestResultPrinter printer;

    public CustomThreadLocalTest(TestResultPrinter printer) {
        this.printer = printer;
    }

    public void getWhenCallVariousThreadThenReturnVariousValue() throws InterruptedException {
        printer.printResult(FIRST_TEST_TITLE);

        THREAD_LOCAL.cleanMap();

        Thread thread1 = new Thread(new TestRunnableClass(), "first_thread");
        thread1.start();
        Thread thread2 = new Thread(new TestRunnableClass(), "second_thread");
        thread2.start();
        thread1.join();
        thread2.join();

        printer.printResult(System.lineSeparator());
        printer.printResult(String.format(TEXT_VARIABLE_COUNT, THREAD_LOCAL.getVariables().size()));
    }

    public void getWhenCallOneThreadThenReturnOneValue() throws InterruptedException {
        printer.printResult(SECOND_TEST_TITLE);

        THREAD_LOCAL.cleanMap();

        THREAD_LOCAL.set("first_value");
        String firstValue = THREAD_LOCAL.get();
        printer.printResult("fromMainThread: " + firstValue);

        THREAD_LOCAL.set("second_value");
        String secondValue = THREAD_LOCAL.get();
        printer.printResult("fromMainThread: " + secondValue);

        THREAD_LOCAL.set("third_value");
        String thirdValue = THREAD_LOCAL.get();
        printer.printResult("fromMainThread: " + thirdValue);
        printer.printResult(System.lineSeparator());

        printer.printResult(String.format(TEXT_VARIABLE_COUNT, THREAD_LOCAL.getVariables().size()));
        printer.printResult("Current value is " + THREAD_LOCAL.get());
    }

    public void removeWhenRemoveThenAnswerNull() {
        printer.printResult(THIRD_TEST_TITLE);

        THREAD_LOCAL.cleanMap();

        THREAD_LOCAL.set("first_value");
        String firstValue = THREAD_LOCAL.get();
        printer.printResult("fromMainThread: " + firstValue);

        THREAD_LOCAL.remove();
        String secondValue = THREAD_LOCAL.get();
        printer.printResult("fromMainThread: " + secondValue);
        printer.printResult(System.lineSeparator());

        printer.printResult(String.format(TEXT_VARIABLE_COUNT, THREAD_LOCAL.getVariables().size()));
        printer.printResult(System.lineSeparator());

        printer.printResult(String.format(TEXT_VARIABLE_VALUE, "first_value", firstValue));
        printer.printResult(String.format(TEXT_VARIABLE_VALUE, "second_value", secondValue));
    }

     private class TestRunnableClass implements Runnable {

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            THREAD_LOCAL.set("\"" + name + "\"" + " thread value");
            printer.printResult(name + " end threadLocal: "
                    + THREAD_LOCAL.get());
        }
    };
}