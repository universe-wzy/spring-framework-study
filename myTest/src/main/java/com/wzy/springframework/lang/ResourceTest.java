package com.wzy.springframework.lang;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-06 14:44
 */
public class ResourceTest extends ResourceImpl {

	@Override
	public boolean isExist() {
		return false;
	}

	@Override
	public boolean isFile() {
		return false;
	}
}
