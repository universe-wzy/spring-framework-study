package com.wzy.springframework.factorybean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-09 15:22
 */
public class Main {
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("application.xml");
		Car car = app.getBean("car", Car.class);
		System.out.println(car);

		CarFactoryBean carFactoryBean = app.getBean("&car", CarFactoryBean.class);
		System.out.println(carFactoryBean.getCarInfo());
	}
}
