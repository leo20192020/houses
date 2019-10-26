package com.git.house.biz.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.house.common.model.House;
import com.git.house.common.page.PageParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import redis.clients.jedis.Jedis;

@Service
public class RecommendService {

	private static final String HOT_HOUSE_KEY="hot_house";
//	private static final Logger logger=LoggerFactory.getLogger(RecommendService.class);
	
	@Autowired
	private HouseService houseService;
	
	public void increase(Long id) {
		Jedis jedis=new Jedis("127.0.0.1");
		jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id+"");
		jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);
		jedis.close();
	}
	
	public List<Long> getHot(){
		Jedis jedis=new Jedis("127.0.0.1");
		Set<String> idSet=jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
		List<Long> ids=idSet.stream().map(Long::parseLong).collect(Collectors.toList());
		return ids;
	}
	
	/*
	 * a.id in … CAN NOT sorted !! So we need to update this method
	 */
//	public List<House> getHotHouse(Integer size){
//		House query=new House();
//		query.setIds(getHot());
//		return houseService.queryAndSetupImg(query, PageParams.build(size, 1));
//		
//	}
	
	
	public List<House> getHotHouse(Integer size){
		House query=new House();
		List<Long> list = getHot();
		list=list.subList(0, Math.min(list.size(), size));
		if(list.isEmpty()) {
			return Lists.newArrayList();
		}
		query.setIds(list);
		List<Long> order=list;
		List<House> houses=houseService.queryAndSetupImg(query, PageParams.build(size, 1));
		Ordering<House> houseSort=Ordering.natural().onResultOf(hs->{
			return order.indexOf(hs.getId());
		});
		
		return houseSort.sortedCopy(houses);
	}

	public List<House> getLastest() {
		House query=new House();
		query.setSort("create_time");
		List<House> houses = houseService.queryAndSetupImg(query, new PageParams(8,1));
		return houses;
	}
	
	
	
	
	
}
