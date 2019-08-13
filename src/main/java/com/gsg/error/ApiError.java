package com.gsg.error;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApiError {
	private HttpStatus status;
	private String errCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();
	private String message;
	private List<String> errors;

	public ApiError(String errCode, HttpStatus status, String message, List<String> errors) {
		this.errCode = errCode;
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public ApiError(String errCode, HttpStatus status, String message, String error) {
		this.errCode = errCode;
		this.status = status;
		this.message = message;
		errors = Arrays.asList(error);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ApiError [status=" + status + ", timestamp=" + timestamp + ", message=" + message + ", errors="
				+ errors + "]";
	}

}