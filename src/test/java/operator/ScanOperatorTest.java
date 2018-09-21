package operator;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.Test;
import java.io.File;
import java.io.FileReader;

public class ScanOperatorTest{

    private static final String queriesFile = "Samples/samples/input/queries.sql";

    @Test
    public void testReadFile(){
        try {
            // try
            CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            while ((statement = parser.Statement()) != null) {
                System.out.println("Read statement: " + statement);
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                FromItem fromItem = plainSelect.getFromItem();
                System.out.println("From item is " + fromItem);
                Expression where = plainSelect.getWhere();
                System.out.println("Where expression is " + where);

            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }

//        String tableName = "Boats";
//        String tableFolder = "Samples/samples/input/db/data/";
//        File file = new File(tableFolder + tableName);
//
//        Operator op = new ScanOperator(file);
//        while(op.getNextTuple()!=null){
//            System.out.println("read successfully");
//        }

    }
}