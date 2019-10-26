package com.git.house.biz.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.git.house.biz.mapper.UserMapper;
import com.git.house.common.model.User;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Value("${domain.name}")
	private String domainName;
	
	@Autowired
	private UserMapper userMapper;
	
	private final Cache<String,String> registerCache=
			CacheBuilder.newBuilder().maximumSize(100)
			.expireAfterAccess(15, TimeUnit.MINUTES)
			.removalListener(new RemovalListener<String,String>(){  
				//if expired, we should delete this user from database. User need to register again
				@Override  //if expired, what shoule we do
				public void onRemoval(RemovalNotification<String, String> notification) {
					String email=notification.getValue();
					User user=new User();
					user.setEmail(email);
					List<User> users=userMapper.selectUsersByQuery(user);
					if(!users.isEmpty()&&Objects.equal(users.get(0).getEnable(), 0)) {
						userMapper.delete(email);
					}
				}
				
			}).build();

		
	@Async
	public void sendMail(String title,String url,String email) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom(from);
		message.setSubject(title);
		message.setTo(email);
		message.setText(url);
		mailSender.send(message);
	}
	
	/*
	 *  Sending email to user is slow. we need to show register successfully to user first 
	 *  SO we use Async frame. we need to enable it at springbootApplictaion 
	 */
	@Async
	public void registerNofify(String email) {
		String randomKey=RandomStringUtils.randomAlphabetic(10);
		registerCache.put(randomKey, email);
		String url="http://"+domainName+"/accounts/verify?key="+randomKey;
		sendMail("Enabling your account in  House - git ",url,email);
	}

	
	/*
	 * check if registerCache has this key. 
	 * if yes, set it enable to 1 and update it in database and invalidate it in registerCache
	 */
	public boolean enableKey(String key) {
		String email=registerCache.getIfPresent(key);
		if(StringUtils.isBlank(email)) {
			return false;
		}
		User updateUser=new User();
		updateUser.setEmail(email);
		updateUser.setEnable(1);
		userMapper.update(updateUser);
		registerCache.invalidate(key);
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
