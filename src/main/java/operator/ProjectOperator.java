package operator;

import model.Tuple;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectOperator extends Operator {

    Operator prevOp;
    List<SelectItem> selectItems;
    Map<String, Integer> currentSchema;

    public ProjectOperator (Operator operator, PlainSelect plainSelect) {
        prevOp = operator;
        selectItems = plainSelect.getSelectItems();
        // yet did not handle cases: select A,D from S, B
        Map<String, Integer> oldSchema = operator.getSchema();
        currentSchema = new HashMap<>();
        for (SelectItem selectItem : selectItems) {
            currentSchema.put(selectItem.toString(),
                    oldSchema.get(selectItem.toString()));
        }

    }

    @Override
    public Tuple getNextTuple() {
        Tuple next = prevOp.getNextTuple();
        long[] data = new long[currentSchema.size()];
        int i=0;
        for (Integer ind: currentSchema.values()) {
            data[i] = next.getDataAt(ind);
        }
        next = new Tuple(data);
        return next;
    }

    @Override
    public void reset() {
        prevOp.reset();
    }

    @Override
    public void dump(int i) {

    }

    @Override
    public Map<String, Integer> getSchema() {
        return currentSchema;
    }
}
