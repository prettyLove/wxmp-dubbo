package com.kochun.wxmp.core.vo.internal.response;

public class ResponseResult {
	
	private int code;
	private Object data;
	private String message;
	private String messageCode;
	
	public static final ResponseResult successResponse(Object data) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setData(data);
		return responseResult;
	}
	
	public static final ResponseResult successResponse() {
		return new ResponseResult();
	}

	public static final ResponseResult successResponse(String messageCode) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setMessageCode(messageCode);
		return responseResult;
	}
	
	public static final ResponseResult failResponse() {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setCode(-1);
		return responseResult;
	}
	
	public static final ResponseResult failResponse(String messageCode) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setCode(-1);
		responseResult.setMessageCode(messageCode);
		return responseResult;
	}
	
	public static final ResponseResult failResponse(int code, String messageCode) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setCode(code);
		responseResult.setMessageCode(messageCode);
		return responseResult;
	}

	public static final ResponseResult failResponse(int code, String messageCode,Object data) {
		ResponseResult responseResult = new ResponseResult();
		responseResult.setCode(code);
		responseResult.setData(data);
		responseResult.setMessageCode(messageCode);
		return responseResult;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
}
