package com.woorifisa.wl.config;

import com.woorifisa.wl.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login/**", "/oauth2/**").permitAll() // 공개 URL
//                        .requestMatchers("/**").permitAll() // 공개 URL
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 커스텀 로그인 페이지
                        .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 시 처리
                            // OAuth2AuthenticationException 메시지 처리
                            HttpSession session = request.getSession(false);
                            if (session != null && session.getAttribute("error_message") == null) {
                                session.setAttribute("error_message", exception.getMessage());
                            }
                            response.sendRedirect("/login?error=true");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 사용자 정보 처리 서비스
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 페이지
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                );
        return http.build();
    }

}
