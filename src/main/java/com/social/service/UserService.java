package com.social.service;

import com.social.domain.User;
import com.social.domain.vo.UserVO;
import com.social.util.Constants.OpenIdType;

public interface UserService {
	User getUserById(int userId);
	
	UserVO getUserByOpenId(String openId, OpenIdType openIdType);
	
	int insertSelective(User record);
	
}
