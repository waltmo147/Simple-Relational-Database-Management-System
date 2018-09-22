package util;

import org.junit.Test;
import util.JoinExpressionVisitor;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Expression;


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

        String whereClause = "Sailors.A = Boats.D And Sailors.A = Reserves.G";
        Expression expr = CCJSqlParserUtil.parseCondExpression(whereClause);

        ExpressionVisitor joinExpress = new JoinExpressionVisitor();
		expr.accept(joinExpress);


    }
}