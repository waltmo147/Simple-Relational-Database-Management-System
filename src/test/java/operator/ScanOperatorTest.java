package operator;

import org.junit.Test;
import java.io.File;
import java.io.StringReader;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class ScanOperatorTest{

    @Test
    public void testReadFile(){
        String statement = "SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try{
            PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();
            Operator op = new ScanOperator(plainSelect, 0);

            while(op.getNextTuple()!=null){
                System.out.println("read successfully");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}