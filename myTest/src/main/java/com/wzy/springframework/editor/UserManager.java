package com.wzy.springframework.editor;

import java.util.Date;

/**
 * 描述:
 * AspectDemo
 *
 * @author wangzhenyu
 * @create 2019-05-13 00:20
 */
public class UserManager {

	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "UserManager{" +
				"date=" + date +
				'}';
	}
}
