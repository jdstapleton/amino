package com._42six.amino.common;

import com._42six.amino.common.translator.FeatureFactTranslatorInt;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.IOException;

import static com._42six.amino.common.util.ByteArrayOutputStreamToBytesWritable.toByteSequence;

public abstract class FeatureFact implements Comparable<FeatureFact>
{
	protected Writable fact;

	public FeatureFact(Writable fact)
	{
		this.fact = fact;
        }

	public FeatureFact()
	{

	}
	public ByteSequence toText(){
        return toByteSequence(fact);
    }

	public ByteSequence toText(FeatureFactTranslatorInt translator) {
        return translator.fromFeatureFact(this);
	}

    public abstract void fromText(FeatureFactTranslatorInt translator, ByteSequence buffer);

	public Writable getFact() {
		return fact;
	}

	public abstract FeatureFactType getType();

	public void setFact(Writable fact) {
		this.fact = fact;
	}

	public abstract Writable setWritable(DataInput in) throws IOException;
}
