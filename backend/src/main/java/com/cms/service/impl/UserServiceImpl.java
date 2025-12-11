package com.cms.service.impl;

import com.cms.entity.User;
import com.cms.dao.UserMapper;
import com.cms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户Service实现类
 * 实现用户管理的业务逻辑
 * @author 严翊航
 * @since 2025-11-30
 * @module 用户管理模块
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userMapper.selectUserByUserId(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        logger.info("添加用户，用户名：{}", user.getUsername());

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 自动生成用户ID
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            String prefix;
            switch (user.getRole()) {
                case "管理员":
                    prefix = "A";
                    break;
                case "教师":
                    prefix = "T";
                    break;
                case "学生":
                    prefix = "S";
                    break;
                default:
                    prefix = "U";
                    break;
            }
            // 获取当前时间戳的后6位作为用户ID的一部分
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            // 生成用户ID：前缀 + 时间戳后6位 + 随机3位数字
            String userId = prefix + timestamp + String.format("%03d", (int) (Math.random() * 1000));
            user.setUserId(userId);
        }
        
        // 设置默认值
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getUsername()); // 使用用户名作为默认姓名
        }
        if (user.getDept() == null || user.getDept().isEmpty()) {
            user.setDept("未知院系"); // 默认院系
        }
        
        user.setCreateTime(new Date());
        user.setStatus(1); // 默认启用
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);
        logger.info("用户添加成功，用户ID：{}", user.getUserId());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        logger.info("更新用户，用户ID：{}", user.getUserId());
        // 获取原用户信息，确保存在
        User originalUser = userMapper.selectUserByUserId(user.getUserId());
        Objects.requireNonNull(originalUser, "用户不存在");

        String incomingPassword = user.getPassword();
        if (incomingPassword == null || incomingPassword.isBlank()) {
            // 未传递密码则保持原值
            user.setPassword(originalUser.getPassword());
        } else if (!passwordEncoder.matches(incomingPassword, originalUser.getPassword())) {
            // 仅当新密码与旧密码不匹配时才重新加密
            user.setPassword(passwordEncoder.encode(incomingPassword));
            logger.info("用户密码已更新，用户ID：{}", user.getUserId());
        } else {
            // 新密码与旧密码一致，避免重复加密
            user.setPassword(originalUser.getPassword());
        }

        userMapper.updateUser(user);
        User updatedUser = userMapper.selectUserByUserId(user.getUserId());
        logger.info("用户更新成功，用户ID：{}", updatedUser.getUserId());
        return updatedUser;
    }

    @Override
    @Transactional
    public boolean deleteUser(String userId) {
        logger.info("删除用户，用户ID：{}", userId);
        boolean result = userMapper.deleteUser(userId) > 0;
        if (result) {
            logger.info("用户删除成功，用户ID：{}", userId);
        } else {
            logger.warn("用户删除失败，用户ID：{}", userId);
        }
        return result;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userMapper.selectUsersByRole(role);
    }

    @Override
    public List<User> getUsersByDept(String dept) {
        return userMapper.selectUsersByDept(dept);
    }

    @Override
    @Transactional
    public boolean disableUser(String userId) {
        logger.info("禁用用户，用户ID：{}", userId);
        User user = userMapper.selectUserByUserId(userId);
        if (user != null) {
            user.setStatus(0);
            boolean result = userMapper.updateUser(user) > 0;
            if (result) {
                logger.info("用户禁用成功，用户ID：{}", userId);
            } else {
                logger.warn("用户禁用失败，用户ID：{}", userId);
            }
            return result;
        }
        logger.warn("用户不存在，无法禁用，用户ID：{}", userId);
        return false;
    }

    @Override
    @Transactional
    public boolean enableUser(String userId) {
        logger.info("启用用户，用户ID：{}", userId);
        User user = userMapper.selectUserByUserId(userId);
        if (user != null) {
            user.setStatus(1);
            boolean result = userMapper.updateUser(user) > 0;
            if (result) {
                logger.info("用户启用成功，用户ID：{}", userId);
            } else {
                logger.warn("用户启用失败，用户ID：{}", userId);
            }
            return result;
        }
        logger.warn("用户不存在，无法启用，用户ID：{}", userId);
        return false;
    }
}