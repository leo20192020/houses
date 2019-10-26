package com.git.house.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.git.house.biz.mapper.UserMapper;
import com.git.house.common.model.User;
import com.git.house.common.utils.BeanHelper;
import com.git.house.common.utils.HashUtils;
import com.google.common.collect.Lists;

@Service
public class UserService {
	
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private MailService mailService;
	
	@Value("${file.prefix}")
	private String imgPrefix;
	
	/*
	 * add User to database 
	 * 1,user is NOT activate, password need to be encoded and pics need to store pics server. 
	 * 2,generate key and bind with email. sending link to user's email. 
	 */
	@Transactional(rollbackFor=Exception.class)
	public boolean addAccount(User account) {
		account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
		List<String> imgList = fileService.getImgPath(Lists.newArrayList(account.getAvatarFile()));
		if(!imgList.isEmpty()) {
			account.setAvatar(imgList.get(0));
		}
		BeanHelper.setDefaultProp(account, User.class);
		account.setEnable(0);
		userMapper.insert(account);
		mailService.registerNofify(account.getEmail());
		return true;
	}

	
	
	
	
	public List<User> getUsers(){
//		System.out.print("111111111111");
		return userMapper.selectUsers();
	}





	public boolean enableKey(String key) {
		return mailService.enableKey(key);
	}


	/*
	 * we need to return user's avatar abs address if successfully login 	
	 */
	public User auth(String username, String password) {
		User user=new User();
		user.setEmail(username);
		user.setPasswd(HashUtils.encryPassword(password));
		//we just need to get user ONLY IF it's enable is 1
		List<User> list=getUserByQuery(user);
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}





	public List<User> getUserByQuery(User user) {
		List<User> list = userMapper.selectUsersByQuery(user);
		//we need to return abs path to frontend because need to show on website
		list.forEach(u->{
			u.setAvatar(imgPrefix+u.getAvatar());
		});
		
		return list;
	}





	public void updateUser(User updateUser, String email) {
		updateUser.setEmail(email);
		BeanHelper.onUpdate(updateUser);
		userMapper.update(updateUser);
		
	}
}
