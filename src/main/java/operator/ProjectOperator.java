package operator;

import model.Tuple;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import util.Catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        if (selectItems.get(0).toString() == "*") {
            currentSchema = operator.getSchema();
        } else {
            Map<String, Integer> oldSchema = operator.getSchema();
            currentSchema = new HashMap<>();
            for (SelectItem selectItem : selectItems) {
                currentSchema.put(selectItem.toString(),
                        oldSchema.get(selectItem.toString()));
            }
        }
    }

    /**
     * @return the next tuple selected by the project operator
     */
    @Override
    public Tuple getNextTuple() {
        Tuple next = prevOp.getNextTuple();
        if (next != null && currentSchema != prevOp.getSchema()) {
            long[] data = new long[currentSchema.size()];
            int i=0;
            for (Integer ind: currentSchema.values()) {
                data[i] = next.getDataAt(ind);
            }
            next = new Tuple(data);
        }
        return next;
    }

    /**
     * reset the project operator would be resetting the previous operator
     */
    @Override
    public void reset() {
        prevOp.reset();
    }

    /**
     * @return the current schema of project operator
     */
    @Override
    public Map<String, Integer> getSchema() {
        return currentSchema;
    }
}
