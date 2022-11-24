package com.dantas.hroauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Autowired
    private JwtTokenStore jwtTokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${oauth.client.name}")
    private String clientName;

    @Value("${oauth.client.secret}")
    private String clientSecret;



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * Configuration of authentication and authorization basement on app/client credentials and client type.
     * <p>
     * @apiNote  withClient -> app/client ID
     *  <br> secret -> app/client password access
     *  <br> scopes -> app/client permissions read, write or both
     *  <br> authorizedGrantTypes -> grant type used by user credentials on app/client
     *  <br> accessTokenValidity -> time to token expiration in seconds;
     * */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientName)
                .secret(bCryptPasswordEncoder.encode(clientSecret))
                .scopes("read","write")
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(86400);
    }

    /**
     * Configuring token processing with configuration bean crated in AppConfig
     * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(accessTokenConverter);
    }
}
