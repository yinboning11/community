package com.boning.service;

import com.boning.entity.LoginTicket;
import com.boning.entity.User;

import java.util.Map;

public interface UserService {


    /**
     * 通过id查询用户
     *
     * @param id
     * @return
     */
    public User findUserById(int id);

    /**
     * 注册账号
     *
     * @param user
     * @return
     */
    public Map<String, Object> register(User user);

    /**
     * 激活账号
     *
     * @param userId
     * @param code
     * @return
     */
    public int activation(int userId, String code);

    /**
     * 登录校验
     *
     * @param username
     * @param password
     * @param expiredSeconds
     * @return
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 退出登录
     *
     * @param ticket
     */
    public void logout(String ticket);
    /**
     * 查询登录凭证
     *
     * @param ticket
     * @return
     */
    public  LoginTicket findLoginTicket(String ticket);


    /**
     * 更新头像
     *
     * @param userId
     * @param headerUrl
     * @return
     */
    public int updateHeaderUrl(int userId, String headerUrl);
}
