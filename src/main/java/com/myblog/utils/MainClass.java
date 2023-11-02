package com.myblog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MainClass {
    public static void main(String[] args) {

       PasswordEncoder encoder = new BCryptPasswordEncoder();
       //System.out.println(encoder.encode("Mike"));
      System.out.println(encoder.encode("Deeksha"));
    }
}
////String name = "NItesh" ;
////        long age = 27;
////        String FatherName = "Mr Rajesh";
////        System.out.println(String.format("%s is your name, age is %s and  father name is %s",name,age,FatherName));
//String name = "Nitesh";
//long age = 27 ;
//String fatherName = "Mr Rajesh";
//
//        System.out.println(String.format("%s is your name ,age is '%s' and fatherName is %s",name,age,fatherName));
