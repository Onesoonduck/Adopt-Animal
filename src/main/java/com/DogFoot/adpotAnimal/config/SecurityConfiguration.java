package com.DogFoot.adpotAnimal.config;

import com.DogFoot.adpotAnimal.jwt.JwtAuthenticationFilter;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf, http basic 비활성화
        http.csrf((auth) -> auth.disable());
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 사용 안함
        http.setSharedObject(SessionManagementConfigurer.class, new SessionManagementConfigurer<HttpSecurity>().sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .authorizeHttpRequests((auth)->auth
                .requestMatchers("members/sign-up","/","members/sign-in").permitAll()  // 홈, 로그인, 가입 페이지는 전체 허가
                .requestMatchers("/admin").hasRole("ADMIN")   // 관리자 페이지는 관리자만
                .anyRequest().authenticated()
            );

        // jwt 인증 필터 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}