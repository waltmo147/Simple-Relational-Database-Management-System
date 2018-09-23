package util;

import org.junit.Test;
import util.JoinExpressionVisitor;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Expression;
import java.io.FileReader;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;


public class JoinExpressionVisitorTest{

    @Test
    public void testExtractExpression(){
        // sqlQuery = "SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Boats.D And Sailors.A = Reserves.G;";
        Map<String, Integer> schemaMap = new HashMap<>();
        schemaMap.put("Sailors.A", 0);
        schemaMap.put("Sailors.B", 1);
        schemaMap.put("Sailors.C", 2);

        schemaMap.put("Reserves.G", 3);
        schemaMap.put("Reserves.H", 4);

        Catalog.getInstance().setCurrentSchema(schemaMap);
        Catalog.getInstance().setAliases("Sailors");
        Catalog.getInstance().setAliases("Boats");
        Catalog.getInstance().setAliases("Reserves");

        Map<String, Integer> a = Catalog.getInstance().getCurrentSchema();

        String whereClause = "Sailors.A = Boats.D And Sailors.A = Reserves.G";
        try{
            CCJSqlParser parser = new CCJSqlParser(new FileReader("test.sql"));
            Statement statement;
            statement = parser.Statement();
            Select select = (Select) statement;
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Expression expr = plainSelect.getWhere();

            JoinExpressionVisitor joinExpress = new JoinExpressionVisitor();
            expr.accept(joinExpress);
            Expression output = joinExpress.getExpression();
            int aa =1;
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}