package com.wzy.springframework.lookup;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-07 11:00
 */
public abstract class GetBeanTest {
	public void showMe(){
		this.getBean().showMe();
	}

	public abstract User getBean();
}
