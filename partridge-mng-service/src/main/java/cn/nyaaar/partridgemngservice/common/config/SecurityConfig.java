package cn.nyaaar.partridgemngservice.common.config;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.enums.PrivilegeEnum;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import cn.nyaaar.partridgemngservice.service.user.impl.CustomizeAuthenticationFailureHandler;
import cn.nyaaar.partridgemngservice.service.user.impl.CustomizeAuthenticationSuccessHandler;
import cn.nyaaar.partridgemngservice.service.user.impl.CustomizeSessionInformationExpiredStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@Slf4j
public class SecurityConfig {

    private final CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    private final CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomizeAuthenticationFailureHandler authenticationFailureHandler;

    public SecurityConfig(CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy,
                          CustomizeAuthenticationSuccessHandler authenticationSuccessHandler,
                          CustomizeAuthenticationFailureHandler authenticationFailureHandler) {
        this.sessionInformationExpiredStrategy = sessionInformationExpiredStrategy;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/user/**")
                .permitAll()
                .antMatchers("/swagger-ui/**")
                .hasRole(PrivilegeEnum.ROOT.getCode())
                .anyRequest()
                .hasAnyRole(PrivilegeEnum.USER.getCode(), PrivilegeEnum.ROOT.getCode())
                .and()
                .logout().
                permitAll().//允许所有用户
                        logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        log.info("[{}] logout, time:{}", ((UserDetails) authentication.getPrincipal()).getUsername(),
                                DateUtil.date());
                    }
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(new BaseResponse()));
                }).//登出成功处理逻辑
                        deleteCookies("JSESSIONID")//登出之后删除cookie
                //登入
                .and().formLogin().
                permitAll().//允许所有用户
                        successHandler(authenticationSuccessHandler).//登录成功处理逻辑
                        failureHandler(authenticationFailureHandler)//登录失败处理逻辑
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredSessionStrategy(sessionInformationExpiredStrategy))
                .build();
    }
}
