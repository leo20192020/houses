package com.git.house.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConf extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthenInterceptor  authenInterceptor;
	
	@Autowired
	private AuthenActionInterceptor authenActionInterceptor;
	
	public void addInterceotors(InterceptorRegistry registry) {
		registry.addInterceptor(authenInterceptor)
		.excludePathPatterns("/static")
		.addPathPatterns("/**");
		
		
		registry.addInterceptor(authenActionInterceptor)
		.addPathPatterns("/house/toAdd")  
		.addPathPatterns("/accounts/profile").
		addPathPatterns("/accounts/profileSubmit")
		.addPathPatterns("/house/bookmarked")
		.addPathPatterns("/house/del")
		.addPathPatterns("/house/ownlist")
		.addPathPatterns("/house/add")
        .addPathPatterns("/house/toAdd")
		.addPathPatterns("/agency/agentMsg")
		.addPathPatterns("/comment/leaveComment")
		.addPathPatterns("/comment/leaveBlogComment");
	
		super.addInterceptors(registry);
	}
	
}
