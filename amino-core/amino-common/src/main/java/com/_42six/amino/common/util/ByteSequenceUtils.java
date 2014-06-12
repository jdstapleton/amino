package com._42six.amino.common.util;

import com.google.common.io.BaseEncoding;
import org.apache.accumulo.core.data.ByteSequence;

public final class ByteSequenceUtils {

    private ByteSequenceUtils() {}


    public static String bytesSequenceToHexString(ByteSequence writable) {
        return BaseEncoding.base16().encode(writable.getBackingArray(), 0, writable.length());
    }

    public static String bytesSequenceToHexStringWithSpaces(ByteSequence writable) {
        return BaseEncoding.base16().encode(writable.getBackingArray(), 0, writable.length()).replaceAll(".{2}", "$0 ").trim();
    }
}
