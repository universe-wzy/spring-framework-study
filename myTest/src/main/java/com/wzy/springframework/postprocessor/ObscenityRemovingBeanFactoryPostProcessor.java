package com.wzy.springframework.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringValueResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * 描述:
 * AspectDemo
 *
 * @author wangzhenyu
 * @create 2019-05-13 10:04
 */
public class ObscenityRemovingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	private Set<String> obscenties;

	public ObscenityRemovingBeanFactoryPostProcessor(){
		this.obscenties = new HashSet<>();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String[] beanNames = beanFactory.getBeanDefinitionNames();

		for(String beanName : beanNames){
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
			StringValueResolver valueResolver = strVal -> {
				if(isObscene(strVal)) {
					return "******";
				}
				return strVal;
			};

			BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
			visitor.visitBeanDefinition(beanDefinition);
		}
	}

	private boolean isObscene(Object strVal) {
		String potentialObscenity = strVal.toString();
		return this.obscenties.contains(potentialObscenity);
	}

	public Set<String> getObscenties() {
		return obscenties;
	}

	public void setObscenties(Set<String> obscenties) {
		this.obscenties = obscenties;
	}
}
