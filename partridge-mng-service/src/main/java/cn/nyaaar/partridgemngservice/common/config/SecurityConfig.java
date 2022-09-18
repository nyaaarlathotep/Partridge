package cn.nyaaar.partridgemngservice.common.config;

import cn.nyaaar.partridgemngservice.common.enums.PrivilegeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .antMatcher("/*")
////                .antMatcher("/swagger-ui/*")
//                .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
//                .formLogin()
//                .permitAll()
//                .and()
//                .csrf().disable()
//                .build();
        return http
                .authorizeRequests()
                .antMatchers("/user")
                .permitAll()
                .antMatchers("/*swagger*")
                .hasRole(PrivilegeEnum.ROOT.getCode())
                .anyRequest()
//                .permitAll()
                .hasRole(PrivilegeEnum.USER.getCode())
                .and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .build();
    }
}
