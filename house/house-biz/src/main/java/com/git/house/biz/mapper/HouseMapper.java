package com.git.house.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.git.house.common.model.Community;
import com.git.house.common.model.House;
import com.git.house.common.model.HouseUser;
import com.git.house.common.model.UserMsg;
import com.git.house.common.page.PageParams;
@Mapper
public interface HouseMapper {

	List<Community> selectCommunity(Community community);

	Long selectPageCount(House query);

	List<House> selectPageHouses(@Param("house")House query, @Param("pageParams")PageParams pageParams);

	void insertUserMsg(UserMsg userMsg);

	void insert(House house);

	void insertHouseUser(HouseUser houseUser);

	HouseUser selectHouseUser(@Param("userId")Long userId, 
			@Param("houseId")Long houseId, @Param("type")Integer type);

	void updateHouse(House updateHouse);

	void downHouse(long houseId);

	void deleteHouseUser(@Param("id")long houseId, @Param("userId")Long userId, @Param("type")Integer type);
	
	
	
	
	
	
	
	

}
