package com.wzy.springframework.lookup;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-07 10:59
 */
public class Teacher extends User {
	@Override
	public void showMe(){
		System.out.println("i am teacher");
	}
}
