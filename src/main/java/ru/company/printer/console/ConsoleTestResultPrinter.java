package ru.company.printer.console;

import ru.company.printer.TestResultPrinter;

public class ConsoleTestResultPrinter implements TestResultPrinter {
    @Override
    public void printResult(String result) {
        System.out.println(result);
    }
}
