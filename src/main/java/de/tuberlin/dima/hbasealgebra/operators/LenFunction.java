package de.tuberlin.dima.hbasealgebra.operators;

import java.sql.SQLException;
import java.util.List;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.phoenix.expression.Expression;
import org.apache.phoenix.expression.function.ScalarFunction;
import org.apache.phoenix.parse.FunctionParseNode.Argument;
import org.apache.phoenix.parse.FunctionParseNode.BuiltInFunction;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveDoublePhoenixArray;
import org.apache.phoenix.schema.tuple.Tuple;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.jts.JtsLineString;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

//for now: only for lines
@BuiltInFunction(name=LenFunction.NAME,  args={ 
		@Argument(allowedTypes={PDataType.DOUBLE_ARRAY})} )
public class LenFunction extends ScalarFunction {

	public static final String NAME="LEN";
	public static GeometryFactory factory;
	private double[] dArray;
	public LenFunction() {
	}

	public LenFunction(List<Expression> children) {
		super(children);
	}
	
	@Override
	public boolean evaluate(Tuple tuple, ImmutableBytesWritable ptr) {
		Expression arg = getChildren().get(0);
        if (!arg.evaluate(tuple, ptr)) {
            return false;
        }
        
		try {
			dArray = (double[])((PrimitiveDoublePhoenixArray) PDataType.DOUBLE_ARRAY.toObject(ptr.get(),ptr.getOffset(),ptr.getLength(),arg.getDataType())).getArray();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(dArray.length<2){
			ptr.set(PDataType.LONG.toBytes(0));
			return true;
		}
		if(dArray.length>=7 && (dArray[0]==DataTypes.LINE || dArray[0]==DataTypes.DLINE)){
			ptr.set(PDataType.LONG.toBytes(Line.getLength(dArray)));
		}
        return true;
	}

	@Override
	public PDataType getDataType() {
		return PDataType.LONG;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
