package com.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置类
 * 用于配置密码加密和验证
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * 配置PasswordEncoder，支持SHA-256和BCrypt两种加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
            
            @Override
            public String encode(CharSequence rawPassword) {
                // 使用BCrypt加密
                return bcryptEncoder.encode(rawPassword);
            }
            
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // 检查密码长度，SHA-256密码是64位字符
                if (encodedPassword.length() == 64 && !encodedPassword.startsWith("$2a$")) {
                    // 使用SHA-256验证，指定UTF-8编码
                    try {
                        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                        byte[] hash = md.digest(rawPassword.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        StringBuilder hexString = new StringBuilder();
                        for (byte b : hash) {
                            String hex = Integer.toHexString(0xff & b);
                            if (hex.length() == 1) {
                                hexString.append('0');
                            }
                            hexString.append(hex);
                        }
                        return hexString.toString().equals(encodedPassword);
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    // 使用BCrypt验证
                    return bcryptEncoder.matches(rawPassword, encodedPassword);
                }
            }
        };
    }
}