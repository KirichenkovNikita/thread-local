package ru.company;

import ru.company.printer.console.ConsoleTestResultPrinter;
import ru.company.test.CustomThreadLocalTest;

public class ThreadLocalApplication {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadLocalTest threadLocalTest = new CustomThreadLocalTest(new ConsoleTestResultPrinter());
        System.out.println("---------FIRST TEST---------\n");
        threadLocalTest.getWhenCallVariousThreadThenReturnVariousValue();
        System.out.println("\n\n\n---------SECOND TEST---------\n");
        threadLocalTest.getWhenCallOneThreadThenReturnOneValue();
        System.out.println("\n\n\n---------THIRD TEST---------\n");
        threadLocalTest.removeWhenRemoveThenAnswerNull();
    }
}
