package com.dantas.hrapigatewayzuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /** Injection of object tokenStore to use jwt access token pass thought http auth headers*/
    @Autowired
    private JwtTokenStore tokenStore;

    private static final String[] PUBLIC_PATHS = {"/hr-oauth/oauth/token"}; //PATH TO LOGIN ON AUTH SERVER
    private static final String[] OPERATOR_ROLE_PATHS = {"/hr-worker/**"}; // AUTHORIZED PATHS TO ROLE 'OPERATOR'
    private static final String[] ADMIN_ROLE_PATHS = {"/hr-payroll/**", "/hr-user/**",
            "/actuator/**", "/hr-worker/actuator/**", "/hr-oauth/actuator/**"}; // AUTHORIZED PATHS TO ROLE 'ADMIN'

    /** With this configuration application can read token in authorization headers of http request*/
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    /**
    * Configuring access to specified paths for specified roles, both are passed thought JWT token
    * @param httpSecurity
    */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers(PUBLIC_PATHS).permitAll()
                .antMatchers(HttpMethod.GET,OPERATOR_ROLE_PATHS).hasAnyRole("OPERATOR","ADMIN")
                .antMatchers(ADMIN_ROLE_PATHS).hasRole("ADMIN")
                .anyRequest().authenticated();

        httpSecurity.cors().configurationSource(corsConfigurationSource());
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
