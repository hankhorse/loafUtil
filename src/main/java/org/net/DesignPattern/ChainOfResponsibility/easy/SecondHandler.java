package org.net.DesignPattern.ChainOfResponsibility.easy;

public class SecondHandler extends Handler{


    public void handleRequest(String request){
        if("second".equals(request)){
            System.out.println("second!!!!!! finish");
        }
    }
}
