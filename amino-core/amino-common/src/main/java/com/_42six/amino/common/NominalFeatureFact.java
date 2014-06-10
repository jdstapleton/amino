package com._42six.amino.common;

import java.io.DataInput;
import java.io.IOException;

import com._42six.amino.common.translator.FeatureFactTranslatorInt;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class NominalFeatureFact extends FeatureFact
{

	public NominalFeatureFact(String fact) 
	{
		super(new Text(fact));
	}

	protected NominalFeatureFact() 
	{

	}

    @Override
	public int compareTo(FeatureFact ff) 
	{
		return 1;
	}

	@Override
	public FeatureFactType getType() {
		return FeatureFactType.NOMINAL;
	}

	@Override
	public Writable setWritable(DataInput in) throws IOException
	{
		setFact(new Text(Text.readString(in)));
                
		return getFact();
	}


    @Override
    public void fromText(FeatureFactTranslatorInt translator, ByteSequence buffer) {
        ((Text)fact).set(translator.toTextString(buffer));
    }

    @Override
    public ByteSequence toText(FeatureFactTranslatorInt translator) {
        return translator.fromText((Text)fact);
    }

    @Override
	public String toString()
	{
		return getFact().toString();
	}

}
