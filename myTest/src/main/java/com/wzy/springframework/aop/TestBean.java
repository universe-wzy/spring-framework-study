package com.wzy.springframework.aop;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-14 15:28
 */
public class TestBean {

	private String testStr = "testStr";

	public String getTestStr(){
		return testStr;
	}

	public void setTestStr(String testStr){
		this.testStr = testStr;
	}

	public void test(){
		System.out.println("test");
	}
}
