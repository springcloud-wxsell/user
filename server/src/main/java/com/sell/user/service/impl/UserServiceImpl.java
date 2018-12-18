package com.sell.user.service.impl;

import com.sell.common.pojo.UserInfo;
import com.sell.common.pojo.UserInfoExample;
import com.sell.user.mapper.UserInfoMapper;
import com.sell.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author WangWei
 * @Title: UserServiceImpl
 * @ProjectName user
 * @date 2018/12/18 11:27
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo selectByOpenid(String openid) {
        UserInfoExample example = new UserInfoExample();
        example.createCriteria().andOpenidEqualTo(openid);
        List<UserInfo> userInfoList = userInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userInfoList)) {
            return userInfoList.get(0);
        }
        return null;
    }
}
