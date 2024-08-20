package org.net.DesignPattern.ChainOfResponsibility.hard.model;

import org.net.DesignPattern.ChainOfResponsibility.hard.Handler;
import org.net.DesignPattern.ChainOfResponsibility.hard.HandlerAnnotation;

@HandlerAnnotation(offset = 3)
public class ThirdHandler extends Handler {

    public void handleRequest(String request){
        if("third".equals(request)){
            System.out.println("third!!!!!! finish");
        }
        else{
            nextHandler.handleRequest(request);
        }
    }
}
