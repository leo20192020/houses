package com.git.house.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.git.house.common.model.Agency;
import com.git.house.common.model.User;
import com.git.house.common.page.PageParams;

@Mapper
public interface AgencyMapper {

	public List<User> selectAgent(@Param("user")User user, @Param("pageParams")PageParams pageParams);

	public List<Agency> select(Agency agency);

	public Long selectAgentCount(User user);

 }
