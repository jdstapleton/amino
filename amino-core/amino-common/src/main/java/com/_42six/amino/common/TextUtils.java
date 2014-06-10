package com._42six.amino.common;

import org.apache.hadoop.io.Text;

import java.io.UnsupportedEncodingException;

public class TextUtils {
	public static byte [] getBytes(Text text) {
		byte [] bytes = text.getBytes();
		if(bytes.length != text.getLength()) {
			bytes = new byte[text.getLength()];
			System.arraycopy(text.getBytes(), 0, bytes, 0, bytes.length);
		}
		return bytes;
	}

    public static byte [] getBytes(String text) {
        return text.getBytes();
    }

    public static String asString(byte[] text) {
        try {
            return new String(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
