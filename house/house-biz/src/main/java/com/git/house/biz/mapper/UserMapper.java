package com.git.house.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.git.house.common.model.User;

@Mapper
public interface UserMapper {

	public List<User> selectUsers();
	public int insert(User account);
	public List<User> selectUsersByQuery(User user);
	public void delete(String email);
	public void update(User updateUser);
	
	
}
