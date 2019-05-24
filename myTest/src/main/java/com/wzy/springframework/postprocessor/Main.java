package com.wzy.springframework.postprocessor;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-13 10:20
 */
public class Main {
	public static void main(String[] args) {
		ConfigurableListableBeanFactory bf = new XmlBeanFactory(new ClassPathResource("application.xml"));

		BeanFactoryPostProcessor bfp = bf.getBean("bfpp", BeanFactoryPostProcessor.class);
		bfp.postProcessBeanFactory(bf);
		System.out.println(bf.getBean("simpleBean", SimplePostProcessor.class));


	}
}
