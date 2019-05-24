package com.wzy.springframework.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述:
 * AspectDemo
 *
 * @author wangzhenyu
 * @create 2019-05-13 00:24
 */
public class DatePropertyEditor extends PropertyEditorSupport {

	String format = "yyyy-MM-dd";

	public void setFormat(String format){
		this.format = format;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		System.out.println("text: " + text);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			Date date = sdf.parse(text);
			this.setValue(date);
		}catch (ParseException e){
			e.printStackTrace();
		}
	}
}
