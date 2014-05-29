package de.tuberlin.dima.hbasealgebra.operators;

import java.sql.SQLException;
import java.util.List;

import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.phoenix.expression.Expression;
import org.apache.phoenix.expression.function.ScalarFunction;
import org.apache.phoenix.parse.FunctionParseNode.Argument;
import org.apache.phoenix.parse.FunctionParseNode.BuiltInFunction;
import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PhoenixArray;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveDoublePhoenixArray;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveFloatPhoenixArray;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveIntPhoenixArray;
import org.apache.phoenix.schema.SortOrder;
import org.apache.phoenix.schema.tuple.Tuple;
import org.apache.phoenix.util.StringUtil;

@BuiltInFunction(name=PointToVarcharFunction.NAME,  args={ 
		@Argument(allowedTypes={PDataType.DOUBLE_ARRAY})} )
public class PointToVarcharFunction extends ScalarFunction {
	public static final String NAME = "PT_TO_VARCHAR";
	private double[] dArray;
	public PointToVarcharFunction() {
	}

	public PointToVarcharFunction(List<Expression> children) {
		super(children);
	}

	@Override
	public boolean evaluate(Tuple tuple, ImmutableBytesWritable ptr) {
		// Get the child argument and evaluate it first
        Expression arg = getChildren().get(0);
        if (!arg.evaluate(tuple, ptr)) {
            return false;
        }
        
        
		try {
			dArray = (double[])((PrimitiveDoublePhoenixArray) PDataType.DOUBLE_ARRAY.toObject(ptr.get(),ptr.getOffset(),ptr.getLength(),arg.getDataType())).getArray();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        String str = dArray[0] + "," + dArray[1];
        ptr.set(PDataType.VARCHAR.toBytes(str));
        return true;
	}

	@Override
	public PDataType getDataType() {
		return PDataType.VARCHAR;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
