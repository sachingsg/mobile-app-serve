package com.gsg.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	static Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);
	
   @Value("${security.jwt.client-id}")
   private String clientId;

   @Value("${security.jwt.client-secret}")
   private String clientSecret;

   @Value("${security.jwt.grant-type}")
   private String grantType;

   @Value("${security.jwt.scope-read}")
   private String scopeRead;

   @Value("${security.jwt.scope-write}")
   private String scopeWrite = "write";

   @Value("${security.jwt.resource-ids}")
   private String resourceIds;

   @Autowired
   private TokenStore tokenStore;

   @Autowired
   private JwtAccessTokenConverter accessTokenConverter;

   @Autowired
   private AuthenticationManager authenticationManager;

   @Override
   public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
	   logger.info("AuthorizationServerConfig.configure()");
      configurer
              .inMemory()
              .withClient(clientId)
              .secret(clientSecret)
              .authorizedGrantTypes("client-credentials", "password","refresh_token")//grantType)
              .scopes(scopeRead, scopeWrite)
              .resourceIds(resourceIds);
              //.accessTokenValiditySeconds(21600) //21600 6hrs
              //.refreshTokenValiditySeconds(2592000); //30days
      
   }

   @Override
   public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	   logger.info("AuthorizationServerConfig.configure()");
      TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
      enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter,getTokenEnahncer()));
      endpoints.tokenStore(tokenStore)
              .accessTokenConverter(accessTokenConverter)
              .tokenEnhancer(enhancerChain)
              .authenticationManager(authenticationManager)
              .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
   }
   
   @Bean
  public TokenEnhancer getTokenEnahncer() {
	  return new CustomTokenEnhancer();
  }

}