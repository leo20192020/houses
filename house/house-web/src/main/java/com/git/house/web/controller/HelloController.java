package com.git.house.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.git.house.biz.service.UserService;
import com.git.house.common.model.User;

@Controller
public class HelloController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("hello")
	public String hello(ModelMap modelMap) {
		List<User> users=userService.getUsers();
		User user=users.get(0);
		modelMap.put("user",user);
		return "hello";
	}
	
//	最初开发时使用
//	@RequestMapping("index")
//	public String index() {
//		return "homepage/index";
//	}
//	
}
