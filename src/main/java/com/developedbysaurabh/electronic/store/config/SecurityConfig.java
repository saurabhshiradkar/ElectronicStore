package com.developedbysaurabh.electronic.store.config;


import com.developedbysaurabh.electronic.store.security.JwtAuthenticationEntryPoint;
import com.developedbysaurabh.electronic.store.security.JwtAuthenticationFilter;
import com.developedbysaurabh.electronic.store.services.impl.CustomeUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private CustomeUserDetailService userDetailService;
    private JwtAuthenticationEntryPoint entryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS = {

            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/test"


    };

    @Autowired
    public SecurityConfig(CustomeUserDetailService userDetailService, JwtAuthenticationEntryPoint entryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.userDetailService = userDetailService;
        this.entryPoint = entryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //CORS Configuration
    @Bean
    public FilterRegistrationBean corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**",configuration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));

        filterRegistrationBean.setOrder(-200);

        return filterRegistrationBean;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        cors.disable()
        http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/auth/login")
                                .permitAll()
                                .requestMatchers("/auth/google")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/image/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/image/**").hasAnyRole("NORMAL","ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAnyRole("NORMAL","ADMIN")
                                .requestMatchers(HttpMethod.POST,"/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST,"/carts/**").hasAnyRole("NORMAL","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/carts/**").hasAnyRole("NORMAL","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/carts/**").hasAnyRole("NORMAL","ADMIN")

                                .requestMatchers(PUBLIC_URLS)
                                .permitAll()
                                .requestMatchers(HttpMethod.GET)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                        )
                .exceptionHandling(config->config.authenticationEntryPoint(entryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }












//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("login.html")
//                .loginProcessingUrl("/process-url")
//                .defaultSuccessUrl("/dashboard")
//                .failureUrl("error")
//                .and()
//                .logout()
//                .logoutUrl("/logout");
//        return http.build();
//
//        return null;
//    }




//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails normal = User.builder()
//                .username("root1")
//                .password(passwordEncoder().encode("root1"))
//                .roles("NORMAL")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("root2")
//                .password(passwordEncoder().encode("root2"))
//                .roles("ADMIN")
//                .build();
//
//        //users create
////        InMemoryUserDetailsManager implementation class of UserDetailsService
//
//        return new InMemoryUserDetailsManager(normal,admin);
//    }
//

}










