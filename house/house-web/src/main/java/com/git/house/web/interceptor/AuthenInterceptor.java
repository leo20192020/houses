package com.git.house.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.git.house.common.constants.CommonConstants;
import com.git.house.common.model.User;
import com.google.common.base.Joiner;

@Component
public class AuthenInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Map<String, String[]> map = request.getParameterMap();
		//user try to visit profile page. get refused due to Not login.redirect to login page. 
		//it is going to here again. set up errorMsg info and show it on login page. 
		map.forEach((k,v)->{
			if(k.equals("errorMsg")||k.equals("successMsg")||k.equals("target")) {
				request.setAttribute(k, Joiner.on(",").join(v));
			}
		});
		
		String reqUri = request.getRequestURI();
		if(reqUri.startsWith("/static")||reqUri.startsWith("/error")) {
			return true;  //pass the request if error or static
		}
		//if session contains user info. we set it into localThread which can get it again.
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute(CommonConstants.USER_ATTRIBUTE);
		if(user!=null) {
			UserContext.setUser(user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		UserContext.remove();
	}

	
	
	
}
