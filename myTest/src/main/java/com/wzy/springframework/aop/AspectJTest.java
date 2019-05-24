package com.wzy.springframework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-14 15:30
 */
@Aspect
public class AspectJTest {
	@Pointcut("execution(* *.test(..))")
	public void test(){

	}

	@Before("test()")
	public void beforeTest(){
		System.out.println("beforeTest");
	}

	@After("test()")
	public void afterTest(){
		System.out.println("afterTest");
	}

	@Around("test()")
	public Object arountTest(ProceedingJoinPoint p){
		System.out.println("before1");
		Object obj = null;
		try{
			obj = p.proceed();
		}catch (Throwable e){
			e.printStackTrace();
		}
		System.out.println("after1");

		return obj;
	}
}
