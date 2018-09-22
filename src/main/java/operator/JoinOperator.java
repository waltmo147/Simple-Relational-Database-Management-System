package operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import net.sf.jsqlparser.statement.select.PlainSelect;


public class JoinOperator extends Operator{
    private Operator op;

    public JoinOperator(PlainSelect ps){
        this.op = null;
    }

    public JoinOperator(Operator op, PlainSelect ps){
        this.op = op;
    }

    public Tuple getNextTuple(){
        Tuple tuple = null;
        
        return tuple;
    }

    public void reset(){
    }

}