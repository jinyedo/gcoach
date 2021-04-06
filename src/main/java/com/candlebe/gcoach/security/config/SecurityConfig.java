package com.candlebe.gcoach.security.config;

import com.candlebe.gcoach.security.handler.LoginSuccessHandler;
import com.candlebe.gcoach.security.service.MemberUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberUserDetailsService memberUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 일반 로그인
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login") // 인가/인증에 문제시 로그인 화면으로 이동
                .defaultSuccessUrl("/test", true);

        // 소셜 로그인
        http.oauth2Login()// 로그인 시에 OAuth 를 사용한 로그인이 가능하도록
                .loginPage("/login")
                .failureUrl("/login") // 인가/인증에 문제시 로그인 화면으로 이동
                .defaultSuccessUrl("/test", true);

        http.csrf().disable(); // CSRF 토큰을 발해하지 않도록 지정
        http.logout(); // 로그아웃 설정

        // 자동 로그인 설정
        http.rememberMe()
                .key("autoLoginToken")
                .tokenValiditySeconds(60*60*7).userDetailsService(memberUserDetailsService) // 7일
                .rememberMeParameter("auto-login")
                .rememberMeCookieName("remember-me");
    }
}
