package operator;

import model.Tuple;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.*;

public class SortOperatorTest {
    @Test
    public void getNextTuple() throws Exception {

        String tableName = "Boats";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

        String statement = "SELECT * FROM Boats AS BT ORDER BY BT.F;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();
        Operator op = new ScanOperator(plainSelect, file);

        Operator sortOp = new SortOperator(op, plainSelect);
        Tuple tuple = sortOp.getNextTuple();
        long last = Long.MIN_VALUE;
        while(tuple != null){
            long cur = tuple.getDataAt(2);
            assertEquals(true, last <= cur);
            tuple = sortOp.getNextTuple();
        }

    }

}