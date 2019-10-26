package com.git.house.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.git.house.biz.service.AgencyService;
import com.git.house.biz.service.CityService;
import com.git.house.biz.service.HouseService;
import com.git.house.biz.service.RecommendService;
import com.git.house.common.constants.CommonConstants;
import com.git.house.common.constants.HouseUserType;
import com.git.house.common.model.House;
import com.git.house.common.model.HouseUser;
import com.git.house.common.model.User;
import com.git.house.common.model.UserMsg;
import com.git.house.common.page.PageData;
import com.git.house.common.page.PageParams;
import com.git.house.common.result.ResultMsg;
import com.git.house.web.interceptor.UserContext;

@Controller
public class HouseController {

	
	@Autowired
	private HouseService houseService;

	@Autowired
	private RecommendService recommendService;
	
	/*
	 * 1， page function
	 * 2, house search  as well as type search like sale or rent
	 * 3,sort results
	 * 4,show picts price and title ....
	 */
	@RequestMapping("house/list")
	public String getHouseList(Integer pageSize,Integer pageNum,
			House query,ModelMap model) {
		PageData<House> ps=houseService.queryHouse(query,PageParams.build(pageSize, pageNum));
		List<House> hotHouses=recommendService.getHotHouse(CommonConstants.RECOMMEND_SIZE);
		model.put("recomHouse", hotHouses);
		model.put("ps", ps);
		model.put("vo", query);
		return "house/listing";
	}
	
	
	
	@Autowired
	private AgencyService agencyService;
	
	@RequestMapping("house/detail")
	public String houseDetail(Long id,ModelMap modelMap) {
		House house=houseService.queryOneHouse(id);
		HouseUser houseUser=houseService.getHouseUser(id);
		recommendService.increase(id);
		if(houseUser.getUserId()!=null&&!houseUser.getUserId().equals(0)) {
			modelMap.put("agent", agencyService.getAgentDetail(houseUser.getUserId()));
		}
		List<House> hotHouses=recommendService.getHotHouse(CommonConstants.RECOMMEND_SIZE);
		modelMap.put("recomHouse", hotHouses);
		modelMap.put("house", house);
		return "/house/detail";
	}
	
	@RequestMapping("house/leaveMsg")
	public String houseMsg(UserMsg userMsg) {
		houseService.addUserMsg(userMsg);
		//We are requesting house/details and past params is meaningless so redirect
		return "redirect:/house/detail?id="+userMsg.getHouseId()+
				ResultMsg.successMsg("Message sent successfully").asUrlParams();
	}
	
	
	//-------add new house to the system----------
	@Autowired
	private CityService cityService;
	
	@RequestMapping("/house/toAdd")
	public String toAdd(ModelMap modelMap) {
		modelMap.put("citys", cityService.getAllCitys());
		modelMap.put("communitys", houseService.getAllCommunities());
		return "house/add";
	}
	
	
	@RequestMapping("/house/add")
	public String doAdd(House house) {
		User user=UserContext.getUser();
		house.setState(CommonConstants.HOUSE_STATE_UP);
		houseService.addHouse(house,user);
		return "house/add";
	}
	
	
	@RequestMapping("house/ownList")
	public String getOwnList(Integer pageNum,Integer pageSize,ModelMap modelMap) {
		User user=UserContext.getUser();
		House house=new House();
		house.setUserId(user.getId());
		house.setBookmarked(false);
		modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
		return "/house/ownList";
	}
	
	//-------------bookmarked and rating---------------
	@ResponseBody
	@RequestMapping("/house/rating")
	public ResultMsg houseRating(Double rating, Long id) {
		houseService.updateRating(id,rating);
		return ResultMsg.successMsg("ok");
	}

	
	@ResponseBody
	@RequestMapping("/house/bookmark")
	public ResultMsg bookmark(Long id) {
		User user=UserContext.getUser();
		houseService.bindUser2House(id,user.getId(),true);
		return ResultMsg.successMsg("ok");
	}
	
	
	@ResponseBody
	@RequestMapping("/house/unbookmark")
	public ResultMsg unbookmark(long id) {
		User user = UserContext.getUser();
		houseService.unBindUser2House(id,user.getId(),HouseUserType.BOOKMARK);
		return ResultMsg.successMsg("ok");
	}
	
	@RequestMapping("/house/bookmarked")
	public String bookemarked(House house,Integer pageNum, Integer pageSize, ModelMap modelMap) {
		User user=UserContext.getUser();
		house.setBookmarked(true);
		house.setUserId(user.getId());
		modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
		modelMap.put("pageType", "book");
		return "/house/ownList";
	}
	
	
	
	@RequestMapping(value="house/del")
	public String delsale(Long id,String pageType) {
		User user=UserContext.getUser();
		houseService.unBindUser2House(id, user.getId(), pageType.equals("own")?
				HouseUserType.SALE:HouseUserType.BOOKMARK);
		
		return "/redirect:house/ownList";
		
	}
}































