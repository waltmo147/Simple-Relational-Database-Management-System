package operator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import util.SelectExpressionVisitor;

import java.util.HashMap;

public class SelectOperator {
    private ScanOperator scan;
    private PlainSelect pl;


    public Tuple getNextTuple() {
        Tuple next = scan.getNextTuple();
        Expression where = pl.getWhere();
        if (where == null) {
            return next;
        }
        SelectExpressionVisitor sv = new
        while ()

    }
}
