package org.net.DesignPattern.ChainOfResponsibility.easy;

import lombok.Setter;

/**
*  责任链 头接口
**/
@Setter
public abstract class Handler {

    //下一个责任链
    public Handler nextHandler;

    // 处理的具体方法
    public void handleRequest(String request) {

    }


}
