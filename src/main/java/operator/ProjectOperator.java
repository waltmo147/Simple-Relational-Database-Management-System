package operator;

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
        Map<String, Integer> newSchema = new HashMap<>();

        currentSchema = operator.getSchema();

    }

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
