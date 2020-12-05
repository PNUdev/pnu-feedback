package com.pnu.dev.pnufeedback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}
