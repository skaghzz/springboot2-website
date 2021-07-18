package com.skagh.springboot2website.config.auth;

import com.skagh.springboot2website.domain.user.Role;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity  // spring security 설정들을 활성화시킨다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()   // h2-console 화면을 사용하기 위해 해당 옵션들을 disable함
            .headers().frameOptions().disable()
        .and()
            .authorizeRequests()    // URL 별 권한 관리를 설정하는 옵션의 시작점
            .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
            .antMatchers("/api/v1/**").hasRole(Role.USER.name())
            .anyRequest().authenticated()   // 설정된 값들 이외 나머지 URL은 모두 인증된 사용자들에게만 허용한다.
        .and()
            .logout()   // 로그아웃 기능에 대한 여러 설정의 진입점
                .logoutSuccessUrl("/")  // 로그아웃 성공 시 / 주소로 이동
        .and()
            .oauth2Login()  // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                .userInfoEndpoint() // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                    .userService(customOAuth2UserService);  // 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록. 
                                                            // 사용자 정보를 가져온 이후에 진행하고자 하는 기능을 명시할 수 있다.
    }
}
