package org.net.DesignPattern.ChainOfResponsibility.hard;

public class Main {

    public static void main(String[] args) {

        HandlerManager handlerManager = new HandlerManager();
        handlerManager.init();
        handlerManager.handleRequest("first");

    }
}
