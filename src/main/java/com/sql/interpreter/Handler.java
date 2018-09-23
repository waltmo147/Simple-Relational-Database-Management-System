package com.sql.interpreter;

import operator.*;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import util.Catalog;

import java.io.FileReader;
import java.io.StringReader;

public class Handler {
    public static void parseSql() {
        try {
            // try
            String inputPath = Catalog.getInstance().getSqlQueriesPath();
            CCJSqlParser parser = new CCJSqlParser(new FileReader(inputPath));
            Statement statement;
            int ind = 0;
            while ((statement = parser.Statement()) != null) {
                System.out.println("Read statement: " + statement);
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                System.out.println("Select body is " + select.getSelectBody());
                Operator operator = constructQueryPlan(plainSelect);
                operator.dump(ind);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
     /**
     * consturct a left deep join query plan
     * @param plainSelect
     * @return
     */
    public Operator constructQueryPlan(PlainSelect plainSelect){
        int tableCount;
        Operator opLeft;
        if(plainSelect.getJoins() == null){
            tableCount = 1;
        }
        else{
            tableCount = 1 + plainSelect.getJoins().size();
        }
        opLeft = new ScanOperator(plainSelect, 0);
        for(int i = 1; i < tableCount; ++i){
            Operator opRight = new ScanOperator(plainSelect, i);
            opLeft = new JoinOperator(opLeft, opRight, plainSelect);
            if(plainSelect.getWhere() != null){
                opLeft = new SelectOperator(opLeft, plainSelect);
            }
        }
        if(tableCount == 1 && plainSelect.getWhere() != null){
            opLeft = new SelectOperator(opLeft, plainSelect);
        }
        opLeft = new ProjectOperator(opLeft, plainSelect);
        if(plainSelect.getDistinct() != null){
            opLeft = new SortOperator(opLeft, plainSelect);
            opLeft = new DuplicateEliminationOperator(opLeft);
        }
        else if(plainSelect.getDistinct() != null){
            opLeft = new SortOperator(opLeft, plainSelect);
        }
        return opLeft;
    }
}
