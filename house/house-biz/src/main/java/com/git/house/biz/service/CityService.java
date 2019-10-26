package com.git.house.biz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.git.house.common.model.City;
import com.google.common.collect.Lists;

@Service
public class CityService {

	public List<City> getAllCitys() {
		City city=new City();
		city.setCityCode("06516");
		city.setCityName("NEW HAVEN");
		city.setId(1);
		return Lists.newArrayList(city);
	}

}
