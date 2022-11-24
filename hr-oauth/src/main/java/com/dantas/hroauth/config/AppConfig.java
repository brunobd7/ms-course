package com.dantas.hroauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;


    /**
     * Configuration bean to encrypt password
     * */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration bean to manage our jwt token and set configurations
     * */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtSecret);
        return  tokenConverter;
    }

    /**
     *  This bean read information inside accessToken object
     * */
    @Bean
    public JwtTokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }


}
