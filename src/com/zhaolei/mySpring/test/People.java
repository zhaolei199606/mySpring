package com.zhaolei.mySpring.test;

/**
 * @author zhaolei144
 * @date : 2020-01-07
 */
public class People {
    private String name;
    private String address;
    private Cat cat;

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", cat=" + cat +
                '}';
    }

}
