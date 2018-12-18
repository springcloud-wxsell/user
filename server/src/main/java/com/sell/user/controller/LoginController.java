package com.sell.user.controller;

import com.sell.common.constant.CookieConstant;
import com.sell.common.constant.RedisConstant;
import com.sell.common.enums.ResultEnum;
import com.sell.common.enums.RoleEnum;
import com.sell.common.pojo.UserInfo;
import com.sell.common.utils.CookieUtils;
import com.sell.common.utils.ResultVOUtils;
import com.sell.common.vo.ResultVO;
import com.sell.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWei
 * @Title: UserController
 * @ProjectName user
 * @date 2018/12/18 11:45
 * @description:
 */
@RequestMapping("/login")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 买家登录
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid,
                          HttpServletResponse response) {
        // 1. openid和数据库进行匹配
        UserInfo userInfo = userService.selectByOpenid(openid);
        if (null == userInfo) {
            return ResultVOUtils.error(ResultEnum.LOGIN_FAIL);
        }
        // 2. 判断角色
        if (!RoleEnum.BUYER.getCode().equals(userInfo.getRole().intValue())) {
            return ResultVOUtils.error(ResultEnum.ROLE_PERMISSION_ERROR);
        }
        // 3. cookie中设置openid=abc
        CookieUtils.set(response, CookieConstant.OPENID, openid, CookieConstant.expire);
        return ResultVOUtils.success();
    }

    @RequestMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        // 1. 根据openid查询用户
        UserInfo userInfo = userService.selectByOpenid(openid);
        if (null == userInfo) {
            return ResultVOUtils.error(ResultEnum.LOGIN_FAIL);
        }
        // 2. 判断角色
        if (!RoleEnum.SELLER.getCode().equals(userInfo.getRole().intValue())) {
            return ResultVOUtils.error(ResultEnum.ROLE_PERMISSION_ERROR);
        }
        // 判断用户是否登录
        Cookie cookie = CookieUtils.get(request, CookieConstant.TOKEN);
        if (cookie != null &&
                !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue())))) {
            return ResultVOUtils.success();
        }
        // 3. redis中设置key=UUID, value=xyz
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_TEMPLATE, token),
                openid, expire, TimeUnit.SECONDS);
        // 4. cookie中设置openid=xyz
        CookieUtils.set(response, CookieConstant.TOKEN, token, expire);
        return ResultVOUtils.success();
    }
}
