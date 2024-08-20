package org.net.DesignPattern.ChainOfResponsibility.hard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class HandlerManager {

    /**
    *  责任链处理
    **/
    private  Map<Integer, Handler> handlerList = new HashMap<>();

    //所有offset的集合
    private List<Integer> offsetList;

    /**
    *  通过反射 获取所有带有 @HandlerAnnotation 注解的类，并按顺序存入handlerList
    **/
    public void init()
    {
        List<Class<?>> list = scanPackage("org.net.DesignPattern.ChainOfResponsibility.hard.model");
        //所有按照顺序排序 1、2、4、6、8
        offsetList = list.stream().map(clazz-> clazz.getAnnotation(HandlerAnnotation.class).offset()).sorted().toList();

        //所有的责任链的存入handlerList
        list.forEach(clazz->{
            HandlerAnnotation handlerAnnotation = clazz.getAnnotation(HandlerAnnotation.class);
            try {
                handlerList.put(handlerAnnotation.offset(), (Handler) clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        //按照顺序 依此给下一个责任链添加
       for(int i=0;i<offsetList.size()-1;i++){
           Handler handler = handlerList.get(offsetList.get(i));
           handler.setNextHandler(handlerList.get(offsetList.get(i+1)));
       }
    }

    /**
    * @Author : YangFeng
    * @Desc : 扫描指定包，返回包下所有类的对象集合
    * @Date : 2024/8/8
    **/
    public List<Class<?>> scanPackage(String packageName)
    {
        List<Class<?>> list = new ArrayList<>();
        //获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        try {
            //获取包下所有类
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                //获取包下所有类
                File file = new File(resource.getFile());
                //获取包下所有类
                File[] files = file.listFiles();
                for (File f : files) {
                    //获取包下所有类
                    String className = packageName + "."+ f.getName().replace(".class", "");
                    list.add(Class.forName(className));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }

    public void handleRequest(String request)
    {
        //获取第一个责任链
        Handler handler = handlerList.get(offsetList.get(0));
        //执行责任链
        handler.handleRequest(request);
    }

}
