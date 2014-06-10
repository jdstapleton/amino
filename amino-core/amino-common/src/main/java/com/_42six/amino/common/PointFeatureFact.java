package com._42six.amino.common;

import java.io.DataInput;
import java.io.IOException;

import com._42six.amino.common.translator.FeatureFactTranslatorInt;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.Writable;
import com._42six.amino.common.writable.CoordinateWritable;

public class PointFeatureFact extends FeatureFact
{
	protected PointFeatureFact()
	{
		
	}

    public PointFeatureFact(double longitudex, double latitudey)
	{
		super(new CoordinateWritable(longitudex, latitudey));
	}

	@Override
	public Writable getFact() 
	{
		return (CoordinateWritable)this.fact;
	}

	@Override
	public FeatureFactType getType() 
	{
		return FeatureFactType.POINT;
	}

	@Override
	public void setFact(Writable fact) 
	{
		this.fact = (CoordinateWritable)fact;
	}

	@Override
	public Writable setWritable(DataInput in) throws IOException 
	{
		CoordinateWritable cw = new CoordinateWritable();
		cw.readFields(in);
		this.fact = cw;
		return this.fact;
	}

	@Override
	public int compareTo(FeatureFact ff) 
	{
		return ((CoordinateWritable)this.fact).compareTo(((CoordinateWritable)ff.fact));
	}
	
	@Override
	public String toString()
	{
		return Double.toString(((CoordinateWritable)this.fact).longitudex) + "," + Double.toString(((CoordinateWritable)this.fact).latitudey);
	}

    @Override
    public ByteSequence toText(FeatureFactTranslatorInt translator) {
        return translator.fromCoordinate((CoordinateWritable)fact);
    }

    @Override
    public void fromText(FeatureFactTranslatorInt translator, ByteSequence buffer) {
        CoordinateWritable buf = translator.toCoordinate(buffer);
        CoordinateWritable thisCoordindate = (CoordinateWritable)fact;
        thisCoordindate.longitudex = buf.longitudex;
        thisCoordindate.latitudey = buf.latitudey;
    }


}
