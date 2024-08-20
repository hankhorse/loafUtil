package org.net.DesignPattern.ChainOfResponsibility.hard.model;

import org.net.DesignPattern.ChainOfResponsibility.hard.Handler;
import org.net.DesignPattern.ChainOfResponsibility.hard.HandlerAnnotation;

@HandlerAnnotation(offset = 2)
public class SecondHandler extends Handler {

    public void handleRequest(String request){
        if("second".equals(request)){
            System.out.println("second!!!!!! finish");
        }
        else{
            nextHandler.handleRequest(request);
        }
    }
}
