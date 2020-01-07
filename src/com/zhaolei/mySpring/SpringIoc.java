package com.zhaolei.mySpring;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于实现spring Ioc的功能
 * 用map作为bean工厂
 *
 * @author zhaolei144
 * @date : 2020-01-07
 */
public class SpringIoc {
    private Map<String,Object> map = new HashMap<>();
    /**
     * location为文件路径
     * @param location
     */
    public void loadBean(String location) throws ParserConfigurationException, IOException, SAXException, NoSuchFieldException,
            IllegalAccessException {
        //加载xml文件，为了代码整洁，所有异常用throws方式
        InputStream inputStream = new FileInputStream(location);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        //遍历<bean>标签，获取对应的class和id属性的值加载对应的类
        for(int i=0;i<nodes.getLength();i++){
            Node node = nodes.item(i);
            if(node instanceof Element){
                Element elem = (Element)node;
                String id = elem.getAttribute("id");//获取id标签的值
                String classStr = elem.getAttribute("class");//获取class标签的值
                Class clazz = null;
                Object obj = null;
                try {
                    //加载对应的类
                    clazz = Class.forName(classStr);
                    //生成对应的实例对象
                    obj = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //遍历<property>
                NodeList propertyNodes = elem.getElementsByTagName("property");
                for(int j =0;j<propertyNodes.getLength();j++){
                    Node propertyNode = propertyNodes.item(j);
                    if(propertyNode instanceof Element){
                        Element propertyElement = (Element)propertyNode;
                        String name = propertyElement.getAttribute("name");//获取name属性
                        String value = propertyElement.getAttribute("value");//获取value属性
                        //将对应的属性放到对象中
                        Field field = clazz.getDeclaredField(name);
                        field.setAccessible(true);//将属性权限设置为可访问
                        if(value != null && value.length()>0){//注意当没有value标签时会返回"",需要加上长度判断
                            field.set(obj,value);//设置对应的属性，这个地方可以设置不同类型的值
                        }else{
                            //考虑类中引入另一个类的情况
                            String ref = ((Element) propertyNode).getAttribute("ref");
                            field.set(obj,getBean(ref));
                        }
                    }
                }
                //将obj放到spring工厂中
                map.put(id,obj);
            }
        }

    }
    //获取bean方法
    public Object getBean(String bean){
        return map.get(bean);
    }
}
