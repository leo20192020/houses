package com.git.house.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.git.house.biz.mapper.AgencyMapper;
import com.git.house.common.model.Agency;
import com.git.house.common.model.User;
import com.git.house.common.page.PageData;
import com.git.house.common.page.PageParams;

@Service
public class AgencyService {

	@Autowired
	private AgencyMapper agencyMapper;
	
	@Value("${file.prefix}")
	private String imgPrefix;
	
	/*
	 * get User by userId and set up its avatar which needs to be showed on page
	 * Also, need to show agency name. PS: in user table, we just have agency_id
	 */
	public User getAgentDetail(Long userId) {
		User user=new User();
		user.setId(userId);
		user.setType(2);
		List<User> list=agencyMapper.selectAgent(user,PageParams.build(1, 1));
		setImg(list);
		if(!list.isEmpty()) {
			User agent=list.get(0);
			Agency agency=new Agency();
			agency.setId(agent.getAgencyId().intValue());
			List<Agency> agencies=agencyMapper.select(agency);
			if(!agencies.isEmpty()) {
				agent.setAgencyName(agencies.get(0).getName());
			}
			return agent;
		}
		return null;
	}

	private void setImg(List<User> list) {
		// TODO Auto-generated method stub
		list.forEach(user->{
			user.setAvatar(imgPrefix+user.getAvatar());
		});
	}

	public PageData<User> getAllAgent(PageParams pageParams) {
		List<User> agents=agencyMapper.selectAgent(new User(), pageParams);
		setImg(agents);
		Long count=agencyMapper.selectAgentCount(new User());
		return PageData.buildPage(agents, count, pageParams.getPageSize(), pageParams.getPageNum());
	}
	
	
	
}
