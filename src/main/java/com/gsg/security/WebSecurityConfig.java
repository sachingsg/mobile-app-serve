package com.gsg.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	private UserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${security.signing-key}")
	private String signingKey;

	@Value("${security.encoding-strength}")
	private Integer encodingStrength;

	@Value("${security.security-realm}")
	private String securityRealm;

	public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("WebSecurityConfig.configure()");
		
		 http .cors() .and() .csrf() .disable()
		 .authorizeRequests().antMatchers("/users/**", "/otp/**").permitAll()
		 .anyRequest().authenticated().and() 
		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.httpBasic()
				.realmName(securityRealm)
				.and()
				.csrf()
				.disable();

	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("WebSecurityConfig.configure()");
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		logger.info("WebSecurityConfig.corsConfigurationSource()");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	
	 @Bean
	   public JwtAccessTokenConverter accessTokenConverter() {
		 logger.info("WebSecurityConfig.accessTokenConverter()");
	      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	      converter.setSigningKey(signingKey);
	      return converter;
	   }

	   @Bean
	   public TokenStore tokenStore() {
		   logger.info("WebSecurityConfig.tokenStore()");
	      return new JwtTokenStore(accessTokenConverter());
	   }

	   @Bean
	   @Primary //Making this primary to avoid any accidental duplication with another token service instance of the same name
	   public DefaultTokenServices tokenServices() {
		   logger.info("WebSecurityConfig.tokenServices()");
	      DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	      defaultTokenServices.setTokenStore(tokenStore());
	      defaultTokenServices.setSupportRefreshToken(true);
	      
	      
	      return defaultTokenServices;
	   }
	
	
}
