package operator;

import model.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.Test;
import util.Catalog;

import java.io.File;
import java.io.StringReader;

public class ProjectOperatorTest {

    @Test
    public void getNextTuple() {

        String tableName = "Sailors";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

        String statement = "SELECT * FROM Sailors S WHERE S.A <= 3;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try{
            PlainSelect plainSelect = (PlainSelect) ((Select) parserManager
                    .parse(new StringReader(statement))).getSelectBody();
            System.out.println("plainSelect: ");

            Operator op = new ScanOperator(plainSelect, file);
            Catalog catalog = Catalog.getInstance();
            System.out.println(catalog.getCurrentSchema());
            Tuple currentTuple = op.getNextTuple();
            FromItem fromItem = plainSelect.getFromItem();
            System.out.println("From item is " + fromItem);
            Expression whereEx = plainSelect.getWhere();
            System.out.println("Where expression is " + whereEx);
            int index = op.getSchema().get("S.A");
            System.out.println(index);

        }catch(Exception e){
            e.printStackTrace();
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