package org.net.Class;


import org.net.Model.Entity.Student;

import java.util.Optional;

/**
* @Author : YangFeng
* @Desc :  Option 类练习
* @Date : 2024/7/3
**/
public class Option {

  public static void main(String[] args) {

    Student student = new Student("张三", 18, "男");

    Student studentNul = null;

    Optional.of(student).stream().map(s-> s.getName());
    // ofNullable赋值一个 ifPresentOrElse如果不为空执行第一个 如果为空 执行第二个
    Optional.ofNullable(student).ifPresentOrElse(s->student.setAge(1111), ()->System.out.println("student is null"));

    studentNul = Optional.ofNullable(studentNul).orElse(new Student("五万", 118, "女"));

    System.out.println(student.toString());
    System.out.println(studentNul);
  }
}
