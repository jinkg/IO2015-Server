package com.jin.u2f.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jin.u2f.U2F;
import com.jin.u2f.data.DeviceRegistration;
import com.jin.u2f.data.U2FUser;
import com.jin.u2f.data.messages.AuthenticateRequestData;
import com.jin.u2f.data.messages.AuthenticateResponse;
import com.jin.u2f.data.messages.RegisterRequestData;
import com.jin.u2f.data.messages.RegisterResponse;
import com.jin.u2f.exceptions.DeviceCompromisedException;
import com.jin.u2f.exceptions.NoEligableDevicesException;
import com.jin.u2f.repository.UserRepository;
import com.jin.u2f.response.ResponseBuilder;

@Controller
public class ApiController {
	public static final String APP_ID = "https://localhost:8443";

	private final U2F u2f = new U2F();
	private final Map<String, String> requestStorage = new HashMap<String, String>();

	@Autowired
	private UserRepository userStorage;

	@RequestMapping("/startRegistration")
	public @ResponseBody String startRegistration(@RequestParam(value = "username") String username) {
		RegisterRequestData registerRequestData = u2f.startRegistration(APP_ID, getRegistrations(username));
		requestStorage.put(registerRequestData.getRequestId(), registerRequestData.toJson());
		return ResponseBuilder.buildStartRegistrationData(registerRequestData);
	}

	@RequestMapping(value = "/finishRegistration", method = RequestMethod.POST)
	public @ResponseBody String finishRegistration(@RequestParam(value = "tokenResponse") String response,
			@RequestParam(value = "username") String username) {
		RegisterResponse registerResponse = RegisterResponse.fromJson(response);
		RegisterRequestData registerRequestData = RegisterRequestData
				.fromJson(requestStorage.remove(registerResponse.getRequestId()));
		DeviceRegistration registration = u2f.finishRegistration(registerRequestData, registerResponse);
		addRegistration(username, registration);
		return ResponseBuilder.buildFinishRegistrationResultData();
	}

	@RequestMapping("/startAuthentication")
	public @ResponseBody String startAuthentication(@RequestParam("username") String username) {
		AuthenticateRequestData authenticateRequestData;
		try {
			authenticateRequestData = u2f.startAuthentication(APP_ID, getRegistrations(username));
		} catch (NoEligableDevicesException e) {
			return ResponseBuilder.buildNoDeviceError();
		}
		requestStorage.put(authenticateRequestData.getRequestId(), authenticateRequestData.toJson());
		return ResponseBuilder.buildStartAuthenticateData(authenticateRequestData);
	}

	@RequestMapping(value = "/finishAuthentication", method = RequestMethod.POST)
	public @ResponseBody String finishAuthentication(@RequestParam("tokenResponse") String response,
			@RequestParam("username") String username) {
		AuthenticateResponse authenticateResponse = AuthenticateResponse.fromJson(response);
		AuthenticateRequestData authenticateRequest = AuthenticateRequestData
				.fromJson(requestStorage.remove(authenticateResponse.getRequestId()));
		try {
			u2f.finishAuthentication(authenticateRequest, authenticateResponse, getRegistrations(username));
		} catch (DeviceCompromisedException e) {
			return "<p>Device possibly compromised and therefore blocked: " + e.getMessage() + "</p>";
		}
		return ResponseBuilder.buildFinishAuthenticateResultData();
	}

	private Iterable<DeviceRegistration> getRegistrations(String username) {
		List<DeviceRegistration> registrations = new ArrayList<DeviceRegistration>();

		for (U2FUser user : userStorage.findByUsername(username)) {
			registrations.add(DeviceRegistration.fromJson(user.getData()));
		}
		return registrations;
	}

	private void addRegistration(String username, DeviceRegistration registration) {
		userStorage.insert(new U2FUser(username, registration.getKeyHandle(), registration.toJson()));
	}
}
