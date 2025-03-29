package com.ikeyit.common.data;

/**
 * Utility class for handling privacy-sensitive data.
 * Provides methods to mask sensitive information like phone numbers and email addresses
 * by replacing characters with asterisks while preserving some parts of the original text.
 */
public class PrivacyUtils {

    /**
     * Masks a portion of the input text with asterisks (*) while preserving some characters
     * at the beginning and end.
     * 
     * For text longer than size + 2:
     * - Preserves (length-size)/2 characters at the start and end
     * - Replaces 'size' characters in the middle with asterisks
     * 
     * For text shorter than or equal to size + 1:
     * - Preserves first character
     * - Replaces remaining characters with asterisks
     *
     * @param text the text to mask
     * @param size the number of characters to mask
     * @return the masked text, or the original text if null or empty
     */
    public static String hidePrivacy(String text, int size) {
        if (text == null || text.isEmpty())
            return text;

        int len = text.length();
        char[] chars = new char[len];
        if (len >= size + 2) {
            int startLen = (len - size) / 2;
            int i = 0;
            for (;i < startLen; i++)
                chars[i] = text.charAt(i);
            for (;i < startLen + size; i++)
                chars[i] = '*';
            for (;i < len; i++)
                chars[i] = text.charAt(i);
        } else if (len <= size + 1) {
            int i = 0;
            chars[i] = text.charAt(i);
            for (i++;i < len;i++)
                chars[i] = '*';
        }

        return new String(chars);
    }

    /**
     * Masks a portion of an email address while preserving the domain part.
     * The local part (before @) is masked using the hidePrivacy method,
     * while the domain part (after @) remains unchanged.
     *
     * @param email the email address to mask
     * @param size the number of characters to mask in the local part
     * @return the masked email address, or the original email if null or empty
     */
    public static String hideEmail(String email, int size) {
        if (email == null || email.isEmpty())
            return email;
        String[] parts = email.split("@");
        return PrivacyUtils.hidePrivacy(parts[0], size) + '@' + parts[1];
    }
}
