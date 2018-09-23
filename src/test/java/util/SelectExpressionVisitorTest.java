package util;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import operator.Operator;
import operator.ScanOperator;
import operator.Tuple;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SelectExpressionVisitorTest {

    static SelectExpressionVisitor visitor;
    //Catalog catalog = Catalog.getInstance();
    //HashMap<String, Integer> myschema = new HashMap<>();
    //private static final String queriesFile = "Samples/samples/input/queries.sql";


    @Test
    public void visit() {
//        HashMap<String, Integer> mySchema = new HashMap<>();
//        mySchema.put("id", 0);
//        mySchema.put("a", 1);
//        mySchema.put("b", 2);
//        mySchema.put("c", 3);
//        catalog.setCurrentSchema("Sailors");
//        Tuple tuple = new Tuple("1,4,5,6");

        String tableName = "Sailors";
        String tableFolder = "Samples/samples/input/db/data/";
        File file = new File(tableFolder + tableName);

        String statement = "SELECT * FROM Sailors S WHERE S.A = 3;";
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try{
            PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();
            Operator op = new ScanOperator(plainSelect, file);
            Catalog catalog = Catalog.getInstance();
            System.out.println(catalog.getCurrentSchema());
            Tuple currentTuple = op.getNextTuple();
            FromItem fromItem = plainSelect.getFromItem();
            System.out.println("From item is " + fromItem);
            Expression whereEx = plainSelect.getWhere();
            System.out.println("Where expression is " + whereEx);

            while(currentTuple != null){
                visitor = new SelectExpressionVisitor(currentTuple, op.getSchema());
                if (whereEx != null) {
                    whereEx.accept(visitor);
                    assertEquals(true, visitor.getResult());
                }
                System.out.println("select expression successfully");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void visit1() {
    }

    @Test
    public void visit2() {
    }

    @Test
    public void visit3() {
    }

    @Test
    public void visit4() {
    }

    @Test
    public void visit5() {
    }

    @Test
    public void visit6() {
    }

    @Test
    public void visit7() {
    }

    @Test
    public void visit8() {
    }
}