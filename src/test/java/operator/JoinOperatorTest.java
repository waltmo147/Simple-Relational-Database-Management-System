package operator;

import org.junit.Test;
import java.io.File;
import java.io.StringReader;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import util.Catalog;
import util.Constants;

public class JoinOperatorTest{

    @Test
    public void testJoin(){
        File file1 = new File(Catalog.getInstance().getDataPath("Sailors"));
        File file2 = new File(Catalog.getInstance().getDataPath("Reserves"));

        String statement = "SELECT * FROM Sailors, Reserves;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try{
            PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();
            Operator op1 = new ScanOperator(plainSelect, file1);
            Operator op2 = new ScanOperator(plainSelect, file2);
            Operator opJoin = new JoinOperator(op1, op2, plainSelect);

            while(opJoin.getNextTuple()!=null){
                System.out.println("read successfully");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}