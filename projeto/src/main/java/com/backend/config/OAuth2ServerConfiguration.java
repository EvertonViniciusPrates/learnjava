package com.backend.config;

import com.backend.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * OAuth2ServerConfiguration
 */

 @Configuration
 @EnableResourceServer
public class OAuth2ServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     *
     */

    private static final String RESTSERVICE = "restservice";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESTSERVICE);
    }
    
    @Override public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.logout()
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .and()
        .authorizeRequests()
        .anyRequest().fullyAuthenticated()
        .antMatchers(HttpMethod.OPTIONS,"/**").permitAll();
    }

    @Configuration
    @EnableResourceServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{
        private TokenStore tokenStore = new InMemoryTokenStore();
        
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;
        @Autowired
        private MyUserDetailsService userDetailsService;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception{
            endpointsConfigurer.tokenStore(this.tokenStore)
            .authenticationManager(this.authenticationManager)
            .userDetailsService(userDetailsService);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
            clients.inMemory()
            .withClient("client")
            .authorizedGrantTypes("password","authorization_code","refresh_token").scopes("all")
            .refreshTokenValiditySeconds(3000000)
            .resourceIds(RESTSERVICE)
            .secret(passwordEncoder.encode("123"))
            .accessTokenValiditySeconds(5000000);
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices(){
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }

    }
}