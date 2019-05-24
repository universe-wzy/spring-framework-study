package com.wzy.springframework.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-17 00:03
 */
public class SpringJDBCTest {
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("application-jdbc.xml");

		UserService userService = app.getBean("userService", UserService.class);
		User user = new User();
		user.setName("wang");
		user.setAge(27);
		user.setSex("male");
		userService.save(user);

		System.out.println(userService.getUsers());
	}
}
