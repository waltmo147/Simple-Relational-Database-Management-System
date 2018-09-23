package operator;

import java.util.Map;

public class ProjectOperator extends Operator {

    Operator prevOperator;
    Map<String, Integer> currentSchema;


    @Override
    public Tuple getNextTuple() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void dump(int i) {

    }

    @Override
    public Map<String, Integer> getSchema() {
        return null;
    }
}
