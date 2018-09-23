package operator;

import java.util.Map;

public abstract class Operator {

    public abstract Tuple getNextTuple();

    public abstract void reset();

    public abstract void dump(int i);

}
