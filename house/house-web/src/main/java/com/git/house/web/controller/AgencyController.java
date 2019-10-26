package com.git.house.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.git.house.biz.service.AgencyService;
import com.git.house.biz.service.HouseService;
import com.git.house.biz.service.MailService;
import com.git.house.common.model.House;
import com.git.house.common.model.User;
import com.git.house.common.page.PageData;
import com.git.house.common.page.PageParams;

@Controller
public class AgencyController {

	@Autowired
	private AgencyService agencyService;
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping("/agency/agentList")
	public String agentList(Integer pageSize,Integer pageNum,ModelMap modelMap) {
		PageData<User> ps=agencyService.getAllAgent(PageParams.build(pageSize, pageNum));
		modelMap.put("ps", ps);
		return "/user/agent/agentList";
	}
	
	
	@RequestMapping("/agency/agentDetail")
	public String agentDetail(Long id,ModelMap modelMap) {
		User user=agencyService.getAgentDetail(id);
		House query=new House();
		query.setUserId(id);  
		//get houses by userId AND bookmarked is false
		//So in sql if userId is not null -> select from house_user and inner with house
		query.setBookmarked(false); 
		PageData<House> bindHouse=houseService.queryHouse(query,
					new PageParams(3,1));
		if(bindHouse!=null) {
			modelMap.put("bindHouse", bindHouse);
		}
		modelMap.put("agent", user);
		return "/user/agent/agentDetail";
	}
	
	
	
	
}
