package com.gsg.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	static Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);
	
	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		logger.info("ResourceServerConfig.configure()");
		resources.resourceId(resourceIds).tokenServices(tokenServices);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		logger.info("ResourceServerConfig.configure()");
		http
				.requestMatchers()
				.and()
				.authorizeRequests()
				//.antMatchers("/login/**", "/otp/**").permitAll()
				// .antMatchers("/actuator/**", "/api-docs/**").permitAll()
				// .antMatchers("/springjwt/**" ).authenticated()
			.antMatchers("/**").permitAll()
		//.antMatchers("/api/**").authenticated()
		;
	}
}
