package com.boning.service.Impl;

import com.boning.dao.LoginTicketMapper;
import com.boning.dao.UserMapper;
import com.boning.entity.LoginTicket;
import com.boning.entity.User;
import com.boning.service.UserService;
import com.boning.utils.CommunityConstant;
import com.boning.utils.CommunityUtil;
import com.boning.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    //模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }


    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();


        //空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }


        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        //邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }


        // 设置注册用户信息
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));// 设置salt
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);// 普通用户
        user.setStatus(0);//未激活
        user.setActivationCode(CommunityUtil.generateUUID());//激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        //System.out.println(user);
        userMapper.insertUser(user);


        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }


    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(username)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        //验证账号,使用用户名登录
        User user = userMapper.selectByName(username);
        if (user == null) {
            // 使用email作为账户登录
            user = userMapper.selectByEmail(username);
            if (user == null) {
                map.put("usernameMsg", "该账号不存在");
                return map;
            }
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        loginTicketMapper.insertLoginTicket(loginTicket);
//        // 获取loginTicket的RedisKey
//        String loginTicketKey = RedisKeyUtil.getLoginTicketKey(loginTicket.getTicket());
//        redisTemplate.opsForValue().set(loginTicketKey, loginTicket);
        map.put("ticket", loginTicket.getTicket());


        return map;
    }

    /**
     * 退出登录
     *
     * @param ticket
     */
    public void logout(String ticket) {
//        String loginTicketKey = RedisKeyUtil.getLoginTicketKey(ticket);
//        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(loginTicketKey);
//        loginTicket.setStatus(1);
//        redisTemplate.opsForValue().set(loginTicketKey, loginTicket);
        loginTicketMapper.updateStatus(ticket, 1);
    }

    /**
     * 查询登录凭证
     *
     * @param ticket
     * @return
     */
    @Override
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectLoginTicket(ticket);
    }



    /**
     * 更新头像
     *
     * @param userId
     * @param headerUrl
     * @return
     */
    public int updateHeaderUrl(int userId, String headerUrl) {
        int rows = userMapper.updateHeader(userId, headerUrl);
        // 更新头像，数据发生改变
//        clearUserCache(userId);
        return rows;
    }
}
