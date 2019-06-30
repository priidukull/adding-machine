package com.github.priidukull;

public class Main {
    public static void main(String[] args) {
        AddingMachine machine = new AddingMachine();
        Thread t = new Thread(machine);
        t.start();
    }
}
