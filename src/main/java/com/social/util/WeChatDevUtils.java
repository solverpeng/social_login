package com.social.util;

import net.sf.json.JSONObject;

import java.text.MessageFormat;

public class WeChatDevUtils {
	public static final String GET_QRCONNECT_URL = "https://open.weixin.qq.com/connect/qrconnect?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_login&state={2}#wechat_redirect";
	public static final String GET_OAUTH_USER_AGREE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state=1#wechat_redirect";
	public static final String GET_OAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
	public static final String GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}";
	public static final String CHECK_OAUTH_TOKEN_IS_VALID = "https://api.weixin.qq.com/sns/auth?access_token={0}&openid={1}";
	public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";

	public static final String SOCIAL_LOGIN_CLIENT_ID = AppConfig.getProperty(Constants.WeChat.SOCIAL_LOGIN_CLIENT_ID);
	public static final String SOCIAL_LOGIN_CLIENT_SECRET = AppConfig.getProperty(Constants.WeChat.SOCIAL_LOGIN_CLIENT_SERCRET);

	/**
	 * wechat login request code
	 */
	public static String getCodeUrl(String redirectUri, String state) throws Exception {
		return MessageFormat.format(GET_QRCONNECT_URL, SOCIAL_LOGIN_CLIENT_ID, redirectUri, state);
	}

	/**
	 * get authorization address - scope snsapi_userinfo | snsapi_base
	 */
	public static String getAuthorUrl(String redirectUri, String scope) throws Exception {
		return MessageFormat.format(GET_OAUTH_USER_AGREE_URL, SOCIAL_LOGIN_CLIENT_ID, java.net.URLEncoder.encode(redirectUri, "utf-8"), scope);
	}

	/**
	 * by code in exchange for web authorization - access_token
	 */
	public static JSONObject oauth(String code) throws Exception {
		String oauthUrl = MessageFormat.format(GET_OAUTH_URL, SOCIAL_LOGIN_CLIENT_ID, SOCIAL_LOGIN_CLIENT_SECRET, code);
		String response = HttpClientUtils.sendRequest(oauthUrl);
		return JSONObject.fromObject(response);
	}

	/**
	 * Gets the basic information for the login user
	 */
	public static WxUserInfo getUserInfoBySns(String accessToken, String openId) throws Exception {
		String getUserInfoUrl = MessageFormat.format(GET_USER_INFO, accessToken, openId);
		String response = HttpClientUtils.sendRequest(getUserInfoUrl);
		return (WxUserInfo) JSONObject.toBean(JSONObject.fromObject(response), WxUserInfo.class);
	}

	/**
	 * Check whether the authorization certificate is valid accessToken
	 */
	public static JSONObject checkTokenIsValid(String accessToken, String openId) throws Exception {
		String checkTokenUrl = MessageFormat.format(CHECK_OAUTH_TOKEN_IS_VALID, accessToken, openId);
		String response = HttpClientUtils.sendRequest(checkTokenUrl);
		return JSONObject.fromObject(response);
	}

	/**
	 * refresh access_token(if need) refreshToken
	 */
	public static JSONObject refreshToken(String refreshToken) throws Exception {
		String refreshTokenUrl = MessageFormat.format(REFRESH_TOKEN_URL, SOCIAL_LOGIN_CLIENT_ID, refreshToken);
		String response = HttpClientUtils.sendRequest(refreshTokenUrl);
		return JSONObject.fromObject(response);
	}
}

