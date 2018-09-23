package util;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import operator.Tuple;
import org.junit.Test;

import java.io.FileReader;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SelectExpressionVisitorTest {

    static SelectExpressionVisitor visitor;
    Catalog catalog = Catalog.getInstance();
    HashMap<String, Integer> myschema = new HashMap<>();
    private static final String queriesFile = "Samples/samples/input/queries.sql";


    @Test
    public void visit() {
        HashMap<String, Integer> mySchema = new HashMap<>();
        mySchema.put("id", 0);
        mySchema.put("a", 1);
        mySchema.put("b", 2);
        mySchema.put("c", 3);
        catalog.setCurrentSchema("Sailors");
        Tuple tuple = new Tuple("1,4,5,6");

        try {
            // try
            CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            if ((statement = parser.Statement()) != null) {
                visitor = new SelectExpressionVisitor(tuple);
                System.out.println("Read statement: " + statement);
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                FromItem fromItem = plainSelect.getFromItem();
                System.out.println("From item is " + fromItem);
                Expression whereEx = plainSelect.getWhere();
                System.out.println("Where expression is " + whereEx);
                if (whereEx != null) {
                    whereEx.accept(visitor);
                    assertEquals(true, visitor.getResult());
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
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