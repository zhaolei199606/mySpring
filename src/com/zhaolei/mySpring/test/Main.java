package com.zhaolei.mySpring.test;

import com.zhaolei.mySpring.SpringIoc;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author zhaolei144
 * @date : 2020-01-07
 */
public class Main {
    public static void main(String[] args) throws IllegalAccessException, ParserConfigurationException, NoSuchFieldException, SAXException, IOException {
        SpringIoc springIoc = new SpringIoc();
        //加载配置文件
        springIoc.loadBean("C:\\Users\\zhaolei144\\IdeaProjects\\mySpring\\src\\com\\zhaolei\\mySpring\\test\\application.xml");
        People people = (People)springIoc.getBean("people");
        System.out.println(people);

    }
}
