/**
*  需要依赖
 *   <dependency>
 *             <groupId>org.apache.commons</groupId>
 *             <artifactId>commons-lang3</artifactId>
 *             <version>3.12.0</version>
 *         </dependency>
**/
package org.net.Util;


import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
*  Object 对象相关操作
**/
public class ObjectUtil extends ObjectUtils {

    /**
     * 将一个对象的属性复制到另一个对象中
     *
     * @param src 要复制的源对象
     * @param clazz 目标对象的类
     * @param <T> 目标对象的类型
     * @return 返回复制后的目标对象
     */
    public static <T> T copy(Object src, Class<T> clazz){
        T t = null;
        if(src!=null){
            try{
                t =clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(src,t);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }

        }
        return t;
    }

    /**
     * 将一个对象深拷贝到另一个对象中
     *
     * @param src 要复制的源对象
     * @param <T> 目标对象的类型
     * @return 返回复制后的目标对象
     */
    public static <T extends Serializable> T deepCopy(Object src, Class<T> clazz){
        if (src == null) {
            return null;
        }
        T t = null;
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
        ) {
            // 序列化源对象
            oos.writeObject(src);
            oos.flush();

            // 反序列化生成新对象
            try (
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bais);
            ) {
                Object obj = ois.readObject();
                t = clazz.cast(obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
    *  判断对象是否为空
    **/
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        // 检查 String 类型
        if (object instanceof String str) {
            return str.trim().isEmpty();
        }
        // 检查 CharSequence 类型
        else if (object instanceof CharSequence cs) {
            return cs.isEmpty();
        }
        // 检查 Object[] 类型
        else if (object instanceof Object[] arr) {
            return arr.length == 0;
        }
        // 检查一般数组类型
        else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }
        // 检查 Collection 类型
        else if (object instanceof Collection<?> collection) {
            return collection.isEmpty();
        }
        // 检查 Map 类型
        else if (object instanceof Map<?, ?> map) {
            return map.isEmpty();
        }
        return false;
    }

    /**
    *   将对象转换为Map
    **/
    public static Map<String,Object> obj2Map(Object obj){
        Map<String,Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getFields();
        for(Field field:fields){
            field.setAccessible(true);
            try{
                map.put(field.getName(),field.get(obj));
            }catch (IllegalAccessException e){
               e.printStackTrace();
            }
        }
        return map;
    }

}
