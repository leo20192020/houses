package com.git.house.web.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.git.house.common.model.User;

@Component
public class AuthenActionInterceptor implements HandlerInterceptor{

	/*
	 * for example: user try to visit profile page but have not login.
	 * AuthenInterceptor take care of get user from session.  
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user=UserContext.getUser();
		if(user==null) {
			String msg = URLEncoder.encode("please login in first");
			String target = URLEncoder.encode(request.getRequestURI().toString(), "utf-8");
			if("GET".equalsIgnoreCase(request.getMethod())) {
				response.sendRedirect("/account/signin?errorMsg="+msg+"&target="+target);
				return false;
			}else {
				//set up msg which will be catched by authenInterceptor and show to user
				response.sendRedirect("/accounts/signin?errorMsg="+msg);
				return false;
			}
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
		
	}

	
	
	
	
}
