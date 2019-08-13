package com.gsg.utilities;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.gsg.error.RTException;
import com.gsg.mongo.model.rt.TicketRequest;
import com.gsg.mongo.model.rt.TicketResponse;

@Component
@PropertySource("classpath:rt.properties")
public class TicketUtility {

	@Value("${rt.url}")
	private String baseUrl;

	@Value("${rt.token}")
	private String authToken;

	Logger logger = LoggerFactory.getLogger(TicketUtility.class);

	public TicketResponse createRTTicket(TicketRequest tktReq) throws JsonParseException, IOException {
		logger.info("TicketUtility.createTicket()");
		
		TicketResponse response = new TicketResponse();

		String url = baseUrl + "/ticket";

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(new MediaType("application", "json"));
		requestHeaders.add("Authorization", "token " + authToken);
		HttpEntity<TicketRequest> requestEntity = new HttpEntity<>(tktReq, requestHeaders);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<TicketResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				TicketResponse.class);

		response = responseEntity.getBody();
		logger.info("result>>" + responseEntity);

		return response;
	}

	public TicketResponse updateRTTicket(String ticketId, TicketRequest tktReq) throws RTException {
		logger.info("TicketUtility.updateTicket()");
		TicketResponse response = new TicketResponse();

		String url = baseUrl + "/ticket/" + ticketId;
		

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		requestHeaders.setContentType(new MediaType("application", "json"));
		requestHeaders.add("Authorization", "token " + authToken);
		HttpEntity<TicketRequest> requestEntity = new HttpEntity<>(tktReq, requestHeaders);
		logger.debug(requestEntity.getBody().toString());
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,String.class);

		int sts = responseEntity.getStatusCode().value();
		if(sts >400){
			throw new RTException("RT Error occured with status code"+sts);
		}

		return response;
	}

	/*public TicketResponse createTicket(String subject, String queue) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		TicketResponse response = new TicketResponse();

		HttpURLConnection conn = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		try {
			URL object = new URL(baseUrl + "/ticket");
			conn = (HttpURLConnection) object.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", contentType);
			conn.setRequestProperty("Authorization", "token " + authToken);
			conn.setRequestProperty("Accept", contentType);
			conn.setRequestMethod("POST");

			ObjectNode jsonObj = mapper.createObjectNode();
			jsonObj.put("Subject", subject);
			jsonObj.put("Queue", queue);

			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(jsonObj.toString());
			wr.flush();

			// if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String responseStr, jsonVal = "";
			while ((responseStr = reader.readLine()) != null)
				jsonVal += responseStr;

			reader.close();

			response = mapper.readValue(jsonVal, TicketResponse.class);
			// }

			return response;

		} catch (MalformedURLException e) {

			e.printStackTrace();
			response.setType("failed");
			response.setId(null);
			response.set_url("no_url");
			return response;

		} finally {
			wr.close();
			mapper = null;
			response = null;
			reader = null;
			conn = null;
		}
*/
	
}

