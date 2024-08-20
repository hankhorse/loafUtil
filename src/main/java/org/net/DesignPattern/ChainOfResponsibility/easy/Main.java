package org.net.DesignPattern.ChainOfResponsibility.easy;

public class Main {


    public static void main(String[] args)
    {
        FirstHandler firstHandler = new FirstHandler();
        firstHandler.handleRequest("second");

    }
}
