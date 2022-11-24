package com.dantas.hrapigatewayzuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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

    }
}
