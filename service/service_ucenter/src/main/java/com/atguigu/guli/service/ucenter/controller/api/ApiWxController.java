package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.JwtInfo;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.base.exception.GuLiExcpetion;
import com.atguigu.guli.service.ucenter.Config.HttpClientUtils;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.utils.UcenterProperties;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Api(tags = "微信支付")
//@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Resource
    private UcenterProperties ucenterProperties;
    @Resource
    private MemberService memberService;

    @GetMapping("login")
    public String genQrConnect(HttpSession session){

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //处理回调url
        String redirecturi = "";
        try {
            redirecturi = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuLiExcpetion(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state：生成随机数，存入session
        String state = UUID.randomUUID().toString();
        log.info("生成 state = " + state);
        session.setAttribute("wx_open_state", state);

        String qrcodeUrl = String.format(
                baseUrl,
                ucenterProperties.getAppId(),
                redirecturi,
                state
        );

        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session){
        //回调被拉起，并获得code和state参数
        log.info("callback被调用");
        log.info("code = " + code);
        log.info("state = " + state);

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
            log.error("非法回调请求");
            throw new GuLiExcpetion(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        String  sessionstate = (String) session.getAttribute("wx_open_state");
        if (!state.equals(sessionstate)){
            log.error("非法回调请求");
            throw new GuLiExcpetion(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //携带授权临时票据code，和appid以及appsecret请求access_token
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> accessTokenParam = new HashMap();
        accessTokenParam.put("appid", ucenterProperties.getAppId());
        accessTokenParam.put("secret", ucenterProperties.getAppSecret());
        accessTokenParam.put("code", code);
        accessTokenParam.put("grant_type", "authorization_code");
        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);

        String result = "";
        try {
            client.get();
            result =  client.getContent();
            System.out.println("result = " + result);
        }  catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuLiExcpetion(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String ,Object> resultMap = gson.fromJson(result, HashMap.class);

        //判断微信获取access_token失败的响应
        Object errcodeObj = resultMap.get("errcode");
        if(errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            Double errcode = (Double)errcodeObj;
            log.error("获取access_token失败 - " + "message: " + errmsg + ", errcode: " + errcode);
            throw new GuLiExcpetion(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //微信获取access_token响应成功
        String accessToken = (String)resultMap.get("access_token");
        String openid = (String)resultMap.get("openid");

        log.info("accessToken = " + accessToken);
        log.info("openid = " + openid);

        //根据access_token获取微信用户的基本信息
        // 查看当前用户是否已经注册
        Member member = memberService.getByOpenid(openid);
       if (member == null) {
           // 想微信服务器端发起请求
           String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
           Map<String, String> baseUserInfoParam = new HashMap();
           baseUserInfoParam.put("access_token", accessToken);
           baseUserInfoParam.put("openid", openid);
           client = new HttpClientUtils(baseUserInfoUrl, baseUserInfoParam);

           String resultUserInfo = null;
           try {
               client.get();
               resultUserInfo = client.getContent();
           } catch (Exception e) {
               log.error(ExceptionUtils.getMessage(e));
               throw new GuLiExcpetion(ResultCodeEnum.FETCH_USERINFO_ERROR);
           }

           HashMap<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
           if (resultUserInfoMap.get("errcode") != null) {
               log.error("获取用户信息失败" + "，message：" + resultMap.get("errmsg"));
               throw new GuLiExcpetion(ResultCodeEnum.FETCH_USERINFO_ERROR);
           }

           String nickname = (String) resultUserInfoMap.get("nickname");
           String headimgurl = (String) resultUserInfoMap.get("headimgurl");
           Double sex = (Double) resultUserInfoMap.get("sex");

           //用户注册
           member = new Member();
           member.setOpenid(openid);
           member.setNickname(nickname);
           member.setAvatar(headimgurl);
           member.setSex(sex.intValue());
           memberService.save(member);

       }
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);

        //携带token跳转
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }
}