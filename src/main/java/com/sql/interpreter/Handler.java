package com.sql.interpreter;

import net.sf.jsqlparser.statement.select.PlainSelect;
import operator.Operator;
import operator.ProjectOperator;
import operator.DuplicateEliminationOperator;
import operator.JoinOperator;
import operator.ScanOperator;
import operator.SelectOperator;
import operator.SortOperator;

public class Handler {
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
