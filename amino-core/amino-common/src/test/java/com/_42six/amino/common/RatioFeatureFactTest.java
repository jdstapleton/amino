package com._42six.amino.common;

import com._42six.amino.common.translator.FeatureFactTranslatorImpl;
import com._42six.amino.common.translator.FeatureFactTranslatorInt;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.DoubleWritable;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static com._42six.amino.common.translator.FeatureFactTranslatorImpl.bytesSequenceToHexStringWithSpaces;
import static org.junit.Assert.*;

public class RatioFeatureFactTest {

	RatioFeatureFact fact;

    private static double byteSequenceToDouble(ByteSequence sequence) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(sequence.toArray()));
        DoubleWritable actual = new DoubleWritable();
        actual.readFields(dataInputStream);
        return actual.get();
    }

	@Before
	public void setup(){
		fact = new RatioFeatureFact(4.26);
	}
	
	@Test
	public void testToTextFeatureFactTranslatorInt() {
		FeatureFactTranslatorInt translator = new FeatureFactTranslatorImpl();
        ByteSequence result = fact.toText(translator);
        String actual = bytesSequenceToHexStringWithSpaces(result);
		assertEquals("08 C0 11 0A 3D 70 A3 D7 0A", actual);
        // And now back again
        assertEquals(4.26, translator.toRatio(result), 0.0001);
	}

	@Test
	public void testGetType() {		
		assertEquals(FeatureFactType.RATIO, fact.getType());
	}

	@Test
	public void testToText() throws IOException {
        ByteSequence result = fact.toText();
        double actual = byteSequenceToDouble(result);
		assertEquals(4.26, actual, 0.0001);
	}

	@Test
	public void testSetWritable() throws IOException {
		fact.fact = null;
		assertNull(fact.getFact());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		out.writeDouble(4.26);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(os.toByteArray()));		
		fact.setWritable(in);
		assertNotNull(fact.getFact());
	}

	@Test
	public void testRatioFeatureFactDouble() {
		fact = new RatioFeatureFact(1.23);
		assertEquals("1.23", fact.toString());
	}

	@Test
	public void testCompareTo() {
		assertTrue(fact.compareTo(new RatioFeatureFact(1.0)) > 0);
		assertTrue(fact.compareTo(new RatioFeatureFact(4.26)) == 0);
		assertTrue(fact.compareTo(new RatioFeatureFact(11.0)) < 0);
	}

	@Test
	public void testToString() {
		String s = fact.toString();
		assertEquals("4.26", s);
	}

	@Test
	public void testGetFact() {
		DoubleWritable dw = new DoubleWritable(4.20);
		fact.setFact(dw);
		assertEquals(dw, fact.getFact());
	}

	@Test
	public void testSetFact() {
		DoubleWritable dw = new DoubleWritable(4.20);
		fact.setFact(dw);
		assertEquals(dw, fact.getFact());
	}

}
