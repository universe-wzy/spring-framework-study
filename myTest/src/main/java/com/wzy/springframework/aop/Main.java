package com.wzy.springframework.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-14 15:37
 */
public class Main {
	public static void main(String[] args) {

		ApplicationContext app = new ClassPathXmlApplicationContext("aspectTest.xml");
		long start = System.currentTimeMillis();
		TestBean testBean = app.getBean("AspectDemo", TestBean.class);
		testBean.test();
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
	}
}
