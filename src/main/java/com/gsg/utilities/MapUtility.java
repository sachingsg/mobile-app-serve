package com.gsg.utilities;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppUser.MapLocation;
import com.gsg.utilities.SMSUtility.ShortMessage;

@Component
@PropertySource("classpath:application.properties")
public class MapUtility {
	
	private static Logger logger = LoggerFactory.getLogger(MapUtility.class);
	

	@Value("${map.geocode.apikey}")
	private String geocodeApiKey;
	
	public String getLocationStateDetails(MapLocation location) throws JsonProcessingException, IOException, GenericException {

		StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location
				+ "&key=" + geocodeApiKey);

		logger.info("Map URL >>" + url);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		// requestHeaders.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<ShortMessage> requestEntity = new HttpEntity<>(requestHeaders);

		RestTemplate restTemplate = new RestTemplate();

		String response = restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class).getBody();
		// System.out.println(response);

		String stateName = "";

		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(response);

		JsonNode result = actualObj.get("results");
		
		if(result.size() !=0 ){
			JsonNode address_components = result.get(0).get("address_components");
			for (int i = 0; i < address_components.size(); i++) {
				JsonNode types = address_components.get(i).get("types");
	
				for (int j = 0; j < types.size(); j++) {
					if (types.get(j).asText().equals("administrative_area_level_1")) {
						JsonNode longName = address_components.get(i).get("long_name");
						System.out.println("State >>" + longName.asText());
						stateName = longName.asText();
						break;
					}
				}
	
				if (!StringUtils.isEmpty(stateName)) {
					break;
				}
			}
		}else{
			throw new GenericException("Location can not be found.");
		}
		logger.info("State >>" + stateName);
		return stateName;

	}
}
