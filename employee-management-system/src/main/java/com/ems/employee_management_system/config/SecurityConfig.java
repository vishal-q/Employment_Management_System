package com.ems.employee_management_system.config;

import com.ems.employee_management_system.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.ems.employee_management_system.config.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(customUserDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Public
                .requestMatchers("/", "/login", "/register",
                                 "/css/**", "/js/**", "/images/**",
                                 "/CSS/**", "/JavaScript/**", "/Images/**",
                                 "/static/**", "/webjars/**",
                                 "/access-denied").permitAll()

                // ADMIN only
                .requestMatchers(
                    "/employees", "/employees/**",
                    "/addEmployee", "/saveEmployee",
                    "/editEmployee/**", "/deleteEmployee/**",
                    "/departments", "/departments/**",
                    "/downloadEmployees",
                    "/approveLeave/**", "/rejectLeave/**",
                    "/announcements/post", "/announcements/delete/**",
                    "/tasks/assign", "/tasks/delete/**",
                    "/wfh/approve/**", "/wfh/reject/**",
                    "/complaints/respond/**",
                    "/salary/generate",
                    "/performance/add", "/performance/delete/**",
                    "/holidays/add", "/holidays/delete/**"
                ).hasRole("ADMIN")

                // Both roles
                .requestMatchers(
                    "/dashboard",
                    "/attendance", "/saveAttendance", "/markMyAttendance",
                    "/leave", "/applyLeave",
                    "/my-profile",
                    "/announcements",
                    "/messages", "/messages/**",
                    "/wfh", "/wfh/**",
                    "/complaints", "/complaints/**",
                    "/tasks", "/tasks/**",
                    "/notifications", "/notifications/**",
                    "/salary", "/salary/download/**",
                    "/performance",
                    "/holidays",
                    "/change-password"
                ).hasAnyRole("ADMIN", "EMPLOYEE")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)   // DB se role load karo
                )
                .successHandler(oAuth2LoginSuccessHandler)
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        return http.build();
    }
}
