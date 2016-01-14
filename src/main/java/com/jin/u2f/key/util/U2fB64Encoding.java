package com.jin.u2f.key.util;

import org.apache.commons.codec.binary.Base64;

public class U2fB64Encoding {

	public static String encode(byte[] decoded) {
		return Base64.encodeBase64URLSafeString(decoded);
	}

	public static byte[] decode(String encoded) {
		return Base64.decodeBase64(encoded);
	}
}
