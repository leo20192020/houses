package com.git.house.web.controller;


import com.git.house.common.model.User;
import com.git.house.common.result.ResultMsg;
import com.mysql.jdbc.StringUtils;

public class UserHelper {

	public static ResultMsg validate(User account) {
		if(StringUtils.isNullOrEmpty(account.getEmail())) {
			return ResultMsg.errorMsg("Wrong email address");
		}
		if(StringUtils.isNullOrEmpty(account.getName())) {
			return ResultMsg.errorMsg("Wrong username");
		}
		if(StringUtils.isNullOrEmpty(account.getConfirmPasswd())||
			StringUtils.isNullOrEmpty(account.getPasswd())||
			!account.getPasswd().equals(account.getConfirmPasswd())){
			return ResultMsg.errorMsg("wrong password");
		}
		if(account.getPasswd().length()<6){
			return ResultMsg.errorMsg("length of password need to larger than 6");
		}
		
		return ResultMsg.successMsg("");
	}

	
	
	
}
