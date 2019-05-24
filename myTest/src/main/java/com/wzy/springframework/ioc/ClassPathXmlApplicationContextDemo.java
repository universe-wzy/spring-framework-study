package com.wzy.springframework.ioc;

import com.wzy.springframework.ioc.beans.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 * 学习applicationContext源码
 * 测试推送是否ok
 *
 * @author wangzhenyu
 * @create 2019-04-25 16:28
 */
public class ClassPathXmlApplicationContextDemo {
	public static void main(String[] args) {
		/**
		 * 常规的获得bean
		 */
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("application.xml");
		Person person = app.getBean("person", Person.class);
		System.out.println(person);
		String[] aliasNames = app.getAliases("person");
		for(String name : aliasNames){
			System.out.println(name);
		}
//		person.setAge(12);
//		Person person1 = app.getBean(Person.class);
//		System.out.println(person1);
//		Person person2 = app.getBean(Person.class, "zhangsan", 14, new Date());
//		System.out.println(person2);


		/**
		 * 获得工厂，不过传入的beanName得是个FactoryBean，也就是得是个工厂而不是工厂创建的bean
		 */
//		ClassPathXmlApplicationContext personFactory = (ClassPathXmlApplicationContext)app.getBean("&person");
//		System.out.println(personFactory);
	}
}
