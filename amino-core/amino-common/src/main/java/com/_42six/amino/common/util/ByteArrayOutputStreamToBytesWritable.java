package com._42six.amino.common.util;

import com._42six.amino.common.FeatureFact;
import org.apache.accumulo.core.data.ArrayByteSequence;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;

import java.io.*;

/**
 * This class allows one to move the byte buffer directly into a BytesWritable object.  This way
 * no useless copy is needed.  Note that both this stream and every bytes writable where share the
 * underlying byte buffer.  Though also note that {@link org.apache.hadoop.io.BytesWritable#getBytes()}
 * returns a copy of the bytes buffer.  As does {@link #toByteArray()} on {@link java.io.ByteArrayOutputStream}.
 */

public class ByteArrayOutputStreamToBytesWritable extends ByteArrayOutputStream {

    public ByteArrayOutputStreamToBytesWritable() {}

    public BytesWritable toBytesWritable() {
        return new BytesWritable(buf, count);
    }

    private static Writable newInstanceOfSameType(Writable obj) {
        try {
            return obj.getClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Writable cloneWritable(Writable old) {
        try {
            ByteArrayOutputStreamToBytesWritable  buf = new ByteArrayOutputStreamToBytesWritable();
            DataOutputStream outputStream = new DataOutputStream(buf);
            old.write(outputStream);
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(buf.buf, 0, buf.size()));
            Writable dst = newInstanceOfSameType(old);
            dst.readFields(inputStream);
            return dst;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BytesWritable toBytesWritable(Writable old) {
        try {
            ByteArrayOutputStreamToBytesWritable  buf = new ByteArrayOutputStreamToBytesWritable();
            DataOutputStream outputStream = new DataOutputStream(buf);
            old.write(outputStream);
            return buf.toBytesWritable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ByteSequence toByteSequence(Writable old) {
        try {
            ByteArrayOutputStreamToBytesWritable  buf = new ByteArrayOutputStreamToBytesWritable();
            DataOutputStream outputStream = new DataOutputStream(buf);
            old.write(outputStream);
            return new ArrayByteSequence(buf.buf, 0, buf.count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Writable fromByteSequence(ByteSequence sequence, Writable dst) {
        try {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(sequence.getBackingArray(),
                    sequence.offset(), sequence.length()));
            dst.readFields(inputStream);
            return dst;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}