package com.git.house.web.interceptor;

import com.git.house.common.model.User;

public class UserContext {

	private static final ThreadLocal<User> USER_HOLDER=new ThreadLocal();
	
	public static void setUser(User user) {
		USER_HOLDER.set(user);
	}
	
	public static void remove() {
		USER_HOLDER.remove();
	}
	
	public static User getUser() {
		return USER_HOLDER.get();
	}
	
}
