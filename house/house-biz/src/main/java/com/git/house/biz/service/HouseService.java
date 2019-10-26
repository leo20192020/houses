package com.git.house.biz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.git.house.biz.mapper.HouseMapper;
import com.git.house.common.constants.HouseUserType;
import com.git.house.common.model.Community;
import com.git.house.common.model.House;
import com.git.house.common.model.HouseUser;
import com.git.house.common.model.User;
import com.git.house.common.model.UserMsg;
import com.git.house.common.page.PageData;
import com.git.house.common.page.PageParams;
import com.git.house.common.utils.BeanHelper;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Service
public class HouseService {

	@Autowired
	private HouseMapper houseMapper;
	
	
	
	/*
	 * 1, query by Community name. NOT 22 locast st, it is like kenny apt. Name of community
	 * 2,add prefix to picts as they store in database without prefix
	 * 3,return pageData which can be recognized by front end 
	 */
	public PageData<House> queryHouse(House query, PageParams pageParams) {
		List<House> houses=Lists.newArrayList();
		if(Strings.isNullOrEmpty(query.getName())) {
			Community community=new Community();
			community.setName(query.getName());
			List<Community> communities=houseMapper.selectCommunity(community);
			if(!communities.isEmpty()) {
				query.setCommunityId(communities.get(0).getId());
			}
		}
		houses=queryAndSetupImg(query,pageParams);
		Long count=houseMapper.selectPageCount(query);
		
		return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
	}

	
	@Value("${file.prefix}")
	private String imgPrefix;
	
	
	//query and get List<house> and set up prefix
	public List<House> queryAndSetupImg(House query, PageParams pageParams) {
		query.setName(null);
		List<House> houses=houseMapper.selectPageHouses(query,pageParams);
		houses.forEach(house->{
			house.setFirstImg(imgPrefix+house.getFirstImg());
			house.setImageList(house.getImageList().stream().map(img->imgPrefix+img).collect(Collectors.toList()));
			house.setFloorPlanList(house.getFloorPlanList().stream().map(img->imgPrefix+img).collect(Collectors.toList()));
		});
		
		return houses;
	}

	//Just get one house with details
	public House queryOneHouse(Long id) {
		House query=new House();
		query.setId(id);
		List<House> houses=queryAndSetupImg(query,PageParams.build(1, 1));
		if(!houses.isEmpty()) {
			return houses.get(0);
		}
		return null;
	}

	public HouseUser getHouseUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Autowired
	private AgencyService agencyService;
	
	@Autowired
	private MailService mailService;
	
	/*
	 * package userMsg and store it into database
	 * send email to agent
	 */
	public void addUserMsg(UserMsg userMsg) {
		BeanHelper.onInsert(userMsg);
		houseMapper.insertUserMsg(userMsg);
		User agent=agencyService.getAgentDetail(userMsg.getAgentId());
		mailService.sendMail("A email coming from customer "+userMsg.getEmail()
				,userMsg.getMsg()
				, agent.getEmail());
	}

	public List<Community> getAllCommunities() {
		Community community=new Community();
		return houseMapper.selectCommunity(community);
	}

	
	/*
	 * add house into the datebase 
	 * 1, add imgs into database
	 * 2,add floorplan into database
	 * 3,insert house into house table
	 * 4, binding user and house into house_user table 
	 */
	@Autowired
	private FileService fileService;
	
	public void addHouse(House house, User user) {
		if(CollectionUtils.isNotEmpty(house.getHouseFiles())) {
			String images=Joiner.on(",").join(
					fileService.getImgPath(house.getHouseFiles()));
			house.setImages(images);
		}
		if(CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
			String images=Joiner.on(",").join(
					fileService.getImgPath(house.getFloorPlanFiles()));
			house.setFloorPlan(images);
		}

		BeanHelper.onInsert(house);
		houseMapper.insert(house);
		bindUser2House(house.getId(),user.getId(),false);
		
		
	}

	//binding this house to user
	//check if the relationship has been exit
	public void bindUser2House(Long houseId, Long userId, boolean collect) {
		HouseUser existHouseUser=houseMapper.selectHouseUser(userId,houseId, 
				collect?HouseUserType.BOOKMARK.value:HouseUserType.SALE.value);
		if(existHouseUser!=null) {
			return;
		}
		HouseUser houseUser=new HouseUser();
		houseUser.setHouseId(houseId);
		houseUser.setUserId(userId);
		houseUser.setType(collect?HouseUserType.BOOKMARK.value:HouseUserType.SALE.value);
		BeanHelper.setDefaultProp(houseUser, HouseUser.class);
		BeanHelper.onInsert(houseUser);
		houseMapper.insertHouseUser(houseUser);
	}

	public void updateRating(Long id, Double rating) {
		House house=queryOneHouse(id);
		Double oldRating=house.getRating();
		Double newRating=oldRating.equals(0D)?rating:Math.min((oldRating+rating)/2,5);
		House updateHouse=new House();
		updateHouse.setId(id);
		updateHouse.setRating(newRating);
		BeanHelper.onUpdate(updateHouse);
		houseMapper.updateHouse(updateHouse);
	}

	public void unBindUser2House(long houseId, Long userId, HouseUserType type) {
		if(type==HouseUserType.BOOKMARK) {
			houseMapper.deleteHouseUser(houseId,userId,type.value);
		}else {
			houseMapper.downHouse(houseId);
		}
		
	}

 
}
