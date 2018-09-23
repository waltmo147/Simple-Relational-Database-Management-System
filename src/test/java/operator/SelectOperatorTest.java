package operator;

import model.Tuple;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.*;

public class SelectOperatorTest {

    @Test
    public void getNextTuple() throws Exception {
        String tableName = "Boats";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

        String statement = "SELECT * FROM Boats AS BT WHERE BT.E = 9;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();
        Operator scanOp = new ScanOperator(plainSelect, file);

        Operator selectOp = new SelectOperator(scanOp, plainSelect);

        Tuple tuple = selectOp.getNextTuple();
        while(tuple != null){
            assertEquals(9, tuple.getDataAt(1));
            tuple = selectOp.getNextTuple();
        }
    }

    @Test
    public void reset() {
    }

    @Test
    public void dump() {
    }

    @Test
    public void getSchema() {
    }
}