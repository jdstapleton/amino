package com._42six.amino.common;

import com._42six.amino.common.translator.FeatureFactTranslatorInt;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.IOException;

public class DateFeatureFact extends IntervalFeatureFact
{
    // number of milliseconds to bucket features into
    protected static long constraint = 86400000;
    //protected long timeInMillis;

    /**
     * Default constructor
     */
    protected DateFeatureFact() { super(new LongWritable(0)); }

    /**
     * Constructor for timestamps
     *
     * @param timeInMillis      timestamp in milliseconds
     */
    public DateFeatureFact(long timeInMillis) {
        this(timeInMillis, constraint);
    }

    /**
     * Main constructor used for initializing the superclass fact member
     *
     * @param timeInMillis      timestamp in milliseconds
     * @param constraint        millisecond mask used to constraint the timestamp
     */
    protected DateFeatureFact(long timeInMillis, long constraint) {
        super(new LongWritable(constrain(timeInMillis, constraint)));
    }

    @Override
    public FeatureFactType getType()
    {
        return FeatureFactType.DATE;
    }

    public long asTimeStamp() {
        return ((LongWritable)this.fact).get();
    }

    @Override
    public int compareTo(FeatureFact ff)
    {

        return ((LongWritable)this.fact).compareTo(((LongWritable)ff.fact));
    }

    @Override
    public Writable setWritable(DataInput in) throws IOException {
        setFact(new LongWritable(in.readLong()));
        return getFact();
    }

    /**
     * Takes a timetsamp and returns that timestamp with some part removed
     * Constraint passed in is a number of milliseconds used to mask the timestamp
     * To constrain to one hour, pass the number of milliseconds in one hour
     *
     * @param timeInMillis      timestamp in milliseconds
     * @param constraint        number of milliseconds to strip of timestamp
     *
     * @return                  the time in milliseconds with constraint removed
     */
    protected static long constrain(long timeInMillis, long constraint)
    {
        return timeInMillis - (timeInMillis % constraint);
    }

    public Text asText() {
        return new Text(fact.toString());
    }

    @Override
    public ByteSequence toText(FeatureFactTranslatorInt translator) {
        return translator.fromDate(((LongWritable)fact).get());
    }

    @Override
    public void fromText(FeatureFactTranslatorInt translator, ByteSequence buffer) {
        ((LongWritable)fact).set(translator.toDate(buffer));
    }

}
