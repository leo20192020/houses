package com.git.house.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.git.house.biz.service.UserService;
import com.git.house.common.constants.CommonConstants;
import com.git.house.common.model.User;
import com.git.house.common.result.ResultMsg;
import com.git.house.common.utils.HashUtils;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/*
	 * Register
	 * 1,check if account is null
	 * 2,validate account and get resultMsg
	 * 3,process resultMsg 
	 */
	@RequestMapping("account/register")
	public String accountsRegister(User account,ModelMap modelMap){
		if(account==null||account.getName()==null) {
//			modelMap.put("agencyList", agencyService.getAllAgency());
			return "user/accounts/register";
		}
		
		ResultMsg resultMsg=UserHelper.validate(account);
		
		if(resultMsg.isSuccess()&&userService.addAccount(account)) {
			modelMap.put("email", account.getEmail());//registerSubmit.ftl shows success / sending  emial
			return "user/accounts/registerSubmit";
		}else {
			return "redirect:/accounts/register?"+resultMsg.asUrlParams(); 
				
		}
				
	}
	
	
		@RequestMapping("/account/verify")
		public String verify(String key) {
			boolean result=userService.enableKey(key);
			if(result) {
				return "redirect:/index?"+ResultMsg.successMsg("successfully active").asUrlParams();
			}else {
				return "redirect:/accounts/register?"
						+ResultMsg.errorMsg("fail to active, please check if link has been out of date")
						.asUrlParams();
			}
		}
	
		
		@RequestMapping("/accounts/signin")
		public String signin(HttpServletRequest req) {
			String username=req.getParameter("username");
			String password=req.getParameter("password");
			//if session is valid, Interceptor will pass and will NOT access here. So user session is invalid now. 
			//user try to visit profile page. Interceptor intercept it and can get target as param. 
			String target = req.getParameter("target");
			if(username==null||password==null) {
				req.setAttribute("target", target); //always take target param
				return "user/accounts/signin";
			}
			
			//password and username is not null
			User user=userService.auth(username,password);
			if(user==null) { //failure to login in as wrong username or password
				return "redirect:/accounts/signin?"+"target="+target
				+"&username="+username
				+"&"+ResultMsg.errorMsg("wrong username or passwprd").asUrlParams();
			}else {
				HttpSession session = req.getSession(true);
				session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
				return StringUtils.isNoneBlank(target)?"redirect:"+target:"redirect/index";
			}
			
		}
	
	
	
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return "redirect:/index";
		
	}
	
	@RequestMapping("accounts/profile")
	public String profile(HttpServletRequest req,User updateUser,ModelMap model) {
		if(updateUser.getEmail()==null) {
			return "/user/accounts/profile";
		}
		userService.updateUser(updateUser,updateUser.getEmail());
		//we need to update info of user
		User query=new User();
		query.setEmail(updateUser.getEmail());
		List<User> list = userService.getUserByQuery(query);
		req.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, list.get(0));
		return "redirect:/accounts/profile?"+ResultMsg.successMsg("successfully update").asUrlParams();
	}
	
	
	public String changePassword(String email, String password,String newPassword,
			String confirmPassword,ModelMap model) {
		User user = userService.auth(email, password);
		if(user==null&&!confirmPassword.equals(newPassword)) {
			return "redirect:/accounts/profile?"+ResultMsg.errorMsg("wrong password ! ").asUrlParams();
		}
		User updateUser=new User();
		updateUser.setPasswd(HashUtils.encryPassword(newPassword));
		userService.updateUser(updateUser, email);
		
		return "redirect:/accounts/profile?"+ResultMsg.successMsg("password has been updated successfuly").asUrlParams();
	}
	
	
	
}
