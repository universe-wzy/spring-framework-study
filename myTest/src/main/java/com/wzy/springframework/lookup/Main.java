package com.wzy.springframework.lookup;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-07 11:01
 */
public class Main {
	Main(User user){
		user.showMe();
	}

	Main(Teacher teacher){
		teacher.showMe();
	}

	public static void main(String[] args) {
//		ApplicationContext app = new ClassPathXmlApplicationContext("application.xml");
//		GetBeanTest AspectDemo = app.getBean("getBeanTest", GetBeanTest.class);
//		AspectDemo.showMe();
		User user = new Teacher();
		new Main(user);
	}
}
