package com.ikeyit.common.data;

import java.nio.ByteBuffer;
import java.util.UUID;

import java.util.Base64;

/**
 * Utility class for generating unique identifiers.
 * Provides methods to create URL-safe, base64-encoded UUIDs.
 */
public class IdUtils {
	/**
	 * Generates a URL-safe, base64-encoded UUID string.
	 * The generated string:
	 * - Is based on a random UUID
	 * - Uses URL-safe base64 encoding
	 * - Has padding removed
	 * - Is 22 characters long
	 *
	 * @return a URL-safe, base64-encoded UUID string
	 */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
	    byte[] bytes = ByteBuffer.allocate(16).putLong(0, uuid.getLeastSignificantBits()).putLong(8, uuid.getMostSignificantBits()).array();
	    String withPadding = Base64.getUrlEncoder().encodeToString(bytes);
		return withPadding.substring(0, withPadding.length() - 2);
	}
}
