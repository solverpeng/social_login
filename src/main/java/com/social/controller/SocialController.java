package com.social.controller;

import com.qq.connect.QQConnectException;
import com.social.util.AppConfig;
import com.social.util.Constants;
import com.social.util.WeChatDevUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import weibo4j.Oauth;
import weibo4j.model.WeiboException;
import weibo4j.util.WeiboConfig;

import java.io.IOException;

@Controller
@RequestMapping("/social")
public class SocialController extends BaseController {
    private static Logger log = Logger.getLogger(SocialController.class);

    @RequestMapping("/index")
    public String index() {
        return "social/index";
    }

    @RequestMapping("/weibo")
    public void weiBo() throws WeiboException, IOException {
        updateProperties();
        Oauth oauth = new Oauth();
        String url = oauth.authorize("code", "");
        log.info("SocialController#weiBo -> url：" + url);
        getResponse().sendRedirect(url);
    }

    @RequestMapping("/wechat")
    public void weChat() throws Exception {
        String state = getUUID();
        getRequest().getSession().setAttribute("state", state);
        String codeUrl = WeChatDevUtils.getCodeUrl(AppConfig.getProperty(Constants.WeChat.SOCIAL_LOGIN_REDIRECT_URI), state);
        log.info("SocialController#weChat -> getCodeUrl：" + codeUrl);
        getResponse().sendRedirect(codeUrl);
    }

    @RequestMapping("/mobile/wechat")
    public void mobileWeChat() throws Exception {
        getRequest().getSession().setAttribute("state", Constants.OpenIdType.WECHAT);
        String redirectUrl = AppConfig.getProperty(Constants.WeChat.SOCIAL_LOGIN_REDIRECT_URI);
        log.info("SocialController#mobileWeChat -> redirectUrl：" + redirectUrl);
        String url = WeChatDevUtils.getAuthorUrl(redirectUrl, "snsapi_userinfo");
        log.info("SocialController#mobileWeChat -> url：" + url);
        getResponse().sendRedirect(url);
    }

    @RequestMapping("/qq")
    public void qq() throws IOException, QQConnectException {
        getResponse().setContentType("text/html;charset=utf-8");
        String authorizeURL = new com.qq.connect.oauth.Oauth().getAuthorizeURL(getRequest());
        log.info("SocialController#qq -> authorizeURL：" + authorizeURL);
        getResponse().sendRedirect(authorizeURL);
    }

    @RequestMapping("/faceBook")
    public String faceBook() {
        return "";
    }

    private void updateProperties() {
        WeiboConfig.updateProperties("client_ID", AppConfig.getProperty(Constants.WEIBO.SOCIAL_LOGIN_CLIENT_ID));
        WeiboConfig.updateProperties("client_SERCRET", AppConfig.getProperty(Constants.WEIBO.SOCIAL_LOGIN_CLIENT_SERCRET));
        WeiboConfig.updateProperties("redirect_URI", AppConfig.getProperty(Constants.WEIBO.SOCIAL_LOGIN_REDIRECT_URI));
    }

}
