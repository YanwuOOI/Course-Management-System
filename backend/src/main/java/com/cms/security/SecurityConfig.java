package com.cms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用于配置JWT认证和权限控制
 * @author 许祖嘉
 * @date 2025-11-28
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护，因为使用JWT认证
            .csrf(csrf -> csrf.disable())
            
            // 显式配置CORS
            .cors(cors -> cors.configurationSource(request -> {
                org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
                // 允许所有源，不使用allowCredentials
                config.setAllowedOrigins(java.util.Collections.singletonList("*") );
                config.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.Arrays.asList("*"));
                config.setAllowCredentials(false);
                return config;
            }))
            
            // 设置会话管理为无状态，因为使用JWT认证
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求授权规则
            .authorizeHttpRequests(authorize -> authorize
                // 允许所有用户访问登录接口
            .requestMatchers("/auth/**").permitAll()
            
            // 允许所有用户访问公共接口
            .requestMatchers("/public/**").permitAll()
            
            // 允许所有用户访问注册接口
            .requestMatchers("/users").permitAll()
            
            // 允许学生角色访问学生相关接口
            .requestMatchers("/student/**").hasRole("学生")
            
            // 允许教师角色访问教师相关接口
            .requestMatchers("/teacher/**").hasRole("教师")
            
            // 允许管理员角色访问管理员相关接口
            .requestMatchers("/admin/**").hasRole("管理员")
            
            // 成绩相关接口权限配置
            .requestMatchers("/scores/student/**").hasRole("学生")
            .requestMatchers("/scores/teacher/**").hasRole("教师")
            .requestMatchers("/scores/course/**").hasRole("教师")
            .requestMatchers("/scores/batch").hasRole("教师")
            .requestMatchers("/scores/**").hasAnyRole("教师", "管理员")
            
            // 成绩异议相关接口权限配置
            .requestMatchers("/score-disputes/student/**").hasRole("学生")
            .requestMatchers("/score-disputes/course/**").hasRole("教师")
            .requestMatchers("/score-disputes/**").hasAnyRole("教师", "管理员")
            
            // 用户管理相关接口权限配置
            .requestMatchers("/users/profile").authenticated()
            .requestMatchers("/users/{userId}/profile").authenticated()
            .requestMatchers("/users/**").hasRole("管理员")
                
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            
            // 在UsernamePasswordAuthenticationFilter之前添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    // PasswordEncoder配置已移至com.cms.config.PasswordEncoderConfig类
    
    /**
     * 配置AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .and()
                .build();
    }
}