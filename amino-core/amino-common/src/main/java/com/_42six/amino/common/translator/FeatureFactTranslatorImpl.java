package com._42six.amino.common.translator;

import com._42six.amino.common.FeatureFact;
import com._42six.amino.common.util.ByteArrayOutputStreamToBytesWritable;
import com._42six.amino.common.writable.CoordinateWritable;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import org.apache.accumulo.core.client.lexicoder.*;
import org.apache.accumulo.core.data.ArrayByteSequence;
import org.apache.accumulo.core.data.ByteSequence;
import org.apache.accumulo.core.util.ComparablePair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import java.util.List;

/**
 * This implementation converts double ratios and intervals to text by:
 *   - adding a large constant value to the ratio/interval
 *   - padding the ratio/interval with zeroes on the beginning and end
 * It converts them back to double by:
 *   - subtracting the same large constant value from the ratio/interval
 * <p/>
 * This ensures both positive and negative interval/ratios are correctly
 * ascii sorted when scanned.
 */
public class FeatureFactTranslatorImpl implements FeatureFactTranslatorInt {
    public static final Lexicoder<Text> TEXT_LEXICODER = new TextLexicoder();
    public static final Lexicoder<Double> DBL_LEXICODER = new DoubleLexicoder();
    public static final Lexicoder<Long> ULONG_LEXICODER = new ULongLexicoder();
    public static final PairLexicoder<Double, Double> DBL_PAIR_LEXICODER =
            new PairLexicoder<Double, Double>(DBL_LEXICODER, DBL_LEXICODER);

    public static final ListLexicoder<ComparablePair<Double, Double>> POLYGON_LEXICODER =
            new ListLexicoder<ComparablePair<Double, Double>>(DBL_PAIR_LEXICODER);


    @Override
    public ByteSequence fromFeatureFact(FeatureFact fact) {
        return ByteArrayOutputStreamToBytesWritable.toByteSequence(fact.getFact());
    }

    @Override
    public ByteSequence fromRatio(double fact) {
        return new ArrayByteSequence(DBL_LEXICODER.encode(fact));
    }

    @Override
    public double toRatio(BytesWritable fact) {
        return DBL_LEXICODER.decode(fact.copyBytes());
    }

    @Override
    public double toRatio(ByteSequence fact) {
        return DBL_LEXICODER.decode(fact.toArray());
    }

    @Override
    public double toRatio(byte[] fact) {
        return DBL_LEXICODER.decode(fact);
    }

    @Override
    public ByteSequence fromInterval(double fact) {
        return new ArrayByteSequence(DBL_LEXICODER.encode(fact));
    }

    @Override
    public ByteSequence fromCoordinate(CoordinateWritable fact) {
        ComparablePair<Double, Double> pair = new ComparablePair<Double, Double>(fact.longitudex, fact.latitudey);
        return new ArrayByteSequence(DBL_PAIR_LEXICODER.encode(pair));
    }

    @Override
    public ByteSequence fromPolygon(List<CoordinateWritable> fact) {
        List<ComparablePair<Double, Double>> points =Lists.transform(fact, new Function<CoordinateWritable, ComparablePair<Double, Double>>() {
            @Override
            public ComparablePair<Double, Double> apply(CoordinateWritable fact) {
                return new ComparablePair<Double, Double>(fact.longitudex, fact.latitudey);
            }
        });
        return new ArrayByteSequence(POLYGON_LEXICODER.encode(points));
    }

    @Override
    public ByteSequence fromText(String str) {
        return fromText(new Text(str));
    }

    @Override
    public ByteSequence fromText(Text str) {
        return new ArrayByteSequence(TEXT_LEXICODER.encode(str));
    }

    @Override
    public double toInterval(BytesWritable fact) {
        return DBL_LEXICODER.decode(fact.copyBytes());
    }

    @Override
    public double toInterval(ByteSequence fact) {
        return DBL_LEXICODER.decode(fact.toArray());
    }

    @Override
    public double toInterval(byte[] fact) {
        return DBL_LEXICODER.decode(fact);
    }

    // since timestamps are never negative and specified as a whole number of milliseconds, simply
    // store longs as left justified zero padded hex.
    @Override
    public long toDate(BytesWritable fact) {
        return ULONG_LEXICODER.decode(fact.copyBytes());
    }

