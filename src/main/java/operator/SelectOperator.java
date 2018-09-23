package operator;

import model.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import util.SelectExpressionVisitor;

import java.util.Map;

public class SelectOperator extends Operator{

    private Operator prevOp;
    private Expression expression;
    private Map<String, Integer> currentSchema;

    public SelectOperator(Operator operator, PlainSelect plainSelect) {
        prevOp = operator;
        expression = plainSelect.getWhere();
        currentSchema = operator.getSchema();
    }

    @Override
    public Tuple getNextTuple() {
        Tuple next = prevOp.getNextTuple();
        if (expression != null) {
            while (next != null) {
                SelectExpressionVisitor sv = new SelectExpressionVisitor(next, prevOp.getSchema());
                expression.accept(sv);
                if (sv.getResult()) {
                    break;
                }
                next = prevOp.getNextTuple();
            }
        }
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
