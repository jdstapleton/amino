package com._42six.amino.common.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CoordinateWritable extends SpatialWritable {
	public double longitudex;
	public double latitudey;
	
	public CoordinateWritable()
	{
		
	}
	
	public CoordinateWritable(double longitudex, double latitudey)
	{
		this.longitudex = longitudex;
		this.latitudey = latitudey;
	}

	@Override
	public void readFields(DataInput in) throws IOException 
	{
		this.longitudex = in.readDouble();
		this.latitudey = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException 
	{
		out.writeDouble(this.longitudex);
		out.writeDouble(this.latitudey);
	}

    @Override
    public int compareTo(SpatialWritable thatSpatial) {
        CoordinateWritable that = (CoordinateWritable) thatSpatial;

        int val = Double.compare(this.longitudex, that.longitudex);
        if ( val == 0 ) {
            return Double.compare(this.latitudey, that.latitudey);
        }
        return val;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && compareTo((CoordinateWritable) o) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(longitudex);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitudey);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
	public SpatialType getType() 
	{
		return SpatialType.Coordinate;
	}


    //TODO toString() should return the geohash....For now lets just return the pairing, for debugging sake
    @Override
    public String toString() {
        return "CoordinateWritable{" +
                "longitudex=" + longitudex +
                ", latitudey=" + latitudey +
                '}';
    }
}