    @Override
    public long toDate(ByteSequence fact) {
        return ULONG_LEXICODER.decode(fact.toArray());
    }

    @Override
    public long toDate(byte[] fact) {
        return ULONG_LEXICODER.decode(fact);
    }

    @Override
    public CoordinateWritable toCoordinate(BytesWritable fact) {
        ComparablePair<Double, Double> pair = DBL_PAIR_LEXICODER.decode(fact.copyBytes());
        return new CoordinateWritable(pair.getFirst(), pair.getSecond());
    }

    @Override
    public CoordinateWritable toCoordinate(ByteSequence fact) {
        ComparablePair<Double, Double> pair = DBL_PAIR_LEXICODER.decode(fact.toArray());
        return new CoordinateWritable(pair.getFirst(), pair.getSecond());
    }

    @Override
    public CoordinateWritable toCoordinate(byte[] fact) {
        ComparablePair<Double, Double> pair = DBL_PAIR_LEXICODER.decode(fact);
        return new CoordinateWritable(pair.getFirst(), pair.getSecond());
    }


    private static Function<ComparablePair<Double, Double>, CoordinateWritable> TO_COORD
            = new Function<ComparablePair<Double, Double>, CoordinateWritable>() {
        @Override
        public CoordinateWritable apply(ComparablePair<Double, Double> pair) {
            return new CoordinateWritable(pair.getFirst(), pair.getSecond());
        }
    };

    @Override
    public List<CoordinateWritable> toPolygon(BytesWritable fact) {
        List<ComparablePair<Double, Double>> points = POLYGON_LEXICODER.decode(fact.copyBytes());
        return Lists.transform(points, TO_COORD);
    }

    @Override
    public List<CoordinateWritable> toPolygon(ByteSequence fact) {
        List<ComparablePair<Double, Double>> points = POLYGON_LEXICODER.decode(fact.toArray());
        return Lists.transform(points, TO_COORD);
    }

    @Override
    public List<CoordinateWritable> toPolygon(byte[] fact) {
        List<ComparablePair<Double, Double>> points = POLYGON_LEXICODER.decode(fact);
        return Lists.transform(points, TO_COORD);
    }

    @Override
    public String toTextString(BytesWritable fact) {
        return toText(fact).toString();
    }


    @Override
    public String toTextString(ByteSequence fact) {
        return toText(fact).toString();
    }

    @Override
    public String toTextString(byte[] fact) {
        return toText(fact).toString();
    }

    @Override
    public Text toText(BytesWritable fact) {
        return TEXT_LEXICODER.decode(fact.copyBytes());
    }

    @Override
    public Text toText(ByteSequence fact) {
        return TEXT_LEXICODER.decode(fact.toArray());
    }

    @Override
    public Text toText(byte[] fact) {
        return TEXT_LEXICODER.decode(fact);
    }

    @Override
    public FeatureFact toFeatureFact(BytesWritable fact, FeatureFact dst) {
        //return ByteArrayOutputStreamToBytesWritable.toByteSequence(fact.getFact());
        return toFeatureFact(new ArrayByteSequence(fact.getBytes(), 0, fact.getLength()), dst);
    }

    @Override
    public FeatureFact toFeatureFact(ByteSequence fact, FeatureFact dst) {
        ByteArrayOutputStreamToBytesWritable.fromByteSequence(fact, dst.getFact());
        return dst;
    }

    @Override
    public FeatureFact toFeatureFact(byte[] fact, FeatureFact dst) {
        return toFeatureFact(new ArrayByteSequence(fact, 0, fact.length), dst);
    }

    @Override
    public ByteSequence fromDate(long fact) {
        return new ArrayByteSequence(ULONG_LEXICODER.encode(fact));
    }

    public static String bytesSequenceToHexString(ByteSequence writable) {
        return BaseEncoding.base16().encode(writable.getBackingArray(), 0, writable.length());
    }

    public static String bytesSequenceToHexStringWithSpaces(ByteSequence writable) {
        return BaseEncoding.base16().encode(writable.getBackingArray(), 0, writable.length()).replaceAll(".{2}", "$0 ").trim();
    }
}
