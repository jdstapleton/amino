package com._42six.amino.common;

import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.DoubleWritable;
import com._42six.amino.common.translator.FeatureFactTranslatorInt;

public class RatioFeatureFact extends IntervalFeatureFact
{

	public RatioFeatureFact(double fact) 
	{
		super(fact);
	}

	public RatioFeatureFact() 
	{

	}

	@Override
	public FeatureFactType getType() {
		return FeatureFactType.RATIO;
	}

	@Override
	public ByteSequence toText(FeatureFactTranslatorInt translator){
		return translator.fromRatio(((DoubleWritable)fact).get());
	}

    @Override
    public void fromText(FeatureFactTranslatorInt translator, ByteSequence buffer) {
        ((DoubleWritable)fact).set(translator.toRatio(buffer));
    }
}
