package com.wzy.springframework.lang;

import com.wzy.springframework.ioc.beans.Person;

/**
 * 描述:
 * 引用测试
 *
 * @author wangzhenyu
 * @create 2019-05-07 09:14
 */
public class ReferenceTest {
	public static void main(String[] args) {
//		Person p1 = new Person();
//		Person p2 = p1;
//		p1 = new Person();
//		p1.setAge(12);
//		System.out.println(p1);
//
//		p1 = p2;
//		System.out.println(p1);

		String s = "aaaaaa;";
		System.out.println(s.substring(0, s.length() - 1));
	}
}
