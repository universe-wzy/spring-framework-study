package com.wzy.springframework;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-20 15:31
 */
public class Main {
	public static void main(String[] args) {
//		String s = "aaab";
//		System.out.println(s.substring(0, s.length() - 1));
		Test test = new Test();
//		test.f("aaa", "bbb", "ccc");
	}
}


class Test{
	void f(String str, String... a){
		System.out.println(str + a.toString());
	}

	void f(String... a){
		System.out.println(a);
	}
}
