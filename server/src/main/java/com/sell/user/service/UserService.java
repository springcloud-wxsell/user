package com.sell.user.service;

import com.sell.common.pojo.UserInfo;

/**
 * @author WangWei
 * @Title: UserService
 * @ProjectName user
 * @date 2018/12/18 11:26
 * @description:
 */
public interface UserService {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    UserInfo selectByOpenid(String openid);
}
