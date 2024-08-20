package org.net.DesignPattern.ChainOfResponsibility.hard.model;

import org.net.DesignPattern.ChainOfResponsibility.hard.Handler;
import org.net.DesignPattern.ChainOfResponsibility.hard.HandlerAnnotation;

@HandlerAnnotation(offset = 1)
public class FirstHandler  extends Handler {

    public void handleRequest(String request){
        if("first".equals(request)){
            System.out.println("first!!!!!! finish");
        }
        else{
            nextHandler.handleRequest(request);
        }
    }
}
