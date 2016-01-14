package com.jin.u2f.response;

import com.jin.u2f.data.messages.AuthenticateRequestData;
import com.jin.u2f.data.messages.RegisterRequestData;

public class ResponseBuilder {
	public static String buildStartRegistrationData(RegisterRequestData data) {
		return new JsonHttpResponse(ErrorCode.SUCCESS, data).toJson();
	}

	public static String buildFinishRegistrationResultData() {
		return new JsonHttpResponse(ErrorCode.SUCCESS).toJson();
	}

	public static String buildStartAuthenticateData(AuthenticateRequestData data) {
		return new JsonHttpResponse(ErrorCode.SUCCESS, data).toJson();
	}

	public static String buildFinishAuthenticateResultData() {
		return buildFinishRegistrationResultData();
	}

	public static String buildNoDeviceError() {
		return new JsonHttpResponse(ErrorCode.ERROR_NO_DEVICE_ERROR).toJson();
	}
}
