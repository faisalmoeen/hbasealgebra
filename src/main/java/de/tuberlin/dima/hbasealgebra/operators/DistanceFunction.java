package de.tuberlin.dima.hbasealgebra.operators;

import java.sql.SQLException;
import java.util.List;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.phoenix.expression.Expression;
import org.apache.phoenix.expression.function.ArrayIndexFunction;
import org.apache.phoenix.expression.function.ScalarFunction;
import org.apache.phoenix.parse.ParseException;
import org.apache.phoenix.parse.FunctionParseNode.Argument;
import org.apache.phoenix.parse.FunctionParseNode.BuiltInFunction;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveDoublePhoenixArray;
import org.apache.phoenix.schema.tuple.Tuple;

import com.vividsolutions.jts.geom.GeometryFactory;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;

@BuiltInFunction(name = DistanceFunction.NAME, args = {
		@Argument(allowedTypes = { PDataType.DOUBLE_ARRAY}),
		@Argument(allowedTypes = { PDataType.DOUBLE_ARRAY }) })
public class DistanceFunction extends ScalarFunction {
	public static final String NAME="DISTANCE";
	private double[] dArray1;
	private double[] dArray2;
	private GeometryFactory factory;
	
	public DistanceFunction() {
		factory=new GeometryFactory();
	}

	public DistanceFunction(List<Expression> children) {
		super(children);
		factory=new GeometryFactory();
	}

	@Override
	public boolean evaluate(Tuple tuple, ImmutableBytesWritable ptr) {
		Expression arg1 = children.get(0);
		if (!arg1.evaluate(tuple, ptr)) {
		  return false;
		} else if (ptr.getLength() == 0) {
		  return true;
		}
		try {
			dArray1 = (double[])((PrimitiveDoublePhoenixArray) PDataType.DOUBLE_ARRAY.toObject(ptr.get(),ptr.getOffset(),ptr.getLength(),arg1.getDataType())).getArray();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Expression arg2 = children.get(1);
		if (!arg2.evaluate(tuple, ptr)) {
		  return false;
		} else if (ptr.getLength() == 0) {
		  return true;
		}
		
		try {
			dArray2 = (double[])((PrimitiveDoublePhoenixArray) PDataType.DOUBLE_ARRAY.toObject(ptr.get(),ptr.getOffset(),ptr.getLength(),arg2.getDataType())).getArray();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if((dArray1[0]==DataTypes.DLINE||dArray1[0]==DataTypes.LINE) 
				&& (dArray2[0]==DataTypes.DLINE || dArray2[0]==DataTypes.LINE)){
			double distance=Line.createLine(dArray1,factory).distance(Line.createLine(dArray2,factory));
			ptr.set(PDataType.DOUBLE.toBytes(distance));
		}
		
		return true;
	}

	@Override
	public PDataType getDataType() {
		return PDataType.DOUBLE;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
