package com.jin.u2f.response;

import com.jin.u2f.data.messages.json.JsonSerializable;

public class JsonHttpResponse extends JsonSerializable{
	private int errorCode;
	private Object data;
	
	public JsonHttpResponse(int errorCode) {
		this.errorCode = errorCode;
		this.data = null;
	}
	
	public JsonHttpResponse(int errorCode, Object data) {
		this.errorCode = errorCode;
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Object getData() {
		return data;
	}
}
