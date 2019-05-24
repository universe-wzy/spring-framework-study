package com.wzy.springframework.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 描述:
 * 自定义工厂
 *
 * @author wangzhenyu
 * @create 2019-05-09 15:17
 */
public class CarFactoryBean implements FactoryBean<Car> {

	private String carInfo = "超级跑车,400,2000000";

	@Override
	public Car getObject() throws Exception {

		Car car = new Car();
		String[] infos = carInfo.split(",");
		car.setBrand(infos[0]);
		car.setMaxSpeed(Integer.valueOf(infos[1]));
		car.setPrice(Double.valueOf(infos[2]));

		return car;
	}

	@Override
	public Class<Car> getObjectType() {
		return Car.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public String getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(String carInfo) {
		this.carInfo = carInfo;
	}
}
