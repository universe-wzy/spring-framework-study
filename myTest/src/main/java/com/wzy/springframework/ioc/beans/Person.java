package com.wzy.springframework.ioc.beans;

import java.util.Date;

/**
 * 描述:
 * bean
 *
 * @author wangzhenyu
 * @create 2019-04-29 14:40
 */
public class Person {
	private String name;
	private Integer age;
	private Date birthday;


//	public Person(String name, Integer age, Date birthday) {
//		this.name = name;
//		this.age = age;
//		this.birthday = birthday;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				", birthday=" + birthday +
				'}';
	}
}
