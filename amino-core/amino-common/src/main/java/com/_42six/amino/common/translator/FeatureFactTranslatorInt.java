package com._42six.amino.common.translator;

import com._42six.amino.common.FeatureFact;
import com._42six.amino.common.writable.CoordinateWritable;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import java.util.List;

public interface FeatureFactTranslatorInt {
	
	/**
	 * Translates a generic FeatureFact to Text
	 * @param fact fact to be translated
	 * @return Text
	 */
	public ByteSequence fromFeatureFact(FeatureFact fact);
	
	/**
	 * Translate a ratio double to Text
	 * @param fact double ratio value
	 * @return Text
	 */
	public ByteSequence fromRatio(double fact);
	
	/**
	 * Translate an interval double to Text
	 * @param fact double interval value
	 * @return Text
	 */
	public ByteSequence fromInterval(double fact);


    /**
     * Translate an coordinate into a lexigraphical ordered byte sequence
     * @param fact The coordinate value
     * @return Text
     */
    public ByteSequence fromCoordinate(CoordinateWritable fact);

    /**
     * Translate an polygon into a lexigraphical ordered byte sequence
     * @param fact The polygon points value
     * @return Text
     */
    public ByteSequence fromPolygon(List<CoordinateWritable> fact);


    public ByteSequence fromText(String str);
    public ByteSequence fromText(Text str);

	/**
	 * Translate a ratio string to double
	 * @param fact string ratio value
	 * @return double
	 */
	public double toRatio(BytesWritable fact);
    public double toRatio(ByteSequence fact);
    public double toRatio(byte[] fact);
	
	/**
	 * Translate an interval string to double
	 * @param fact string interval value
	 * @return double
	 */
	public double toInterval(BytesWritable fact);
    public double toInterval(ByteSequence fact);
    public double toInterval(byte[] fact);
        
    /**
     * Translate a date string to long
     * @param fact string date value
     * @return long
     */
    public long toDate(BytesWritable fact);
    public long toDate(ByteSequence fact);
    public long toDate(byte[] fact);

    public CoordinateWritable toCoordinate(BytesWritable fact);
    public CoordinateWritable toCoordinate(ByteSequence fact);
    public CoordinateWritable toCoordinate(byte[] fact);

    public List<CoordinateWritable> toPolygon(BytesWritable fact);
    public List<CoordinateWritable> toPolygon(ByteSequence fact);
    public List<CoordinateWritable> toPolygon(byte[] fact);

    public String toTextString(BytesWritable fact);
    public String toTextString(ByteSequence fact);
    public String toTextString(byte[] fact);

    public Text toText(BytesWritable fact);
    public Text toText(ByteSequence fact);
    public Text toText(byte[] fact);

    public FeatureFact toFeatureFact(BytesWritable fact, FeatureFact dst);
    public FeatureFact toFeatureFact(ByteSequence fact, FeatureFact dst);
    public FeatureFact toFeatureFact(byte[] fact, FeatureFact dst);

    /**
     * Translate a date long to Text
     * @param fact long date value
     * @return Text
     */
    public ByteSequence fromDate(long fact);
}
