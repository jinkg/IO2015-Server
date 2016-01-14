package com.jin.u2f.data;

public class U2FUser {
	private String username;
	private String keyHandle;
	private String registrationDataString;
	
	public U2FUser(){
	}

	public U2FUser(String username, String keyHandle, String data) {
		this.username = username;
		this.keyHandle = keyHandle;
		this.registrationDataString = data;
	}

	public String getUsername() {
		return username;
	}

	public String getKeyHandle() {
		return keyHandle;
	}

	public String getData() {
		return registrationDataString;
	}
}
