package com.ikeyit.common.data;


/**
 * Utilities for privacy. For example, replacing some numbers in the phone number to asterisk.
 */
public class PrivacyUtils {

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


    public static String hideEmail(String email, int size) {
        if (email == null || email.isEmpty())
            return email;
        String[] parts = email.split("@");
        return PrivacyUtils.hidePrivacy(parts[0], size) + '@' + parts[1];
    }
}
