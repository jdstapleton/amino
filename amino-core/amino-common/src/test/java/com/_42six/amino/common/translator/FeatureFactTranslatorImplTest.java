package com._42six.amino.common.translator;

import com._42six.amino.common.DateFeatureFact;
import com._42six.amino.common.util.LexigraphicalListComparator;
import com._42six.amino.common.writable.CoordinateWritable;
import org.apache.accumulo.core.data.ArrayByteSequence;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com._42six.amino.common.util.ByteSequenceUtils.bytesSequenceToHexStringWithSpaces;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeatureFactTranslatorImplTest {

    DateFeatureFact fact;
    FeatureFactTranslatorInt translator;

    @Before
    public void setup(){
        fact = new DateFeatureFact(1056206458000L);
        translator = new FeatureFactTranslatorImpl();
    }


    // {{{ Serialization Tests
    @Test
    public void testFromFeatureFact() throws Exception {
        ByteSequence actual = translator.fromFeatureFact(fact);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("00 00 00 F5 E7 A9 00 00", actualInHex);
    }

    @Test
    public void testToFeatureFact() throws Exception {
        // should be the serialized version of 1056153600000L
        final byte[] ORIGINAL = {0, 0, 0, -11, -25, -87, 0, 0};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        DateFeatureFact actual = new DateFeatureFact(0);
        translator.toFeatureFact(to_test, actual);
        assertEquals(1056153600000L, actual.asTimeStamp());
    }

    @Test
    public void testFromRatio() throws Exception {
        final double a_ratio = 3.14159265359;
        ByteSequence actual = translator.fromRatio(a_ratio);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("08 C0 09 21 FB 54 44 2E EA", actualInHex);
    }

    @Test
    public void testToRatio() throws Exception {
        final byte[] ORIGINAL = {8, -64, 9, 33, -5, 84, 68, 46, -22};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        assertEquals(3.14159265359, translator.toRatio(to_test), 0.000001);
    }

    @Test
    public void testFromInterval() throws Exception {
        final double an_interval = 3.14159265359;
        ByteSequence actual = translator.fromInterval(an_interval);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("08 C0 09 21 FB 54 44 2E EA", actualInHex);
    }

    @Test
    public void testToInterval() throws Exception {
        final byte[] ORIGINAL = {8, -64, 9, 33, -5, 84, 68, 46, -22};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        assertEquals(3.14159265359, translator.toInterval(to_test), 0.000001);
    }

    @Test
    public void testFromCoordinate() throws Exception {
        final CoordinateWritable ORIGINAL = new CoordinateWritable(74.0059, 40.7127);
        final ByteSequence actual = translator.fromCoordinate(ORIGINAL);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("08 C0 52 80 60 AA 64 C2 F8 00 08 C0 44 5B 39 C0 EB ED FA", actualInHex);
    }


    @Test
    public void testToCoordinate() throws Exception {
        final byte[] ORIGINAL = {8, -64, 82, -128, 96, -86, 100, -62,
                -8, 0, 8, -64, 68, 91, 57, -64, -21, -19, -6};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        CoordinateWritable actual = translator.toCoordinate(to_test);
        assertEquals(74.0059, actual.longitudex, 0.000001);
        assertEquals(40.7127, actual.latitudey, 0.000001);
    }

    final List<CoordinateWritable> ORIGINAL_POLY = Arrays.asList(new CoordinateWritable(74.0059, 40.7127), new CoordinateWritable(72.0059, 40.7127),
            new CoordinateWritable(74.0059, 38.7127), new CoordinateWritable(72.0059, 38.7127));

    @Test
    public void testFromPolygon() throws Exception {
        final ByteSequence actual = translator.fromPolygon(ORIGINAL_POLY);
        List<CoordinateWritable> foo = translator.toPolygon(actual);
        assertEquals(ORIGINAL_POLY, foo);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("08 C0 52 80 60 AA 64 C2 " +
                "F8 01 01 08 C0 44 5B 39 " +
                "C0 EB ED FA 00 08 C0 52 " +
                "01 02 01 02 60 AA 64 C2 " +
                "F8 01 01 08 C0 44 5B 39 " +
                "C0 EB ED FA 00 08 C0 52 " +
                "80 60 AA 64 C2 F8 01 01 " +
                "08 C0 43 5B 39 C0 EB ED " +
                "FA 00 08 C0 52 01 02 01 " +
                "02 60 AA 64 C2 F8 01 01 " +
                "08 C0 43 5B 39 C0 EB ED FA", actualInHex);
    }

    @Test
    public void testToPolygon() throws Exception {
        // This is the same as the FromPolygon cord set
        final byte[] ORIGINAL = {
                   8, -64,  82, -128,  96, -86, 100, -62,  -8,   1,   1,   8, -64,  68,  91,  57,
                 -64, -21, -19,   -6,   0,   8, -64,  82,   1,   2,   1,   2,  96, -86, 100, -62,
                  -8,   1,   1,    8, -64,  68,  91,  57, -64, -21, -19,  -6,   0,   8, -64,  82,
                -128,  96, -86,  100, -62,  -8,   1,   1,   8, -64,  67,  91,  57, -64, -21, -19,
                  -6,   0,   8,  -64,  82,   1,   2,   1,   2,  96, -86, 100, -62,  -8,   1,   1,
                   8, -64,  67,   91,  57, -64, -21, -19,  -6 };

        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        List<CoordinateWritable> actual = translator.toPolygon(to_test);
        assertEquals(ORIGINAL_POLY, actual);
    }

    @Test
    public void testFromText() throws Exception {
        final Text ORIGINAL = new Text("Hello World");
        final ByteSequence actual = translator.fromText(ORIGINAL);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals("48 65 6C 6C 6F 20 57 6F 72 6C 64", actualInHex);
    }


    @Test
    public void testToText() throws Exception {
        final byte[] ORIGINAL = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        assertEquals(new Text("Hello World"), translator.toText(to_test));
    }

    @Test
    public void testToDate() throws Exception {
        final long ORIGINAL = 1056206458000L;
        final ByteSequence actual = translator.fromDate(ORIGINAL);
        String actualInHex = bytesSequenceToHexStringWithSpaces(actual);
        assertEquals(ORIGINAL, translator.toDate(actual));
        assertEquals("05 F5 EA CF 8C 90", actualInHex);
    }

    @Test
    public void testFromDate() throws Exception {
        final byte[] ORIGINAL = {5, -11, -22, -49, -116, -112};
        final ByteSequence to_test = new ArrayByteSequence(ORIGINAL);
        assertEquals(1056206458000L, translator.toDate(to_test));
    }
    // Serialization Tests }}}

    // Now validate that the serialized version lexigraphly sorts in the same order as the objects themselves
    @Test
    public void testRatioOrder() throws Exception {
        final double a_ratio = 3.14159265359;
        final double b_ratio = 6.28318530718;
        ByteSequence a_actual = translator.fromRatio(a_ratio);
        ByteSequence b_actual = translator.fromRatio(b_ratio);

        assertTrue("a < b", a_actual.compareTo(b_actual) < 0);
        assertTrue("b > a", b_actual.compareTo(a_actual) > 0);
    }

    @Test
    public void testIntervalOrder() throws Exception {
        final double a_interval = 3.14159265359;
        final double b_interval = 6.28318530718;
        ByteSequence a_actual = translator.fromInterval(a_interval);
        ByteSequence b_actual = translator.fromInterval(b_interval);

        assertTrue("a < b", a_actual.compareTo(b_actual) < 0);
        assertTrue("b > a", b_actual.compareTo(a_actual) > 0);
    }

    @Test
    public void testCoordinateOrder() throws Exception {
        // A > B > C > D
        final CoordinateWritable a_coord = new CoordinateWritable(74.0059, 44.7274);
        final CoordinateWritable b_coord = new CoordinateWritable(74.0059, 40.7127);
        final CoordinateWritable c_coord = new CoordinateWritable(71.3269, 40.7127);
        final CoordinateWritable d_coord = new CoordinateWritable(21.1133, 44.7274);

        ByteSequence a_actual = translator.fromCoordinate(a_coord);
        ByteSequence b_actual = translator.fromCoordinate(b_coord);
        ByteSequence c_actual = translator.fromCoordinate(c_coord);
        ByteSequence d_actual = translator.fromCoordinate(d_coord);


        // Since this is an object ensure that the order is consistent
        assertTrue(b_coord.compareTo(a_coord) < 0);
        assertTrue(b_coord.compareTo(c_coord) > 0);
        assertTrue(b_coord.compareTo(d_coord) > 0);

        assertTrue(c_coord.compareTo(a_coord) < 0);
        assertTrue(c_coord.compareTo(b_coord) < 0);
        assertTrue(c_coord.compareTo(d_coord) > 0);

        assertTrue(a_coord.compareTo(b_coord) > 0);
        assertTrue(a_coord.compareTo(c_coord) > 0);
        assertTrue(a_coord.compareTo(d_coord) > 0);

        assertTrue(d_coord.compareTo(a_coord) < 0);
        assertTrue(d_coord.compareTo(b_coord) < 0);
        assertTrue(d_coord.compareTo(c_coord) < 0);

        // now lets compare the byte sequences
        assertTrue("a > b", a_actual.compareTo(b_actual) > 0);
        assertTrue("b > c", b_actual.compareTo(c_actual) > 0);
        assertTrue("c > d", c_actual.compareTo(d_actual) > 0);
    }

    @Test
    public void testPolygonOrder() throws Exception {
        final CoordinateWritable a_coord = new CoordinateWritable(74.0059, 44.7274);
        final CoordinateWritable b_coord = new CoordinateWritable(74.0059, 40.7127);
        final CoordinateWritable c_coord = new CoordinateWritable(71.3269, 40.7127);
        final CoordinateWritable d_coord = new CoordinateWritable(21.1133, 44.7274);

        // A_CLONE == A > B
        final List<CoordinateWritable> A_POLYGON = Arrays.asList(a_coord, b_coord, c_coord);
        final List<CoordinateWritable> B_POLYGON = Arrays.asList(b_coord, c_coord, d_coord);
        final List<CoordinateWritable> A_POLYGON_CLONE = Arrays.asList(a_coord, b_coord, c_coord);

        ByteSequence a_actual_bytes = translator.fromPolygon(A_POLYGON);
        ByteSequence b_actual_bytes = translator.fromPolygon(B_POLYGON);
        ByteSequence a_actual_bytes_cloned = translator.fromPolygon(A_POLYGON_CLONE);
        Comparator<List<CoordinateWritable>> comparator = new LexigraphicalListComparator<CoordinateWritable>();
        assertTrue(comparator.compare(A_POLYGON, B_POLYGON) > 0);
        assertTrue(comparator.compare(A_POLYGON, A_POLYGON) == 0);
        int val = comparator.compare(A_POLYGON, A_POLYGON_CLONE);
        assertEquals(0, val);
        assertTrue(comparator.compare(B_POLYGON, A_POLYGON) < 0);

        assertTrue(a_actual_bytes.compareTo(b_actual_bytes) > 0);
        assertTrue(a_actual_bytes.compareTo(a_actual_bytes_cloned) == 0);
        assertTrue(b_actual_bytes.compareTo(a_actual_bytes) < 0);
    }
    
    @Test
    public void testTextOrder() throws Exception {
        // A > B > C > D
        final Text a_text = new Text("yzzyy");
        final Text b_text = new Text("yzzy");
        final Text c_text = new Text("yyyy");
        final Text d_text = new Text("xxxx");

        ByteSequence a_actual = translator.fromText(a_text);
        ByteSequence b_actual = translator.fromText(b_text);
        ByteSequence c_actual = translator.fromText(c_text);
        ByteSequence d_actual = translator.fromText(d_text);

        // Since this is an object ensure that the order is consistent
        assertTrue(b_text.compareTo(a_text) < 0);
        assertTrue(b_text.compareTo(c_text) > 0);
        assertTrue(b_text.compareTo(d_text) > 0);

        assertTrue(c_text.compareTo(a_text) < 0);
        assertTrue(c_text.compareTo(b_text) < 0);
        assertTrue(c_text.compareTo(d_text) > 0);

        assertTrue(a_text.compareTo(b_text) > 0);
        assertTrue(a_text.compareTo(c_text) > 0);
        assertTrue(a_text.compareTo(d_text) > 0);

        assertTrue(d_text.compareTo(a_text) < 0);
        assertTrue(d_text.compareTo(b_text) < 0);
        assertTrue(d_text.compareTo(c_text) < 0);

        // now lets compare the byte sequences
        assertTrue("a > b", a_actual.compareTo(b_actual) > 0);
        assertTrue("b > c", b_actual.compareTo(c_actual) > 0);
        assertTrue("c > d", c_actual.compareTo(d_actual) > 0);
    }

    @Test
    public void testDateOrder() throws Exception {
        final long a_interval = 1056153600000L;
        final long b_interval = 1056153700000L;
        ByteSequence a_actual = translator.fromDate(a_interval);
        ByteSequence b_actual = translator.fromDate(b_interval);

        assertTrue("a < b", a_actual.compareTo(b_actual) < 0);
        assertTrue("b > a", b_actual.compareTo(a_actual) > 0);
    }
}