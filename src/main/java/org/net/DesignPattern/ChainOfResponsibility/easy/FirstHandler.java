package org.net.DesignPattern.ChainOfResponsibility.easy;

public class FirstHandler extends Handler{



    public void handleRequest(String request){
        if("first".equals(request)){
            System.out.println("first!!!!!! finish");
        }
        else{
            super.setNextHandler(new SecondHandler());
            nextHandler.handleRequest(request);
        }
    }


}
