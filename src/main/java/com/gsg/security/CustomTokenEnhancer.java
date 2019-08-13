package com.gsg.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {

	static Logger logger = LoggerFactory.getLogger(CustomTokenEnhancer.class);

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		logger.info("Spring User >> " + user);
		logger.info(user.getAuthorities().toArray().toString());

		List<String> authorities = new ArrayList<>();

		user.getAuthorities().forEach(a -> {
			authorities.add(a.getAuthority());
		});
		Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("organization", "GSG");
		additionalInfo.put("authorities", authorities.toArray());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}