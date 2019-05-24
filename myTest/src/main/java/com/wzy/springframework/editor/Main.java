package com.wzy.springframework.editor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 * AspectDemo
 *
 * @author wangzhenyu
 * @create 2019-05-13 00:22
 */
public class Main {
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("application.xml");
		UserManager userManager = app.getBean("userManager", UserManager.class);
		System.out.println(userManager);
	}
}
